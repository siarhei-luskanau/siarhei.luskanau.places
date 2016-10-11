package siarhei.luskanau.places.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsHeaderBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsMapBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsPhoneBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsPhotoBinding;
import siarhei.luskanau.places.databinding.ListItemPlaceDetailsWebsiteBinding;
import siarhei.luskanau.places.utils.glide.PlacePhotoId;
import siarhei.luskanau.places.utils.glide.PlacePhotoIdModelLoader;

public class PlaceDetailsAdapter extends BaseRecyclerArrayAdapter<Object, BindableViewHolder> {

    public static final int TYPE_PLACE_HEADER = 1;
    public static final int TYPE_PHONE = 2;
    public static final int TYPE_WEBSITE = 3;
    public static final int TYPE_MAP = 4;
    public static final int TYPE_PHOTO = 5;

    public PlaceDetailsAdapter(Context context) {
        Glide.get(context).register(PlacePhotoId.class, InputStream.class, new PlacePhotoIdModelLoader.Factory());
    }

    @Override
    public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        int layoutResId;
        switch (viewType) {
            case TYPE_PLACE_HEADER:
                layoutResId = R.layout.list_item_place_details_header;
                break;

            case TYPE_PHONE:
                layoutResId = R.layout.list_item_place_details_phone;
                break;

            case TYPE_WEBSITE:
                layoutResId = R.layout.list_item_place_details_website;
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

            case TYPE_PHONE:
                PlacePhoneAdapterItem placePhoneAdapterItem = (PlacePhoneAdapterItem) getItem(position);
                ((ListItemPlaceDetailsPhoneBinding) holder.getBindings())
                        .item.setPhoneNumber(placePhoneAdapterItem.phoneNumber);
                break;

            case TYPE_WEBSITE:
                PlaceWebsiteAdapterItem websiteAdapterItem = (PlaceWebsiteAdapterItem) getItem(position);
                ((ListItemPlaceDetailsWebsiteBinding) holder.getBindings())
                        .item.setUri(websiteAdapterItem.uri);
                break;

            case TYPE_MAP:
                PlaceMapAdapterItem placeMapAdapterItem = (PlaceMapAdapterItem) getItem(position);
                ((ListItemPlaceDetailsMapBinding) holder.getBindings())
                        .item.setLatLng(placeMapAdapterItem.latLng);
                break;

            case TYPE_PHOTO:
                PlacePhotoAdapterItem placePhotoAdapterItem = (PlacePhotoAdapterItem) getItem(position);
                ((ListItemPlaceDetailsPhotoBinding) holder.getBindings())
                        .item.setPlacePhotoMetadata(placePhotoAdapterItem.place,
                        placePhotoAdapterItem.placePhotoMetadata, placePhotoAdapterItem.placesApi);
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
        } else if (object instanceof PlacePhoneAdapterItem) {
            return TYPE_PHONE;
        } else if (object instanceof PlaceWebsiteAdapterItem) {
            return TYPE_WEBSITE;
        } else if (object instanceof PlaceMapAdapterItem) {
            return TYPE_MAP;
        } else if (object instanceof PlacePhotoAdapterItem) {
            return TYPE_PHOTO;
        }
        return -1;
    }

    public static class PlaceHeaderAdapterItem {
        private Place place;

        public PlaceHeaderAdapterItem(Place place) {
            this.place = place;
        }
    }

    public static class PlacePhoneAdapterItem {
        private CharSequence phoneNumber;

        public PlacePhoneAdapterItem(CharSequence phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    public static class PlaceWebsiteAdapterItem {
        private Uri uri;

        public PlaceWebsiteAdapterItem(Uri uri) {
            this.uri = uri;
        }
    }

    public static class PlaceMapAdapterItem {
        private LatLng latLng;

        public PlaceMapAdapterItem(LatLng latLng) {
            this.latLng = latLng;
        }

        public LatLng getLatLng() {
            return latLng;
        }
    }

    public static class PlacePhotoAdapterItem {
        private Place place;
        private PlacePhotoMetadata placePhotoMetadata;
        private PlacesApi placesApi;

        public PlacePhotoAdapterItem(Place place, PlacePhotoMetadata placePhotoMetadata, PlacesApi placesApi) {
            this.place = place;
            this.placePhotoMetadata = placePhotoMetadata;
            this.placesApi = placesApi;
        }
    }

}