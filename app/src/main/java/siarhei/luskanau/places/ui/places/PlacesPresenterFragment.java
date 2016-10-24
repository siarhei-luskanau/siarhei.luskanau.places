package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.abstracts.GoogleApiInterface;
import siarhei.luskanau.places.model.PlaceModel;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.AppUtils;

public class PlacesPresenterFragment extends BaseFragment implements PlacesPresenterInterface {

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
    public void onPlaceSelected(PlaceModel place, String placeId) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.placeDetailsFragment);
        if (fragment instanceof PlaceDetailsFragment) {
            PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) fragment;
            if (place != null) {
                placeDetailsFragment.onPlaceUpdated(place);
            } else {
                placeDetailsFragment.onPlaceIdUpdated(placeId);
            }

            fragment = getChildFragmentManager().findFragmentById(R.id.placeListFragment);
            if (fragment instanceof PlaceListFragment) {
                PlaceListFragment placeListFragment = (PlaceListFragment) fragment;
                if (place != null) {
                    placeListFragment.onPlaceHighlighted(place.getId());
                } else {
                    placeListFragment.onPlaceHighlighted(placeId);
                }
            }
        } else {
            AppNavigationUtil.startActivityWithAnimations(getActivity(),
                    AppNavigationUtil.getPlaceDetailsIntent(getContext(), place.getId()));
        }
    }

    public void loadData() {
        if (!AppUtils.getParentInterface(GoogleApiInterface.class, getActivity()).isPermissionsGranted()) {
            AppUtils.getParentInterface(GoogleApiInterface.class, getActivity()).requestPermissions();
        } else {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.placeListFragment);
            if (fragment instanceof PlaceListFragment) {
                PlaceListFragment placeListFragment = (PlaceListFragment) fragment;
                placeListFragment.loadData();
            }
        }
    }

}