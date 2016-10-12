package siarhei.luskanau.places.ui.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BaseRecyclerFragment;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.adapter.PlacesAdapter;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceListFragment extends BaseRecyclerFragment {

    private static final String TAG = "PlaceListFragment";
    private static final int PLACE_PICKER_REQUEST = 1;

    private Subscription subscription;
    private PlacesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.place_picker_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(new PlacePicker.IntentBuilder().build(getActivity()),
                            PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, e.getMessage(), e);
                    GooglePlayServicesUtil.showErrorDialogFragment(e.getConnectionStatusCode(),
                            getActivity(), PlaceListFragment.this, 100, null);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, e.getMessage(), e);
                    GooglePlayServicesUtil.showErrorDialogFragment(e.errorCode,
                            getActivity(), PlaceListFragment.this, 100, null);
                }
            }
        });
    }

    @Override
    protected void setupRecyclerView(RecyclerView recyclerView) {
        super.setupRecyclerView(recyclerView);

        adapter = new PlacesAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<BindableViewHolder>() {
            @Override
            public void onClick(Context context, BindableViewHolder holder, int position) {
                onPlaceSelected(adapter.getItem(position));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseSubscription(subscription);
        subscription = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                Snackbar.make(getView(), place.getName() + "\n" + place.getAddress(), Snackbar.LENGTH_LONG).show();
                onPlaceSelected(place);
            }
        }
    }

    private PlacesApi getPlacesApi() {
        return AppUtils.getParentInterface(PlacesApiInterface.class, getActivity()).getPlacesApi();
    }

    public void loadData() {
        setRefreshing(true);
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
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Place> data) {
                        setRefreshing(false);
                        onDataLoaded(data);
                    }
                });
    }

    private void onDataLoaded(List<Place> places) {
        adapter.setData(places);
    }

    private void onPlaceSelected(Place place) {
        PlacesPresenterInterface placesPresenterInterface = AppUtils.getParentInterface(
                PlacesPresenterInterface.class,
                getActivity(), getParentFragment(), getTargetFragment());
        placesPresenterInterface.onPlaceSelected(place);
    }

    public void onPlaceHighlighted(Place place) {
        adapter.setSelectedPlaceId(place.getId());
    }

}