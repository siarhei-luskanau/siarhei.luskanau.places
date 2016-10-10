package siarhei.luskanau.places.ui.places.details.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.databinding.ViewPlaceDetailPhotoBinding;
import siarhei.luskanau.places.utils.glide.PlacePhotoId;

public class PlaceDetailPhotoView extends LinearLayout {

    private ViewPlaceDetailPhotoBinding binding;

    public PlaceDetailPhotoView(Context context) {
        super(context);
    }

    public PlaceDetailPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_photo, this, true);
    }

    public void setPlacePhotoMetadata(Place place, PlacePhotoMetadata placePhotoMetadata, PlacesApi placesApi) {
        Glide.with(getContext())
                .load(new PlacePhotoId(place, placePhotoMetadata, placesApi))
                .fitCenter()
                .placeholder(null)
                .into(binding.placePhoto);
    }

}