package siarhei.luskanau.places.utils.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

public class PlacePhotoIdDataFetcher implements DataFetcher<InputStream> {

    private PlacePhotoId model;
    private int scaledWidth;
    private int scaledHeight;

    public PlacePhotoIdDataFetcher(PlacePhotoId model, int width, int height) {
        this.model = model;

        double ratio = Math.min(model.getPlacePhotoMetadata().getMaxWidth() / width,
                model.getPlacePhotoMetadata().getMaxHeight() / height);

        if (ratio > 1) {
            scaledWidth = (int) (width / ratio);
            scaledHeight = (int) (height / ratio);
        } else {
            scaledWidth = width;
            scaledHeight = height;
        }
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Bitmap bitmap = model.getPlacesApi()
                .getPlacePhotoBitmap(model.getPlacePhotoMetadata(), scaledWidth, scaledHeight)
                .toBlocking().first();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public void cleanup() {
    }

    @Override
    public String getId() {
        Place place = model.getPlace();
        PlacePhotoMetadata placePhotoMetadata = model.getPlacePhotoMetadata();
        return String.format(Locale.getDefault(), "%s_w%d_h%d_%s_w%d_h%d",
                place.getId(), placePhotoMetadata.getMaxWidth(), placePhotoMetadata.getMaxHeight(),
                placePhotoMetadata.getAttributions(), scaledWidth, scaledHeight);
    }

    @Override
    public void cancel() {
    }

}