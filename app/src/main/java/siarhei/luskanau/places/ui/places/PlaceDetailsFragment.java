package siarhei.luskanau.places.ui.places;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.adapter.PlaceDetailsAdapter;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseFragment {

    private Subscription subscription;
    private PlaceDetailsAdapter adapter;
    private Place place;
    private List<PlacePhotoMetadata> placePhotoMetadataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PlaceDetailsAdapter(getContext());
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<BindableViewHolder>() {
            @Override
            public void onClick(Context context, BindableViewHolder holder, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof PlaceDetailsAdapter.PlaceMapAdapterItem) {
                    LatLng latLng = ((PlaceDetailsAdapter.PlaceMapAdapterItem) item).getLatLng();
                    String url = AppUtils.buildMapUrl(latLng);
                    AppNavigationUtil.startActivityWithAnimations(getActivity(),
                            AppNavigationUtil.getWebIntent(context, url, place.getName()));
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        releaseSubscription(subscription);
        subscription = getPlacesApi().getPlace(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Place>() {
                    @Override
                    public void onNext(Place data) {
                        onPlaceUpdated(data);
                    }
                });
    }

    public void onPlaceUpdated(Place place) {
        this.place = place;
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
        } else {
            onPlacePhotosUpdated(null);
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
            for (PlacePhotoMetadata placePhotoMetadata : placePhotoMetadataList) {
                adapterItems.add(new PlaceDetailsAdapter.PlacePhotoAdapterItem(place, placePhotoMetadata, placesApi));
            }
        }
        adapter.setData(adapterItems);
    }

}