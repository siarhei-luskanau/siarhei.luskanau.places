package siarhei.luskanau.places;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;

import java.io.InputStream;

import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.presentation.internal.di.components.ApplicationComponent;
import siarhei.luskanau.places.presentation.internal.di.components.DaggerApplicationComponent;
import siarhei.luskanau.places.presentation.internal.di.modules.ApplicationModule;
import siarhei.luskanau.places.utils.glide.PlacePhotoId;
import siarhei.luskanau.places.utils.glide.PlacePhotoIdModelLoader;

public class AppApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();
        initializeLeakDetection();

        MapsGoogleApi.init(this);
        Glide.get(this).register(PlacePhotoId.class, InputStream.class, new PlacePhotoIdModelLoader.Factory());
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            if (!LeakCanary.isInAnalyzerProcess(this)) {
                LeakCanary.install(this);
            }
        }
    }

}