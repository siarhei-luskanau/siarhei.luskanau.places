package siarhei.luskanau.places.utils.glide;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.api.PlacesApi;

public class PlacePhotoId {

    private Place place;
    private int position;
    private PlacePhotoMetadata placePhotoMetadata;
    private PlacesApi placesApi;

    public PlacePhotoId(Place place, int position, PlacePhotoMetadata placePhotoMetadata, PlacesApi placesApi) {
        this.place = place;
        this.position = position;
        this.placePhotoMetadata = placePhotoMetadata;
        this.placesApi = placesApi;
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

    public PlacesApi getPlacesApi() {
        return placesApi;
    }

}