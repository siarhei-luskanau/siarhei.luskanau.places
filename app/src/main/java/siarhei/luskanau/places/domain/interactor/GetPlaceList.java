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
package siarhei.luskanau.places.domain.interactor;

import android.location.Location;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link siarhei.luskanau.places.domain.Place}.
 */
public class GetPlaceList extends UseCase {

    private static final String TAG = "GetPlaceList";
    private static final int DISTANCE_IN_METERS = 100;
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;
    private Location lastLocation;

    @Inject
    public GetPlaceList(LocationRepository locationRepository, PlaceRepository placeRepository,
                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public Observable buildUseCaseObservable() {
        lastLocation = null;
        return this.locationRepository.location()
                .onErrorReturn(throwable -> {
                    Log.e(TAG, throwable.getMessage(), throwable);
                    return null;
                })
                .flatMap(location -> {
                    if (location != null) {
                        if (lastLocation == null || lastLocation.distanceTo(location) >= DISTANCE_IN_METERS) {
                            lastLocation = location;
                            return this.placeRepository.places(location)
                                    .flatMap(places -> Observable.just(new Pair<>(location, places)));
                        }
                    }
                    return Observable.just(new Pair<>(null, Collections.EMPTY_LIST));
                });
    }
}
