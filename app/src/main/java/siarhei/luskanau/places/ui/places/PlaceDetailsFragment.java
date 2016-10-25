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

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BaseRecyclerFragment;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.abstracts.GoogleApiInterface;
import siarhei.luskanau.places.adapter.PlaceDetailsAdapter;
import siarhei.luskanau.places.api.GoogleApi;
import siarhei.luskanau.places.model.PhotoModel;
import siarhei.luskanau.places.model.PlaceModel;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseRecyclerFragment {

    private static final String TAG = "PlaceDetailsFragment";
    private Subscription subscription;
    private PlaceDetailsAdapter adapter;
    private PlaceModel place;

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
                    String uri = ((PlaceDetailsAdapter.PlaceWebsiteAdapterItem) item).getUri();
                    AppNavigationUtil.startActivityWithAnimations(getActivity(),
                            AppNavigationUtil.getWebIntent(context, uri, place.getName()));
                } else if (item instanceof PlaceDetailsAdapter.PlaceMapAdapterItem) {
                    PlaceModel place = ((PlaceDetailsAdapter.PlaceMapAdapterItem) item).getPlace();
                    String url = AppUtils.buildMapUrl(place.getLatitude(), place.getLongitude());
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

    private GoogleApi getGoogleApi() {
        return AppUtils.getParentInterface(GoogleApiInterface.class, getActivity()).getGoogleApi();
    }

    public void onPlaceIdUpdated(String placeId) {
        updateAdapter();

        setRefreshing(true);
        releaseSubscription(subscription);
        subscription = getGoogleApi().getPlace(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<PlaceModel>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshing(false);
                    }

                    @Override
                    public void onNext(PlaceModel data) {
                        setRefreshing(false);
                        onPlaceUpdated(data);
                    }
                });
    }

    public void onPlaceUpdated(PlaceModel place) {
        this.place = place;
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName()) ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity()).onToolbarTitle(null);
        }
        updateAdapter();
    }

    private void updateAdapter() {
        List<Object> adapterItems = new ArrayList<>();
        if (place != null) {
            adapterItems.add(new PlaceDetailsAdapter.PlaceHeaderAdapterItem(place));
            if (!TextUtils.isEmpty(place.getPhoneNumber())) {
                adapterItems.add(new PlaceDetailsAdapter.PlacePhoneAdapterItem(place.getPhoneNumber()));
            }
            if (!TextUtils.isEmpty(place.getWebsiteUri())) {
                adapterItems.add(new PlaceDetailsAdapter.PlaceWebsiteAdapterItem(place.getWebsiteUri()));
            }
            adapterItems.add(new PlaceDetailsAdapter.PlaceMapAdapterItem(place));
            if (place.getPhotos() != null) {
                for (int i = 0; i < place.getPhotos().size(); i++) {
                    PhotoModel photo = place.getPhotos().get(i);
                    adapterItems.add(new PlaceDetailsAdapter.PlacePhotoAdapterItem(photo, i));
                }
            }
        }
        adapter.setData(adapterItems);
    }

}