package siarhei.luskanau.places.ui.places;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BaseRecyclerFragment;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.adapter.PlaceDetailsAdapter;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseRecyclerFragment {

    private static final String TAG = "PlaceDetailsFragment";
    private Subscription subscription;
    private PlaceDetailsAdapter adapter;
    private Place place;
    private List<PlacePhotoMetadata> placePhotoMetadataList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSwipeRefreshLayout().setEnabled(false);
    }

    @Override
    protected void setupRecyclerView(RecyclerView recyclerView) {
        super.setupRecyclerView(recyclerView);

        adapter = new PlaceDetailsAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<BindableViewHolder>() {
            @Override
            public void onClick(Context context, BindableViewHolder holder, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof PlaceDetailsAdapter.PlacePhoneAdapterItem) {
                    try {
                        CharSequence phoneNumber = ((PlaceDetailsAdapter.PlacePhoneAdapterItem) item).getPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phoneNumber));
                        AppNavigationUtil.startActivityWithAnimations(getActivity(), intent);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else if (item instanceof PlaceDetailsAdapter.PlaceWebsiteAdapterItem) {
                    Uri uri = ((PlaceDetailsAdapter.PlaceWebsiteAdapterItem) item).getUri();
                    AppNavigationUtil.startActivityWithAnimations(getActivity(),
                            AppNavigationUtil.getWebIntent(context, uri.toString(), place.getName()));
                } else if (item instanceof PlaceDetailsAdapter.PlaceMapAdapterItem) {
                    LatLng latLng = ((PlaceDetailsAdapter.PlaceMapAdapterItem) item).getLatLng();
                    String url = AppUtils.buildMapUrl(latLng);
                    AppNavigationUtil.startActivityWithAnimations(getActivity(),
                            AppNavigationUtil.getWebIntent(context, url, place.getName()));
                } else if (item instanceof PlaceDetailsAdapter.PlacePhotoAdapterItem) {
                    int photoPosition = ((PlaceDetailsAdapter.PlacePhotoAdapterItem) item).getPosition();
                    AppNavigationUtil.startActivityWithAnimations(getActivity(),
                            AppNavigationUtil.getPlacePhotosIntent(context, place.getId(), photoPosition));
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseSubscription(subscription);
        subscription = null;
    }

    private PlacesApi getPlacesApi() {
        return AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).getPlacesApi();
    }

    public void onPlaceIdUpdated(String placeId) {
        updateAdapter();

        setRefreshing(true);
        releaseSubscription(subscription);
        subscription = getPlacesApi().getPlace(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Place>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshing(false);
                    }

                    @Override
                    public void onNext(Place data) {
                        setRefreshing(false);
                        onPlaceUpdated(data);
                    }
                });
    }

    public void onPlaceUpdated(Place place) {
        this.place = place;
        this.placePhotoMetadataList = null;
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName()) ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity()).onToolbarTitle(null);
        }

        updateAdapter();

        if (place != null) {
            releaseSubscription(subscription);
            subscription = getPlacesApi().getPlacePhotos(place.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<List<PlacePhotoMetadata>>() {
                        @Override
                        public void onNext(List<PlacePhotoMetadata> data) {
                            onPlacePhotosUpdated(data);
                        }
                    });
        }
    }

    private void onPlacePhotosUpdated(List<PlacePhotoMetadata> placePhotoMetadataList) {
        this.placePhotoMetadataList = placePhotoMetadataList;
        updateAdapter();
    }

    private void updateAdapter() {
        List<Object> adapterItems = new ArrayList<>();
        if (place != null) {
            adapterItems.add(new PlaceDetailsAdapter.PlaceHeaderAdapterItem(place));
            if (!TextUtils.isEmpty(place.getPhoneNumber())) {
                adapterItems.add(new PlaceDetailsAdapter.PlacePhoneAdapterItem(place.getPhoneNumber()));
            }
            if (place.getWebsiteUri() != null) {
                adapterItems.add(new PlaceDetailsAdapter.PlaceWebsiteAdapterItem(place.getWebsiteUri()));
            }
            adapterItems.add(new PlaceDetailsAdapter.PlaceMapAdapterItem(place.getLatLng()));
        }
        if (placePhotoMetadataList != null) {
            PlacesApi placesApi = getPlacesApi();
            for (int i = 0; i < placePhotoMetadataList.size(); i++) {
                PlacePhotoMetadata placePhotoMetadata = placePhotoMetadataList.get(i);
                adapterItems.add(new PlaceDetailsAdapter.PlacePhotoAdapterItem(place, i,
                        placePhotoMetadata, placesApi));
            }
        }
        adapter.setData(adapterItems);
    }

}