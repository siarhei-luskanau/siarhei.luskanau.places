package siarhei.luskanau.places.api;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import siarhei.luskanau.places.api.android.GooglePlayServicesApi;
import siarhei.luskanau.places.model.PhotoModel;
import siarhei.luskanau.places.model.PlaceModel;
import siarhei.luskanau.places.utils.glide.PlacePhotoId;

public final class AndroidApiAdapter {

    private AndroidApiAdapter() {
    }

    public static Observable<List<PlaceModel>> getCurrentPlace(final GooglePlayServicesApi googlePlayServicesApi) {
        return googlePlayServicesApi.getCurrentPlace()
                .flatMap(new Func1<List<Place>, Observable<PlaceModel>>() {
                    @Override
                    public Observable<PlaceModel> call(List<Place> places) {
                        List<Observable<PlaceModel>> list = new ArrayList<>();
                        for (Place place : places) {
                            PlaceModel placeModel = toPlaceModel(place);
                            list.add(withPhotos(googlePlayServicesApi, placeModel));
                        }
                        return Observable.merge(list);
                    }
                })
                .toList();
    }

    public static Observable<PlaceModel> getPlace(final GooglePlayServicesApi googlePlayServicesApi, String placeId) {
        return googlePlayServicesApi.getPlace(placeId)
                .map(new Func1<Place, PlaceModel>() {
                    @Override
                    public PlaceModel call(Place place) {
                        return toPlaceModel(place);
                    }
                })
                .flatMap(new Func1<PlaceModel, Observable<PlaceModel>>() {
                    @Override
                    public Observable<PlaceModel> call(PlaceModel placeModel) {
                        return withPhotos(googlePlayServicesApi, placeModel);
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

    private static Observable<PlaceModel> withPhotos(final GooglePlayServicesApi googlePlayServicesApi,
                                                     final PlaceModel placeModel) {
        return Observable.just(placeModel)
                .zipWith(googlePlayServicesApi.getPlacePhotos(placeModel.getId()),
                        new Func2<PlaceModel, List<PlacePhotoMetadata>, PlaceModel>() {
                            @Override
                            public PlaceModel call(PlaceModel placeModel, List<PlacePhotoMetadata> placePhotoMetadatas) {
                                if (placePhotoMetadatas != null) {
                                    List<PhotoModel> photos = new ArrayList<>();
                                    for (int i = 0; i < placePhotoMetadatas.size(); i++) {
                                        PlacePhotoMetadata placePhotoMetadata = placePhotoMetadatas.get(i);
                                        PlacePhotoId placePhotoId = new PlacePhotoId(placeModel.getId(), i,
                                                placePhotoMetadata, googlePlayServicesApi);
                                        photos.add(new PhotoModel(placePhotoId));
                                    }
                                    placeModel.setPhotos(photos);
                                }
                                return placeModel;
                            }
                        });
    }

}