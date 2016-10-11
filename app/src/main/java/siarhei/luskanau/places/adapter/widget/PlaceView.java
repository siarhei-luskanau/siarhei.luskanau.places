package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.Place;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceBinding;

public class PlaceView extends LinearLayout {

    private ViewPlaceBinding binding;

    public PlaceView(Context context) {
        super(context);
    }

    public PlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place, this, true);
    }

    public void setPlace(Place place) {
        binding.placeName.setText(place.getName());
        binding.placeAddress.setText(place.getAddress());
    }

}