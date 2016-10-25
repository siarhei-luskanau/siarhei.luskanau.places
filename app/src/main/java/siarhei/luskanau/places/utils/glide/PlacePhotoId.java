package siarhei.luskanau.places.utils.glide;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.api.android.GooglePlayServicesApi;

public class PlacePhotoId {

    private Place place;
    private int position;
    private PlacePhotoMetadata placePhotoMetadata;
    private GooglePlayServicesApi googlePlayServicesApi;

    public PlacePhotoId(Place place, int position, PlacePhotoMetadata placePhotoMetadata,
                        GooglePlayServicesApi googlePlayServicesApi) {
        this.place = place;
        this.position = position;
        this.placePhotoMetadata = placePhotoMetadata;
        this.googlePlayServicesApi = googlePlayServicesApi;
    }

    public Place getPlace() {
        return place;
    }

    public int getPosition() {
        return position;
    }

    public PlacePhotoMetadata getPlacePhotoMetadata() {
        return placePhotoMetadata;
    }

    public GooglePlayServicesApi getGooglePlayServicesApi() {
        return googlePlayServicesApi;
    }

}