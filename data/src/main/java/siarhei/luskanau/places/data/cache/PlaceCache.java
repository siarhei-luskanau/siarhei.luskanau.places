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
package siarhei.luskanau.places.data.cache;

import io.reactivex.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;

/**
 * An interface representing a place Cache.
 */
public interface PlaceCache {
    /**
     * Gets an {@link Observable} which will emit a {@link String}.
     *
     * @param placeId The place id to retrieve data.
     */
    Observable<PlaceEntity> get(final String placeId);

    /**
     * Puts and element into the cache.
     *
     * @param placeEntity Element to insert in the cache.
     */
    void put(PlaceEntity placeEntity);

    /**
     * Checks if an element (Place) exists in the cache.
     *
     * @param placeId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final String placeId);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}
