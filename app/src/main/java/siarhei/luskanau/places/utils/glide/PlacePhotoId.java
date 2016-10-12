package siarhei.luskanau.places.utils.glide;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.api.RxGoogleApi;

public class PlacePhotoId {

    private Place place;
    private int position;
    private PlacePhotoMetadata placePhotoMetadata;
    private RxGoogleApi rxGoogleApi;

    public PlacePhotoId(Place place, int position, PlacePhotoMetadata placePhotoMetadata, RxGoogleApi rxGoogleApi) {
        this.place = place;
        this.position = position;
        this.placePhotoMetadata = placePhotoMetadata;
        this.rxGoogleApi = rxGoogleApi;
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

    public RxGoogleApi getRxGoogleApi() {
        return rxGoogleApi;
    }

}