package siarhei.luskanau.places.presentation.view.placedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BaseRecyclerFragment;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.adapter.PlaceDetailsAdapter;
import siarhei.luskanau.places.domain.Photo;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.presentation.internal.di.components.PlaceComponent;
import siarhei.luskanau.places.presentation.presenter.PlaceDetailsPresenter;
import siarhei.luskanau.places.presentation.view.placelist.PlaceDetailsPresenterInterface;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseRecyclerFragment implements PlaceDetailsView {

    private static final String TAG = "PlaceDetailsFragment";

    @Inject
    protected PlaceDetailsPresenter placeDetailsPresenter;

    private PlaceDetailsAdapter adapter;
    private Place place;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSwipeRefreshLayout().setEnabled(false);
    }

    private PlaceDetailsPresenter getPlaceDetailsPresenter() {
        if (placeDetailsPresenter == null) {
            this.getComponent(PlaceComponent.class).inject(this);
            this.placeDetailsPresenter.setView(this);
        }
        return placeDetailsPresenter;
    }

    @Override
    protected void setupRecyclerView(RecyclerView recyclerView) {
        super.setupRecyclerView(recyclerView);

        adapter = new PlaceDetailsAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<BindableViewHolder>() {
            @Override
            public void onClick(Context context, BindableViewHolder holder, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof PlaceDetailsAdapter.PlacePhoneAdapterItem) {
                    CharSequence phoneNumber = ((PlaceDetailsAdapter.PlacePhoneAdapterItem) item).getPhoneNumber();
                    getPlaceDetailsPresenter().onPlacePhoneClicked(phoneNumber);
                } else if (item instanceof PlaceDetailsAdapter.PlaceWebsiteAdapterItem) {
                    String url = ((PlaceDetailsAdapter.PlaceWebsiteAdapterItem) item).getUri();
                    getPlaceDetailsPresenter().onPlaceWebsiteClicked(url);
                } else if (item instanceof PlaceDetailsAdapter.PlaceMapAdapterItem) {
                    Place place = ((PlaceDetailsAdapter.PlaceMapAdapterItem) item).getPlace();
                    String url = AppUtils.buildMapUrl(place.getLatitude(), place.getLongitude());
                    getPlaceDetailsPresenter().onPlaceMapClicked(url);
                } else if (item instanceof PlaceDetailsAdapter.PlacePhotoAdapterItem) {
                    int photoPosition = ((PlaceDetailsAdapter.PlacePhotoAdapterItem) item).getPosition();
                    getPlaceDetailsPresenter().onPlacePhotoClicked(place.getId(), photoPosition);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void onPlaceIdUpdated(String placeId) {
        getPlaceDetailsPresenter().setPlaceId(placeId);
        updateAdapter();
        setRefreshing(true);
    }

    private void updateAdapter() {
        List<Object> adapterItems = new ArrayList<>();
        if (place != null) {
            adapterItems.add(new PlaceDetailsAdapter.PlaceHeaderAdapterItem(place));
            if (!TextUtils.isEmpty(place.getPhoneNumber())) {
                adapterItems.add(new PlaceDetailsAdapter.PlacePhoneAdapterItem(place.getPhoneNumber()));
            }
            if (!TextUtils.isEmpty(place.getWebsiteUri())) {
                adapterItems.add(new PlaceDetailsAdapter.PlaceWebsiteAdapterItem(place.getWebsiteUri()));
            }
            adapterItems.add(new PlaceDetailsAdapter.PlaceMapAdapterItem(place));
            if (place.getPhotos() != null) {
                for (int i = 0; i < place.getPhotos().size(); i++) {
                    Photo photo = place.getPhotos().get(i);
                    adapterItems.add(new PlaceDetailsAdapter.PlacePhotoAdapterItem(photo, i));
                }
            }
        }
        adapter.setData(adapterItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPlaceDetailsPresenter().destroy();
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
    public void renderPlace(Place place) {
        setRefreshing(false);
        this.place = place;
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName()) ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity()).onToolbarTitle(null);
        }
        updateAdapter();
    }

    @Override
    public void viewPlacePhone(CharSequence phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phoneNumber));
            navigator.startActivityWithAnimations(getActivity(), intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void viewPlaceWebsite(String url) {
        navigator.startActivityWithAnimations(getActivity(),
                navigator.getWebIntent(getContext(), url, place.getName()));
    }

    @Override
    public void viewPlaceMap(String url) {
        navigator.startActivityWithAnimations(getActivity(),
                navigator.getWebIntent(getContext(), url, place.getName()));
    }

    @Override
    public void viewPlacePhoto(String placeId, int photoPosition) {
        navigator.startActivityWithAnimations(getActivity(),
                navigator.getPlacePhotosIntent(getContext(), place.getId(), photoPosition));
    }
}
