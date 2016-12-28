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

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.exception.NetworkConnectionException;
import siarhei.luskanau.places.data.net.retrofit.MapsGoogleApi;
import siarhei.luskanau.places.domain.LatLng;

/**
 * {@link RestApi} implementation for retrieving data from the network.
 */
public class RestApiImpl implements RestApi {

    private final Context context;
    private final MapsGoogleApi mapsGoogleApi;

    /**
     * Constructor of the class
     *
     * @param context       {@link android.content.Context}.
     * @param mapsGoogleApi {@link MapsGoogleApi}.
     */
    public RestApiImpl(Context context, MapsGoogleApi mapsGoogleApi) {
        if (context == null || mapsGoogleApi == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.context = context.getApplicationContext();
        this.mapsGoogleApi = mapsGoogleApi;
    }

    @Override
    public Observable<List<PlaceEntity>> placeEntityList(LatLng location) {
        return isThereInternetConnection()
                ? mapsGoogleApi.getPlaces(location)
                : Observable.create(subscriber -> subscriber.onError(new NetworkConnectionException()));
    }

    @Override
    public Observable<PlaceEntity> placeEntityById(final String placeId) {
        return isThereInternetConnection()
                ? mapsGoogleApi.getPlaceDetails(placeId)
                : Observable.create(subscriber -> subscriber.onError(new NetworkConnectionException()));
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
