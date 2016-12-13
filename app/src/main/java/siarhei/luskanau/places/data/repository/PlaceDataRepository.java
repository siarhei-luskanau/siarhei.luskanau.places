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
package siarhei.luskanau.places.data.repository;

import android.location.Location;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import siarhei.luskanau.places.data.entity.mapper.PlaceEntityDataMapper;
import siarhei.luskanau.places.data.repository.datasource.PlaceDataStore;
import siarhei.luskanau.places.data.repository.datasource.PlaceDataStoreFactory;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

/**
 * {@link PlaceRepository} for retrieving place data.
 */
public class PlaceDataRepository implements PlaceRepository {

    private final PlaceDataStoreFactory placeDataStoreFactory;
    private final PlaceEntityDataMapper placeEntityDataMapper;

    /**
     * Constructs a {@link PlaceRepository}.
     *
     * @param dataStoreFactory A factory to construct different data source implementations.
     * @param placeEntityDataMapper {@link PlaceEntityDataMapper}.
     */
    @Inject
    public PlaceDataRepository(PlaceDataStoreFactory dataStoreFactory,
                               PlaceEntityDataMapper placeEntityDataMapper) {
        this.placeDataStoreFactory = dataStoreFactory;
        this.placeEntityDataMapper = placeEntityDataMapper;
    }

    @Override
    public Observable<List<Place>> places(Location location) {
        //we always get all places from the cloud
        final PlaceDataStore placeDataStore = this.placeDataStoreFactory.createCloudDataStore();
        return placeDataStore.placeEntityList(location).map(this.placeEntityDataMapper::transform);
    }

    @Override
    public Observable<Place> place(String placeId) {
        final PlaceDataStore placeDataStore = this.placeDataStoreFactory.create(placeId);
        return placeDataStore.placeEntityDetails(placeId).map(this.placeEntityDataMapper::transform);
    }
}
