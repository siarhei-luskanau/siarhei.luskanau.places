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
package siarhei.luskanau.places.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import siarhei.luskanau.places.data.cache.PlaceCache;
import siarhei.luskanau.places.data.entity.mapper.PlaceEntityJsonMapper;
import siarhei.luskanau.places.data.net.RestApi;
import siarhei.luskanau.places.data.net.RestApiImpl;

/**
 * Factory that creates different implementations of {@link PlaceDataStore}.
 */
@Singleton
public class PlaceDataStoreFactory {

    private final Context context;
    private final PlaceCache placeCache;

    @Inject
    public PlaceDataStoreFactory(@NonNull Context context, @NonNull PlaceCache placeCache) {
        this.context = context.getApplicationContext();
        this.placeCache = placeCache;
    }

    /**
     * Create {@link PlaceDataStore} from a place id.
     */
    public PlaceDataStore create(String placeId) {
        PlaceDataStore placeDataStore;

        if (!this.placeCache.isExpired() && this.placeCache.isCached(placeId)) {
            placeDataStore = new DiskPlaceDataStore(this.placeCache);
        } else {
            placeDataStore = createCloudDataStore();
        }

        return placeDataStore;
    }

    /**
     * Create {@link PlaceDataStore} to retrieve data from the Cloud.
     */
    public PlaceDataStore createCloudDataStore() {
        PlaceEntityJsonMapper placeEntityJsonMapper = new PlaceEntityJsonMapper();
        RestApi restApi = new RestApiImpl(this.context, placeEntityJsonMapper);

        return new CloudPlaceDataStore(restApi, this.placeCache);
    }

}