package siarhei.luskanau.places.utils;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class RoundedBitmapImageViewTarget extends BitmapImageViewTarget {

    public RoundedBitmapImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable roundedBitmapDrawable
                = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
        roundedBitmapDrawable.setCircular(true);
        view.setImageDrawable(roundedBitmapDrawable);
    }

}