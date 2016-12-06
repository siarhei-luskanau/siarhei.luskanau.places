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

import java.util.List;

import rx.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;

/**
 * RestApi for retrieving data from the network.
 */
public interface RestApi {

    String API_BASE_URL = "http://www.android10.org/myapi/";

    /**
     * Api url for getting all places
     */
    String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    /**
     * Api url for getting a place profile: Remember to concatenate id + 'json'
     */
    String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    /**
     * Retrieves an {@link rx.Observable} which will emit a List of {@link PlaceEntity}.
     */
    Observable<List<PlaceEntity>> placeEntityList();

    /**
     * Retrieves an {@link rx.Observable} which will emit a {@link PlaceEntity}.
     *
     * @param placeId The place id used to get place data.
     */
    Observable<PlaceEntity> placeEntityById(final String placeId);

}