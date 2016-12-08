package siarhei.luskanau.places.api;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.data.entity.Photo;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.model.PhotoModel;
import siarhei.luskanau.places.model.PlaceModel;

public final class WebApiAdapter {

    private WebApiAdapter() {
    }

    public static Observable<List<PlaceModel>> getPlaces(final MapsGoogleApi mapsGoogleApi, Location location) {
        return mapsGoogleApi.getPlaces(location)
                .flatMap(new Func1<List<PlaceEntity>, Observable<PlaceModel>>() {
                    @Override
                    public Observable<PlaceModel> call(List<PlaceEntity> places) {
                        List<Observable<PlaceModel>> list = new ArrayList<>();
                        for (PlaceEntity place : places) {
                            list.add(getPlace(mapsGoogleApi, place.getPlaceId()));
                        }
                        return Observable.merge(list);
                    }
                })
                .toList();
    }

    public static Observable<PlaceModel> getPlace(final MapsGoogleApi mapsGoogleApi, String placeId) {
        return mapsGoogleApi.getPlaceDetails(placeId)
                .map(new Func1<PlaceEntity, PlaceModel>() {
                    @Override
                    public PlaceModel call(PlaceEntity place) {
                        return toPlaceModel(mapsGoogleApi, place);
                    }
                });
    }

    private static PlaceModel toPlaceModel(MapsGoogleApi mapsGoogleApi, PlaceEntity place) {
        PlaceModel placeModel = new PlaceModel(place.getPlaceId());
        placeModel.setName(place.getName());
        placeModel.setAddress(place.getVicinity());
        placeModel.setPhoneNumber(place.getInternationalPhoneNumber());
        placeModel.setWebsiteUri(place.getWebsite());
        if (place.getGeometry() != null && place.getGeometry().getLocation() != null) {
            placeModel.setLatitude(place.getGeometry().getLocation().getLat());
            placeModel.setLongitude(place.getGeometry().getLocation().getLng());
        }
        if (place.getPhotos() != null) {
            List<PhotoModel> photos = new ArrayList<>();
            for (Photo photo : place.getPhotos()) {
                photos.add(new PhotoModel(mapsGoogleApi.getPlacePhotoUrl(photo)));
            }
            placeModel.setPhotos(photos);
        }
        return placeModel;
    }

}