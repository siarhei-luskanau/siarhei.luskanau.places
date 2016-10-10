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
        int maxWidth = model.getPlacePhotoMetadata().getMaxWidth();
        int maxHeight = model.getPlacePhotoMetadata().getMaxHeight();
        double ratio = (double) model.getPlacePhotoMetadata().getMaxWidth() / width;
        int scale = 1;
        while (true) {
            if (scale * 2 > ratio) {
                break;
            }
            scale *= 2;
        }
        if (scale > 1) {
            scaledWidth = maxWidth / scale;
            scaledHeight = maxHeight / scale;
        } else {
            scaledWidth = maxWidth;
            scaledHeight = maxHeight;
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