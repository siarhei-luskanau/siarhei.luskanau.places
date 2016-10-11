package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.Place;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceBinding;

public class PlaceView extends LinearLayout {

    private ViewPlaceBinding binding;
    private int whiteColor;
    private int grayColor;

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

        whiteColor = ContextCompat.getColor(getContext(), R.color.app_white);
        grayColor = ContextCompat.getColor(getContext(), R.color.app_gray);
    }

    public void setPlace(Place place, boolean isSelected) {
        binding.placeName.setText(place.getName());
        binding.placeAddress.setText(place.getAddress());
        binding.placePhone.setVisibility(TextUtils.isEmpty(place.getPhoneNumber()) ? GONE : VISIBLE);
        binding.placeWebsite.setVisibility(place.getWebsiteUri() == null ? GONE : VISIBLE);
        binding.selectedContainer.setBackgroundColor(isSelected ? grayColor : whiteColor);
    }

}