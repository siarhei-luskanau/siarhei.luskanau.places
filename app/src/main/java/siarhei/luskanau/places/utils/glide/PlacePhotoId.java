package siarhei.luskanau.places.utils.glide;

import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.data.gps.GooglePlayServicesApi;

public class PlacePhotoId {

    private String placeId;
    private int position;
    private PlacePhotoMetadata placePhotoMetadata;
    private GooglePlayServicesApi googlePlayServicesApi;

    public PlacePhotoId(String placeId, int position, PlacePhotoMetadata placePhotoMetadata,
                        GooglePlayServicesApi googlePlayServicesApi) {
        this.placeId = placeId;
        this.position = position;
        this.placePhotoMetadata = placePhotoMetadata;
        this.googlePlayServicesApi = googlePlayServicesApi;
    }

    public String getPlaceId() {
        return placeId;
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