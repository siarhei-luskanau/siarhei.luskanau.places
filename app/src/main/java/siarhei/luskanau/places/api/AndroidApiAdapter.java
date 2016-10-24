package siarhei.luskanau.places.api;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import siarhei.luskanau.places.api.android.GooglePlayServicesApi;
import siarhei.luskanau.places.model.PlaceModel;

public final class AndroidApiAdapter {

    private AndroidApiAdapter() {
    }

    public static Observable<List<PlaceModel>> getCurrentPlace(GooglePlayServicesApi googlePlayServicesApi) {
        return googlePlayServicesApi.getCurrentPlace()
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

    public static Observable<PlaceModel> getPlace(GooglePlayServicesApi googlePlayServicesApi, String placeId) {
        return googlePlayServicesApi.getPlace(placeId)
                .map(new Func1<Place, PlaceModel>() {
                    @Override
                    public PlaceModel call(Place place) {
                        return toPlaceModel(place);
                    }
                });
    }

    private static PlaceModel toPlaceModel(Place place) {
        PlaceModel placeModel = new PlaceModel();
        placeModel.setId(place.getId());
        placeModel.setName(place.getName());
        placeModel.setAddress(place.getAddress());
        placeModel.setPhoneNumber(place.getPhoneNumber());
        if (place.getWebsiteUri() != null) {
            placeModel.setWebsiteUri(place.getWebsiteUri().toString());
        }
        if (place.getLatLng() != null) {
            placeModel.setLatitude(place.getLatLng().latitude);
            placeModel.setLongitude(place.getLatLng().longitude);
        }
        return placeModel;
    }

}