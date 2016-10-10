package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.rx.SimpleObserver;

public class PlacesPresenterFragment extends BaseFragment {

    private static final String TAG = "PlacesPresenterFragment";

    private PlacesApi placesApi;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_presenter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        placesApi = new PlacesApi(getActivity());
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

    private void loadData() {
        releaseSubscription(subscription);
        subscription = placesApi.getAutocompletePredictions("")
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