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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.domain.Place;

/**
 * Mapper class used to transform {@link PlaceEntity} (in the data layer) to {@link Place} in the
 * domain layer.
 */
@Singleton
public class PlaceEntityDataMapper {

    @Inject
    public PlaceEntityDataMapper() {
    }

    /**
     * Transform a {@link PlaceEntity} into an {@link Place}.
     *
     * @param placeEntity Object to be transformed.
     * @return {@link Place} if valid {@link PlaceEntity} otherwise null.
     */
    public Place transform(PlaceEntity placeEntity) {
        Place place = null;
        if (placeEntity != null) {
            place = new Place(placeEntity.getPlaceId());
            place.setName(placeEntity.getName());
            place.setAddress(placeEntity.getVicinity());
            place.setPhoneNumber(placeEntity.getInternationalPhoneNumber());
            place.setWebsiteUri(placeEntity.getWebsite());
            if (placeEntity.getGeometry() != null && placeEntity.getGeometry().getLocation() != null) {
                place.setLatitude(placeEntity.getGeometry().getLocation().getLat());
                place.setLongitude(placeEntity.getGeometry().getLocation().getLng());
            }
        }

        return place;
    }

    /**
     * Transform a List of {@link PlaceEntity} into a Collection of {@link Place}.
     *
     * @param userEntityCollection Object Collection to be transformed.
     * @return {@link Place} if valid {@link PlaceEntity} otherwise null.
     */
    public List<Place> transform(Collection<PlaceEntity> userEntityCollection) {
        List<Place> userList = new ArrayList<>(20);
        Place user;
        for (PlaceEntity userEntity : userEntityCollection) {
            user = transform(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }

        return userList;
    }

}