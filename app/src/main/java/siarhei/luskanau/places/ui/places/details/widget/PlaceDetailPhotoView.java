package siarhei.luskanau.places.ui.places.details.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.Locale;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.databinding.ViewPlaceDetailPhotoBinding;
import siarhei.luskanau.places.rx.SimpleObserver;

public class PlaceDetailPhotoView extends LinearLayout {

    private ViewPlaceDetailPhotoBinding binding;
    private Subscription subscription;

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

    public void setPlacePhotoMetadata(int position, PlacePhotoMetadata placePhotoMetadata, PlacesApi placesApi) {
        binding.tag.setText(String.format(Locale.getDefault(), "%d %s",
                position, placePhotoMetadata.getAttributions()));
        binding.placePhoto.setImageBitmap(null);

        releaseSubscription(subscription);
        subscription = placesApi.getPlacePhotoBitmap(placePhotoMetadata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        binding.placePhoto.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSubscription(subscription);
    }

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}