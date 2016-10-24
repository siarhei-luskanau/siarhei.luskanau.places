package siarhei.luskanau.places.api;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.util.Pair;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.List;

import rx.Observable;
import siarhei.luskanau.places.api.android.GooglePlayServicesApi;
import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.model.PlaceModel;

public class GoogleApi {

    private static final boolean IS_USE_WEB = true;

    private GooglePlayServicesApi googlePlayServicesApi;
    private MapsGoogleApi mapsGoogleApi;

    public GoogleApi(GoogleApiClient googleApiClient, MapsGoogleApi mapsGoogleApi) {
        this.googlePlayServicesApi = new GooglePlayServicesApi(googleApiClient);
        this.mapsGoogleApi = mapsGoogleApi;
    }

    public Observable<Location> getLastLocation() {
        return googlePlayServicesApi.getLastLocation();
    }

    public Observable<List<PlaceModel>> getPlaces(Location location) {
        if (IS_USE_WEB) {
            return WebApiAdapter.getPlaces(mapsGoogleApi, location);
        } else {
            return AndroidApiAdapter.getCurrentPlace(googlePlayServicesApi);
        }
    }

    public Observable<PlaceModel> getPlace(String placeId) {
        if (IS_USE_WEB) {
        } else {
        }
        return AndroidApiAdapter.getPlace(googlePlayServicesApi, placeId);
    }

    public Observable<List<PlacePhotoMetadata>> getPlacePhotos(String placeId) {
        return googlePlayServicesApi.getPlacePhotos(placeId);
    }

    public Observable<Bitmap> getPlacePhotoBitmap(PlacePhotoMetadata placePhotoMetadata, int width, int height) {
        return googlePlayServicesApi.getPlacePhotoBitmap(placePhotoMetadata, width, height);
    }

    public Observable<Pair<Place, List<PlacePhotoMetadata>>> getPlaceWithPhotos(String placeId) {
        return googlePlayServicesApi.getPlaceWithPhotos(placeId);
    }

}