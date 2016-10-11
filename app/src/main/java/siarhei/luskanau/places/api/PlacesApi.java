package siarhei.luskanau.places.api;

import android.graphics.Bitmap;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.List;

import rx.Observable;
import rx.functions.Func0;

public class PlacesApi {

    private GoogleApiClient googleApiClient;

    public PlacesApi(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public Observable<List<AutocompletePrediction>> getAutocompletePredictions(final String query) {
        return Observable.defer(new Func0<Observable<List<AutocompletePrediction>>>() {
            @Override
            public Observable<List<AutocompletePrediction>> call() {
                PendingResult<AutocompletePredictionBuffer> pendingResult
                        = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, null, null);
                AutocompletePredictionBuffer autocompletePredictionBuffer = pendingResult.await();

                Status status = autocompletePredictionBuffer.getStatus();
                if (!status.isSuccess()) {
                    throw new RuntimeException(status.getStatusMessage());
                }

                List<AutocompletePrediction> autocompletePredictions
                        = DataBufferUtils.freezeAndClose(autocompletePredictionBuffer);
                return Observable.just(autocompletePredictions);
            }
        });
    }

    public Observable<Place> getPlace(final String placeId) {
        return Observable.defer(new Func0<Observable<Place>>() {
            @Override
            public Observable<Place> call() {
                PendingResult<PlaceBuffer> pendingResult
                        = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
                PlaceBuffer placeBuffer = pendingResult.await();

                Status status = placeBuffer.getStatus();
                if (!status.isSuccess()) {
                    throw new RuntimeException(status.getStatusMessage());
                }

                List<Place> places = DataBufferUtils.freezeAndClose(placeBuffer);
                return Observable.from(places);
            }
        });
    }

    public Observable<List<PlacePhotoMetadata>> getPlacePhotos(final String placeId) {
        return Observable.defer(new Func0<Observable<List<PlacePhotoMetadata>>>() {
            @Override
            public Observable<List<PlacePhotoMetadata>> call() {
                PendingResult<PlacePhotoMetadataResult> pendingResult
                        = Places.GeoDataApi.getPlacePhotos(googleApiClient, placeId);
                PlacePhotoMetadataResult placePhotoMetadataResult = pendingResult.await();

                Status status = placePhotoMetadataResult.getStatus();
                if (!status.isSuccess()) {
                    throw new RuntimeException(status.getStatusMessage());
                }

                List<PlacePhotoMetadata> placePhotoMetadataList
                        = DataBufferUtils.freezeAndClose(placePhotoMetadataResult.getPhotoMetadata());
                return Observable.just(placePhotoMetadataList);
            }
        });
    }

    public Observable<Bitmap> getPlacePhotoBitmap(final PlacePhotoMetadata placePhotoMetadata,
                                                  final int width, final int height) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                PendingResult<PlacePhotoResult> pendingResult = placePhotoMetadata
                        .getScaledPhoto(googleApiClient, width, height);
                PlacePhotoResult placePhotoResult = pendingResult.await();

                Status status = placePhotoResult.getStatus();
                if (!status.isSuccess()) {
                    throw new RuntimeException(status.getStatusMessage());
                }

                return Observable.just(placePhotoResult.getBitmap());
            }
        });
    }

}