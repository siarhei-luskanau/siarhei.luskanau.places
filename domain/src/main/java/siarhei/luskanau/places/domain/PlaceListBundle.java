package siarhei.luskanau.places.domain;

import java.util.List;

public class PlaceListBundle {

    private LatLng latLng;
    private List<Place> places;

    public PlaceListBundle(LatLng latLng, List<Place> places) {
        this.latLng = latLng;
        this.places = places;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
