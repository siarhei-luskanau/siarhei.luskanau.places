package siarhei.luskanau.places.presentation.view.placelist;

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
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerFragment;
import siarhei.luskanau.places.adapter.PlacesAdapter;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.presentation.internal.di.components.PlaceComponent;
import siarhei.luskanau.places.presentation.presenter.PlaceListPresenter;

public class PlaceListFragment extends BaseRecyclerFragment implements PlaceListView {

    private static final String TAG = "PlaceListFragment";
    private static final int PLACE_PICKER_REQUEST = 1;

    @Inject
    protected PlaceListPresenter placeListPresenter;

    private PlacesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_list, container, false);
    }

    private PlaceListPresenter getPlaceListPresenter() {
        if (placeListPresenter == null) {
            this.getComponent(PlaceComponent.class).inject(this);
            this.placeListPresenter.setView(this);
        }
        return placeListPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.place_picker_fab);
        fab.setOnClickListener(view1 -> {
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
        });
    }

    @Override
    protected void setupRecyclerView(RecyclerView recyclerView) {
        super.setupRecyclerView(recyclerView);

        adapter = new PlacesAdapter();
        adapter.setOnItemClickListener((context, holder, position) ->
                onPlaceSelected(adapter.getItem(position).getId())
        );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPlaceListPresenter().destroy();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showError(String message) {
        setRefreshing(false);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRefreshing(boolean refreshing) {
        setRefreshing(refreshing);
    }

    @Override
    public void renderPlaceList(LatLng location, List<Place> places) {
        adapter.setData(location, places);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(getContext(), data);
                Snackbar.make(getView(), place.getName() + "\n" + place.getAddress(), Snackbar.LENGTH_LONG).show();
                onPlaceSelected(place.getId());
            }
        }
    }

    public void loadData() {
        setRefreshing(true);
        getPlaceListPresenter().updatePlaceList();
    }

    private void onPlaceSelected(String placeId) {
        navigator.startActivityWithAnimations(getActivity(),
                navigator.getPlaceDetailsIntent(getContext(), placeId));
    }
}
