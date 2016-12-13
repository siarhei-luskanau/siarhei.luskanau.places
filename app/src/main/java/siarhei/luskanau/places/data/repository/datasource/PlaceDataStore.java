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

import android.location.Location;

import java.util.List;

import rx.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface PlaceDataStore {
    /**
     * Get an {@link rx.Observable} which will emit a List of {@link PlaceEntity}.
     */
    Observable<List<PlaceEntity>> placeEntityList(Location location);

    /**
     * Get an {@link rx.Observable} which will emit a {@link PlaceEntity} by its id.
     *
     * @param placeId The id to retrieve place data.
     */
    Observable<PlaceEntity> placeEntityDetails(final String placeId);

}