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
package siarhei.luskanau.places.data.cache.serializer;

import com.google.gson.Gson;

import javax.inject.Inject;

import siarhei.luskanau.places.data.entity.PlaceEntity;

/**
 * Class place as Serializer/Deserializer for place entities.
 */
public class JsonSerializer {

    private final Gson gson = new Gson();

    @Inject
    public JsonSerializer() {
    }

    /**
     * Serialize an object to Json.
     *
     * @param placeEntity {@link PlaceEntity} to serialize.
     */
    public String serialize(PlaceEntity placeEntity) {
        String jsonString = gson.toJson(placeEntity, PlaceEntity.class);
        return jsonString;
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param jsonString A json string to deserialize.
     * @return {@link PlaceEntity}
     */
    public PlaceEntity deserialize(String jsonString) {
        PlaceEntity placeEntity = gson.fromJson(jsonString, PlaceEntity.class);
        return placeEntity;
    }
}
