package siarhei.luskanau.places.utils.glide;

import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

public class PlacePhotoIdDataFetcher implements DataFetcher<InputStream> {

    private static final String TAG = "PlacePhotoIdDataFetcher";
    private PlacePhotoId model;

    public PlacePhotoIdDataFetcher(PlacePhotoId model) {
        this.model = model;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Log.d(TAG, getId());
        Bitmap bitmap = model.getGooglePlayServicesApi()
                .getPlacePhotoBitmap(model.getPlacePhotoMetadata(), 0, 0)
                .toBlocking().first();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public void cleanup() {
    }

    @Override
    public String getId() {
        return String.format(Locale.getDefault(), "%s_%d", model.getPlaceId(), model.getPosition());
    }

    @Override
    public void cancel() {
    }

}