package siarhei.luskanau.places.presentation.view.photos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.domain.Photo;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.presentation.internal.di.components.PlaceComponent;
import siarhei.luskanau.places.presentation.presenter.PhotosPresenter;
import siarhei.luskanau.places.presentation.view.placelist.PlaceDetailsPresenterInterface;
import siarhei.luskanau.places.utils.AppUtils;

public class PlacePhotosFragment extends BaseFragment implements PhotosView {

    private static final String POSITION = "POSITION";

    @Inject
    protected PhotosPresenter photosPresenter;

    private ViewPager viewPager;
    private PhotosPagerAdapter adapter;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PhotosPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION)) {
            position = savedInstanceState.getInt(POSITION);
        } else {
            position = AppUtils.getParentInterface(PlacePhotosPresenterInterface.class, getActivity()).getPosition();
        }
    }

    private PhotosPresenter getPhotosPresenter() {
        if (photosPresenter == null) {
            this.getComponent(PlaceComponent.class).inject(this);
            this.photosPresenter.setView(this);
        }
        return photosPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();

        String placeId = AppUtils.getParentInterface(PlacePhotosPresenterInterface.class, getActivity()).getPlaceId();
        getPhotosPresenter().setPlaceId(placeId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPhotosPresenter().destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, viewPager.getCurrentItem());
    }

    public Photo getPhoto(int position) {
        return adapter.getPhoto(position);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderPlace(Place place) {
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName())
                            ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(null);
        }

        adapter.setData(place != null ? place.getPhotos() : null);
        viewPager.setCurrentItem(position);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof PhotoItemFragment) {
                    PhotoItemFragment photoItemFragment = (PhotoItemFragment) fragment;
                    photoItemFragment.updatePhoto();
                }
            }
        }
    }

    private static class PhotosPagerAdapter extends FragmentPagerAdapter {

        private List<Photo> data = new ArrayList<>();

        public PhotosPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(List<Photo> data) {
            this.data.clear();
            if (data != null) {
                this.data.addAll(data);
            }
            notifyDataSetChanged();
        }

        public Photo getPhoto(int position) {
            if (data != null && position < data.size()) {
                return data.get(position);
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoItemFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
}
