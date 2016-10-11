package siarhei.luskanau.places.api;

import android.graphics.Bitmap;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

public class PlacesApi {

    private GoogleApiClient googleApiClient;

    public PlacesApi(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public Observable<List<Place>> getCurrentPlace() {
        return Observable.defer(new Func0<Observable<List<Place>>>() {
            @Override
            public Observable<List<Place>> call() {
                PendingResult<PlaceLikelihoodBuffer> pendingResult
                        = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
                PlaceLikelihoodBuffer autocompletePredictionBuffer = pendingResult.await();

                Status status = autocompletePredictionBuffer.getStatus();
                if (!status.isSuccess()) {
                    throw new RuntimeException(status.getStatusMessage());
                }

                List<Place> list = new ArrayList<>();
                for (PlaceLikelihood placeLikelihood : DataBufferUtils.freezeAndClose(autocompletePredictionBuffer)) {
                    list.add(placeLikelihood.getPlace());
                }
                return Observable.just(list);
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

                List<Place> list = DataBufferUtils.freezeAndClose(placeBuffer);
                return Observable.from(list);
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

                List<PlacePhotoMetadata> list
                        = DataBufferUtils.freezeAndClose(placePhotoMetadataResult.getPhotoMetadata());
                return Observable.just(list);
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