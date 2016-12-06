/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package siarhei.luskanau.places.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.MalformedURLException;
import java.util.List;

import rx.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.entity.mapper.PlaceEntityJsonMapper;
import siarhei.luskanau.places.data.exception.NetworkConnectionException;

/**
 * {@link RestApi} implementation for retrieving data from the network.
 */
public class RestApiImpl implements RestApi {

    private final Context context;
    private final PlaceEntityJsonMapper placeEntityJsonMapper;

    /**
     * Constructor of the class
     *
     * @param context               {@link android.content.Context}.
     * @param placeEntityJsonMapper {@link PlaceEntityJsonMapper}.
     */
    public RestApiImpl(Context context, PlaceEntityJsonMapper placeEntityJsonMapper) {
        if (context == null || placeEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.context = context.getApplicationContext();
        this.placeEntityJsonMapper = placeEntityJsonMapper;
    }

    @Override
    public Observable<List<PlaceEntity>> placeEntityList() {
        return Observable.create(subscriber -> {
            if (isThereInternetConnection()) {
                try {
                    String responsePlaceEntities = getPlaceEntitiesFromApi();
                    if (responsePlaceEntities != null) {
                        subscriber.onNext(placeEntityJsonMapper.transformPlaceEntityCollection(
                                responsePlaceEntities));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new NetworkConnectionException());
                    }
                } catch (Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                subscriber.onError(new NetworkConnectionException());
            }
        });
    }

    @Override
    public Observable<PlaceEntity> placeEntityById(final String placeId) {
        return Observable.create(subscriber -> {
            if (isThereInternetConnection()) {
                try {
                    String responsePlaceDetails = getPlaceDetailsFromApi(placeId);
                    if (responsePlaceDetails != null) {
                        subscriber.onNext(placeEntityJsonMapper.transformPlaceEntity(responsePlaceDetails));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new NetworkConnectionException());
                    }
                } catch (Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                subscriber.onError(new NetworkConnectionException());
            }
        });
    }

    private String getPlaceEntitiesFromApi() throws MalformedURLException {
        return ApiConnection.createGET(API_URL_GET_USER_LIST).requestSyncCall();
    }

    private String getPlaceDetailsFromApi(String placeId) throws MalformedURLException {
        String apiUrl = API_URL_GET_USER_DETAILS + placeId + ".json";
        return ApiConnection.createGET(apiUrl).requestSyncCall();
    }

    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

}