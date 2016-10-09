package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.rx.SimpleObserver;

public class BasePlacesPresenterFragment extends BaseFragment {

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

    private void loadData() {
        releaseSubscription(subscription);
        subscription = Observable.empty()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Object>() {
                    @Override
                    public void onNext(Object data) {
                    }
                });
    }

}