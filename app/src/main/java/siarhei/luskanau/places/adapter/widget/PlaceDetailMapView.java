package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailMapBinding;

public class PlaceDetailMapView extends LinearLayout {

    private ViewPlaceDetailMapBinding binding;

    public PlaceDetailMapView(Context context) {
        super(context);
    }

    public PlaceDetailMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_map, this, true);
    }

    public void setLatLng(LatLng latLng) {
        //binding.placeLatLng.setText(String.valueOf(latLng));
        binding.placeLatLng.setText(R.string.place_detail_show_on_map);
    }

}