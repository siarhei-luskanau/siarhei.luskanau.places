package siarhei.luskanau.places.ui.places;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.rx.SimpleObserver;

public class BasePlacesPresenterFragment extends BaseFragment {

    private static final String TAG = "PlacesPresenterFragment";
    private static final int PLACE_PICKER_REQUEST = 1;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                Log.d(TAG, String.valueOf(place));
                return;
            }
        }
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
                .subscribe(new SimpleObserver<ArrayList<AutocompletePrediction>>() {
                    @Override
                    public void onNext(ArrayList<AutocompletePrediction> data) {
                        onDataLoaded(data);
                    }
                });

        try {
            startActivityForResult(new PlacePicker.IntentBuilder().build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void onDataLoaded(ArrayList<AutocompletePrediction> data) {
        if (data != null) {
            for (AutocompletePrediction autocompletePrediction : data) {
                Log.d(TAG, String.valueOf(autocompletePrediction));
            }
        }
    }

}