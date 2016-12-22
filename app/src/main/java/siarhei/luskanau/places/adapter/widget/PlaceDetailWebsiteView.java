package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailWebsiteBinding;

public class PlaceDetailWebsiteView extends LinearLayout {

    private ViewPlaceDetailWebsiteBinding binding;

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_website, this, true);
    }

    public PlaceDetailWebsiteView(Context context) {
        super(context);
    }

    public PlaceDetailWebsiteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailWebsiteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUri(String uri) {
        binding.placeWebsite.setText(uri);
    }

}