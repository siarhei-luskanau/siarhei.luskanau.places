package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailPhoneBinding;

public class PlaceDetailPhoneView extends LinearLayout {

    private ViewPlaceDetailPhoneBinding binding;

    public PlaceDetailPhoneView(Context context) {
        super(context);
    }

    public PlaceDetailPhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailPhoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_phone, this, true);
    }

    public void setPhoneNumber(CharSequence phoneNumber) {
        binding.placePhone.setText(phoneNumber);
    }

}