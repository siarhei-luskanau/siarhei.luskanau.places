package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailPhotoBinding;
import siarhei.luskanau.places.domain.Photo;

public class PlaceDetailPhotoView extends LinearLayout {

    private ViewPlaceDetailPhotoBinding binding;

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_place_detail_photo, this, true);
    }

    public PlaceDetailPhotoView(Context context) {
        super(context);
    }

    public PlaceDetailPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceDetailPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPhotoModel(Photo photo) {
        if (photo != null) {
            Glide.with(getContext()).load(photo.getPhotoUrl()).fitCenter().placeholder(null).into(binding.placePhoto);
        }
    }
}
