package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailHeaderBinding;
import siarhei.luskanau.places.model.PlaceModel;

public class PlaceDetailHeaderView extends LinearLayout {

    private ViewPlaceDetailHeaderBinding binding;

    public PlaceDetailHeaderView(Context context) {
        super(context);
    }

    public PlaceDetailHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_header, this, true);
    }

    public void setPlace(PlaceModel place) {
        binding.placeName.setText(place.getName());
        binding.placeAddress.setText(place.getAddress());
    }

}