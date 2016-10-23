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

    private static PlaceModel toPlaceModel(Place place) {
        PlaceModel placeModel = new PlaceModel();
        placeModel.setId(place.placeId);
        placeModel.setName(place.name);
        placeModel.setAddress(place.vicinity);
//        placeModel.setPhoneNumber(place.getPhoneNumber());
//        if (place.getWebsiteUri() != null) {
//            placeModel.setWebsiteUri(place.getWebsiteUri().toString());
//        }
        if (place.geometry != null & place.geometry.location != null) {
            placeModel.setLatitude(place.geometry.location.lat);
            placeModel.setLongitude(place.geometry.location.lng);
        }
        return placeModel;
    }

}