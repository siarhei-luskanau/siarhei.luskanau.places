package siarhei.luskanau.places.api;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import rx.Observable;
import siarhei.luskanau.places.api.android.GooglePlayServicesApi;
import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.domain.Place;

public class GoogleApi {

    private GooglePlayServicesApi googlePlayServicesApi;
    private MapsGoogleApi mapsGoogleApi;

    public GoogleApi(GoogleApiClient googleApiClient, MapsGoogleApi mapsGoogleApi) {
        this.googlePlayServicesApi = new GooglePlayServicesApi(googleApiClient);
        this.mapsGoogleApi = mapsGoogleApi;
    }

    public Observable<Location> getLastLocation() {
        return googlePlayServicesApi.getLastLocation();
    }

    public Observable<List<Place>> getPlaces(Location location) {
        return WebApiAdapter.getPlaces(mapsGoogleApi, location);

    }

    public Observable<Place> getPlace(String placeId) {
        return WebApiAdapter.getPlace(mapsGoogleApi, placeId);
    }
}
