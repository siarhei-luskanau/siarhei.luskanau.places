package siarhei.luskanau.places.utils.glide;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.api.PlacesApi;

public class PlacePhotoId {

    private Place place;
    private PlacePhotoMetadata placePhotoMetadata;
    private PlacesApi placesApi;

    public PlacePhotoId(Place place, PlacePhotoMetadata placePhotoMetadata, PlacesApi placesApi) {
        this.place = place;
        this.placePhotoMetadata = placePhotoMetadata;
        this.placesApi = placesApi;
    }

    public Place getPlace() {
        return place;
    }

    public PlacePhotoMetadata getPlacePhotoMetadata() {
        return placePhotoMetadata;
    }

    public PlacesApi getPlacesApi() {
        return placesApi;
    }

}