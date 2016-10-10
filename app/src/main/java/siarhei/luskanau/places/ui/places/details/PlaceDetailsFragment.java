package siarhei.luskanau.places.ui.places.details;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.abstracts.BaseRecyclerAdapter;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsHeaderBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsMapBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsPhotoBinding;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.ui.places.PlaceDetailsPresenterInterface;
import siarhei.luskanau.places.utils.AppUtils;

public class PlaceDetailsFragment extends BaseFragment {

    private static final String TAG = "PlaceDetailsFragment";

    private PlacesApi placesApi;
    private Subscription subscription;
    private PlaceDetailsAdapter adapter;
    private Place place;
    private List<PlacePhotoMetadata> placePhotoMetadataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PlaceDetailsAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<BindableViewHolder>() {
            @Override
            public void onClick(Context context, BindableViewHolder holder, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof PlaceMapAdapterItem) {
                    Place place = ((PlaceMapAdapterItem) item).place;
                    Snackbar.make(getView(), place.getName() + "\n" + place.getAddress(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseSubscription(subscription);
        subscription = null;
    }

    private PlacesApi getPlacesApi() {
        if (placesApi == null) {
            placesApi = new PlacesApi(getActivity());
        }
        return placesApi;
    }

    public void onPlaceIdUpdated(String placeId) {
        updateAdapter();

        releaseSubscription(subscription);
        subscription = getPlacesApi().getPlace(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Place>() {
                    @Override
                    public void onNext(Place data) {
                        onPlaceUpdated(data);
                    }
                });
    }

    public void onPlaceUpdated(Place place) {
        this.place = place;
        if (place != null) {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity())
                    .onToolbarTitle(!TextUtils.isEmpty(place.getName()) ? place.getName() : place.getAddress());
        } else {
            AppUtils.getParentInterface(PlaceDetailsPresenterInterface.class, getActivity()).onToolbarTitle(null);
        }

        updateAdapter();

        if (place != null) {
            releaseSubscription(subscription);
            subscription = getPlacesApi().getPlacePhotos(place.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<List<PlacePhotoMetadata>>() {
                        @Override
                        public void onNext(List<PlacePhotoMetadata> data) {
                            onPlacePhotosUpdated(data);
                        }
                    });
        } else {
            onPlacePhotosUpdated(null);
        }
    }

    private void onPlacePhotosUpdated(List<PlacePhotoMetadata> placePhotoMetadataList) {
        this.placePhotoMetadataList = placePhotoMetadataList;
        if (placePhotoMetadataList != null) {
            for (PlacePhotoMetadata placePhotoMetadata : placePhotoMetadataList) {
                Log.d(TAG, "w" + placePhotoMetadata.getMaxWidth() + " h" + placePhotoMetadata.getMaxHeight()
                        + " " + placePhotoMetadata.getAttributions());
            }
        }
        updateAdapter();
    }

    private void updateAdapter() {
        List<Object> adapterItems = new ArrayList<>();
        if (place != null) {
            adapterItems.add(new PlaceHeaderAdapterItem(place));
            adapterItems.add(new PlaceMapAdapterItem(place));
        }
        if (placePhotoMetadataList != null) {
            adapterItems.addAll(placePhotoMetadataList);
        }
        adapter.setData(adapterItems);
    }

    private class PlaceDetailsAdapter extends BaseRecyclerArrayAdapter<Object, BindableViewHolder> {

        public static final int TYPE_PLACE_HEADER = 1;
        public static final int TYPE_MAP = 2;
        public static final int TYPE_PHOTO = 3;

        @Override
        public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            int layoutResId;
            switch (viewType) {
                case TYPE_PLACE_HEADER:
                    layoutResId = R.layout.list_item_place_details_header;
                    break;

                case TYPE_MAP:
                    layoutResId = R.layout.list_item_place_details_map;
                    break;

                case TYPE_PHOTO:
                    layoutResId = R.layout.list_item_place_details_photo;
                    break;

                default:
                    throw new IllegalArgumentException("Unknown view type: " + viewType);
            }

            ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
            return new BindableViewHolder<>(binding);
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TYPE_PLACE_HEADER:
                    PlaceHeaderAdapterItem placeHeaderAdapterItem = (PlaceHeaderAdapterItem) getItem(position);
                    ((ListItemPlaceDetailsHeaderBinding) holder.getBindings())
                            .item.setPlace(placeHeaderAdapterItem.place);
                    break;

                case TYPE_MAP:
                    PlaceMapAdapterItem placeMapAdapterItem = (PlaceMapAdapterItem) getItem(position);
                    ((ListItemPlaceDetailsMapBinding) holder.getBindings())
                            .item.setPlace(placeMapAdapterItem.place);
                    break;

                case TYPE_PHOTO:
                    PlacePhotoMetadata placePhotoMetadata = (PlacePhotoMetadata) getItem(position);
                    ((ListItemPlaceDetailsPhotoBinding) holder.getBindings())
                            .item.setPlacePhotoMetadata(position, placePhotoMetadata, getPlacesApi());
                    break;

                default:
                    throw new IllegalArgumentException("Unknown view type: " + holder.getItemViewType());
            }
        }

        @Override
        public int getItemViewType(int position) {
            Object object = getItem(position);
            if (object instanceof PlaceHeaderAdapterItem) {
                return TYPE_PLACE_HEADER;
            } else if (object instanceof PlaceMapAdapterItem) {
                return TYPE_MAP;
            } else if (object instanceof PlacePhotoMetadata) {
                return TYPE_PHOTO;
            }
            return -1;
        }

    }

    private static class PlaceHeaderAdapterItem {
        private Place place;

        public PlaceHeaderAdapterItem(Place place) {
            this.place = place;
        }
    }

    private static class PlaceMapAdapterItem {
        private Place place;

        public PlaceMapAdapterItem(Place place) {
            this.place = place;
        }
    }

}