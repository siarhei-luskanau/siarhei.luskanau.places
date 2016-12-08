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
package siarhei.luskanau.places.presentation.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.model.PlaceModel;
import siarhei.luskanau.places.presentation.internal.di.PerActivity;

/**
 * Mapper class used to transform {@link Place} (in the domain layer) to {@link PlaceModel} in the
 * presentation layer.
 */
@PerActivity
public class PlaceModelDataMapper {

    @Inject
    public PlaceModelDataMapper() {
    }

    /**
     * Transform a {@link Place} into an {@link PlaceModel}.
     *
     * @param place Object to be transformed.
     * @return {@link PlaceModel}.
     */
    public PlaceModel transform(Place place) {
        if (place == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        PlaceModel placeModel = new PlaceModel(place.getId());
        placeModel.setName(place.getName());
        placeModel.setAddress(place.getAddress());
        placeModel.setPhoneNumber(place.getPhoneNumber());
        placeModel.setWebsiteUri(place.getWebsiteUri());
        placeModel.setLatitude(place.getLatitude());
        placeModel.setLongitude(place.getLongitude());

        return placeModel;
    }

    /**
     * Transform a Collection of {@link Place} into a Collection of {@link PlaceModel}.
     *
     * @param placesCollection Objects to be transformed.
     * @return List of {@link PlaceModel}.
     */
    public Collection<PlaceModel> transform(Collection<Place> placesCollection) {
        Collection<PlaceModel> placeModelsCollection;

        if (placesCollection != null && !placesCollection.isEmpty()) {
            placeModelsCollection = new ArrayList<>();
            for (Place place : placesCollection) {
                placeModelsCollection.add(transform(place));
            }
        } else {
            placeModelsCollection = Collections.emptyList();
        }

        return placeModelsCollection;
    }
}
