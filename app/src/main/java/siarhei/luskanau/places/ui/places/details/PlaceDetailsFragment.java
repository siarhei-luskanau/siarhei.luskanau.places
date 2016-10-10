package siarhei.luskanau.places.ui.places.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.ui.places.PlaceDetailsPresenterInterface;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseFragment {

    private static final String TAG = "PlaceDetailsFragment";

    private PlacesApi placesApi;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseSubscription(subscription);
        subscription = null;
    }

    private PlacesApi getPlacesApi() {
        if (placesApi == null) {
            placesApi = new PlacesApi(getActivity());
        }
        return placesApi;
    }

    public void onPlaceIdUpdated(String placeId) {
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
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName()) ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity()).onToolbarTitle(null);
        }

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

    private void onPlacePhotosUpdated(List<PlacePhotoMetadata> data) {
        if (data != null) {
            for (PlacePhotoMetadata placePhotoMetadata : data) {
                Log.d(TAG, String.valueOf(placePhotoMetadata.getAttributions()));
            }
        }
    }

}