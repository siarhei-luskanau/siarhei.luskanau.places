package siarhei.luskanau.places.api;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.api.web.model.Place;
import siarhei.luskanau.places.model.PlaceModel;

public final class WebApiAdapter {

    private WebApiAdapter() {
    }

    public static Observable<List<PlaceModel>> getPlaces(MapsGoogleApi mapsGoogleApi, Location location) {
        return mapsGoogleApi.getPlaces(location)
                .map(new Func1<List<Place>, List<PlaceModel>>() {
                    @Override
                    public List<PlaceModel> call(List<Place> places) {
                        List<PlaceModel> list = new ArrayList<>();
                        for (Place place : places) {
                            list.add(toPlaceModel(place));
                        }
                        return list;
                    }
                });
    }

    public static Observable<PlaceModel> getPlace(MapsGoogleApi mapsGoogleApi, String placeId) {
        return mapsGoogleApi.getPlaceDetails(placeId)
                .map(new Func1<Place, PlaceModel>() {
                    @Override
                    public PlaceModel call(Place place) {
                        return toPlaceModel(place);
                    }
                });
    }

    private static PlaceModel toPlaceModel(Place place) {
        PlaceModel placeModel = new PlaceModel();
        placeModel.setId(place.getPlaceId());
        placeModel.setName(place.getName());
        placeModel.setAddress(place.getVicinity());
        if (place.getGeometry() != null && place.getGeometry().getLocation() != null) {
            placeModel.setLatitude(place.getGeometry().getLocation().getLat());
            placeModel.setLongitude(place.getGeometry().getLocation().getLng());
        }
        return placeModel;
    }

}