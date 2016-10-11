package siarhei.luskanau.places;

import android.app.Application;

import com.bumptech.glide.Glide;

import java.io.InputStream;

import siarhei.luskanau.places.utils.glide.PlacePhotoId;
import siarhei.luskanau.places.utils.glide.PlacePhotoIdModelLoader;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Glide.get(this).register(PlacePhotoId.class, InputStream.class, new PlacePhotoIdModelLoader.Factory());
    }

}