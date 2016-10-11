package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;
import siarhei.luskanau.places.rx.SimpleObserver;
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

            fragment = getChildFragmentManager().findFragmentById(R.id.placeListFragment);
            if (fragment instanceof PlaceListFragment) {
                PlaceListFragment placeListFragment = (PlaceListFragment) fragment;
                placeListFragment.onPlaceHighlighted(place);
            }
        } else {
            AppNavigationUtil.startActivityWithAnimations(getActivity(),
                    AppNavigationUtil.getPlaceDetailsIntent(getContext(), place.getId()));
        }
    }

    private PlacesApi getPlacesApi() {
        return AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).getPlacesApi();
    }

    public void loadData() {
        if (!AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).isPermissionsGranted()) {
            AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).requestPermissions();
            return;
        }

        releaseSubscription(subscription);
        subscription = Observable.interval(0, 30, TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<List<Place>>>() {
                    @Override
                    public Observable<List<Place>> call(Long aLong) {
                        return getPlacesApi().getCurrentPlace()
                                .onErrorReturn(new Func1<Throwable, List<Place>>() {
                                    @Override
                                    public List<Place> call(Throwable e) {
                                        Log.e(TAG, e.getMessage(), e);
                                        return null;
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<Place>>() {
                    @Override
                    public void onNext(List<Place> data) {
                        onDataLoaded(data);
                    }
                });
    }

    private void onDataLoaded(List<Place> data) {
        if (data != null) {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.placeListFragment);
            if (fragment instanceof PlaceListFragment) {
                PlaceListFragment placeListFragment = (PlaceListFragment) fragment;
                placeListFragment.updatePlaces(data);
            }
        }
    }

}