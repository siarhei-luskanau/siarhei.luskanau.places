package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.ui.places.details.PlaceDetailsFragment;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.AppUtils;

public class PlacesPresenterFragment extends BaseFragment implements PlacesPresenterInterface {

    private static final String TAG = "PlacesPresenterFragment";

    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_presenter, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseSubscription(subscription);
        subscription = null;
    }

    @Override
    public void onPlaceSelected(Place place) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.placeDetailsFragment);
        if (fragment instanceof PlaceDetailsFragment) {
            PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) fragment;
            placeDetailsFragment.onPlaceUpdated(place);
        } else {
            AppNavigationUtil.startActivityWithAnimations(getActivity(),
                    AppNavigationUtil.getPlaceDetailsIntent(getContext(), place.getId()));
        }
    }

    private PlacesApi getPlacesApi() {
        return AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).getPlacesApi();
    }

    private void loadData() {
        releaseSubscription(subscription);
        subscription = getPlacesApi().getAutocompletePredictions("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<AutocompletePrediction>>() {
                    @Override
                    public void onNext(List<AutocompletePrediction> data) {
                        onDataLoaded(data);
                    }
                });
    }

    private void onDataLoaded(List<AutocompletePrediction> data) {
        if (data != null) {
            for (AutocompletePrediction autocompletePrediction : data) {
                Log.d(TAG, String.valueOf(autocompletePrediction));
            }
        }
    }

}