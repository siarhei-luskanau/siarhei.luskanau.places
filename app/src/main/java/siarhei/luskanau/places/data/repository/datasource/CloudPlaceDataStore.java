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

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import siarhei.luskanau.places.data.cache.PlaceCache;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.net.RestApi;

/**
 * {@link PlaceDataStore} implementation based on connections to the api (Cloud).
 */
class CloudPlaceDataStore implements PlaceDataStore {

    private final RestApi restApi;
    private final PlaceCache placeCache;

    private final Action1<PlaceEntity> saveToCacheAction = placeEntity -> {
        if (placeEntity != null) {
            CloudPlaceDataStore.this.placeCache.put(placeEntity);
        }
    };

    /**
     * Construct a {@link PlaceDataStore} based on connections to the api (Cloud).
     *
     * @param restApi The {@link RestApi} implementation to use.
     * @param placeCache A {@link PlaceCache} to cache data retrieved from the api.
     */
    CloudPlaceDataStore(RestApi restApi, PlaceCache placeCache) {
        this.restApi = restApi;
        this.placeCache = placeCache;
    }

    @Override
    public Observable<List<PlaceEntity>> placeEntityList() {
        return this.restApi.placeEntityList();
    }

    @Override
    public Observable<PlaceEntity> placeEntityDetails(final String placeId) {
        return this.restApi.placeEntityById(placeId).doOnNext(saveToCacheAction);
    }

}