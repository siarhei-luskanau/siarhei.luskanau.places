package siarhei.luskanau.places.api;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import siarhei.luskanau.places.api.web.MapsGoogleApi;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.domain.Photo;
import siarhei.luskanau.places.domain.Place;

public final class WebApiAdapter {

    private WebApiAdapter() {
    }

    public static Observable<List<Place>> getPlaces(final MapsGoogleApi mapsGoogleApi, Location location) {
        return mapsGoogleApi.getPlaces(location)
                .flatMap(new Func1<List<PlaceEntity>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(List<PlaceEntity> places) {
                        List<Observable<Place>> list = new ArrayList<>();
                        for (PlaceEntity place : places) {
                            list.add(getPlace(mapsGoogleApi, place.getPlaceId()));
                        }
                        return Observable.merge(list);
                    }
                })
                .toList();
    }

    public static Observable<Place> getPlace(final MapsGoogleApi mapsGoogleApi, String placeId) {
        return mapsGoogleApi.getPlaceDetails(placeId)
                .map(new Func1<PlaceEntity, Place>() {
                    @Override
                    public Place call(PlaceEntity place) {
                        return toPlace(mapsGoogleApi, place);
                    }
                });
    }

    private static Place toPlace(MapsGoogleApi mapsGoogleApi, PlaceEntity placeEntity) {
        Place placeModel = new Place(placeEntity.getPlaceId());
        placeModel.setName(placeEntity.getName());
        placeModel.setAddress(placeEntity.getVicinity());
        placeModel.setPhoneNumber(placeEntity.getInternationalPhoneNumber());
        placeModel.setWebsiteUri(placeEntity.getWebsite());
        if (placeEntity.getGeometry() != null && placeEntity.getGeometry().getLocation() != null) {
            placeModel.setLatitude(placeEntity.getGeometry().getLocation().getLat());
            placeModel.setLongitude(placeEntity.getGeometry().getLocation().getLng());
        }
        if (placeEntity.getPhotos() != null) {
            List<Photo> photos = new ArrayList<>();
            for (siarhei.luskanau.places.data.entity.Photo photo : placeEntity.getPhotos()) {
                photos.add(new Photo(mapsGoogleApi.getPlacePhotoUrl(photo)));
            }
            placeModel.setPhotos(photos);
        }
        return placeModel;
    }
}
