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
package siarhei.luskanau.places.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.places.data.entity.PlaceEntity;

/**
 * Class used to transform from Strings representing json to valid objects.
 */
public class PlaceEntityJsonMapper {

    private final Gson gson;

    @Inject
    public PlaceEntityJsonMapper() {
        this.gson = new Gson();
    }

    /**
     * Transform from valid json string to {@link PlaceEntity}.
     *
     * @param userJsonResponse A json representing a user profile.
     * @return {@link PlaceEntity}.
     * @throws com.google.gson.JsonSyntaxException if the json string is not a valid json structure.
     */
    public PlaceEntity transformPlaceEntity(String userJsonResponse) throws JsonSyntaxException {
        try {
            Type userEntityType = new TypeToken<PlaceEntity>() {
            }.getType();
            PlaceEntity userEntity = this.gson.fromJson(userJsonResponse, userEntityType);

            return userEntity;
        } catch (JsonSyntaxException jsonException) {
            throw jsonException;
        }
    }

    /**
     * Transform from valid json string to List of {@link PlaceEntity}.
     *
     * @param userListJsonResponse A json representing a collection of users.
     * @return List of {@link PlaceEntity}.
     * @throws com.google.gson.JsonSyntaxException if the json string is not a valid json structure.
     */
    public List<PlaceEntity> transformPlaceEntityCollection(String userListJsonResponse)
            throws JsonSyntaxException {
        List<PlaceEntity> userEntityCollection;
        try {
            Type listOfPlaceEntityType = new TypeToken<List<PlaceEntity>>() {
            }.getType();
            userEntityCollection = this.gson.fromJson(userListJsonResponse, listOfPlaceEntityType);

            return userEntityCollection;
        } catch (JsonSyntaxException jsonException) {
            throw jsonException;
        }
    }

}