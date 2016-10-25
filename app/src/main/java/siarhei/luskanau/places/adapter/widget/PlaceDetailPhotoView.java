package siarhei.luskanau.places.adapter.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.databinding.ViewPlaceDetailPhotoBinding;
import siarhei.luskanau.places.model.PhotoModel;

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

    public void setPhotoModel(PhotoModel photoModel) {
        if (photoModel != null) {
            DrawableTypeRequest drawableTypeRequest = photoModel.getPlacePhotoId() != null
                    ? Glide.with(getContext()).load(photoModel.getPlacePhotoId())
                    : Glide.with(getContext()).load(photoModel.getPhotoUrl());
            drawableTypeRequest.fitCenter().placeholder(null).into(binding.placePhoto);
        }
    }

}