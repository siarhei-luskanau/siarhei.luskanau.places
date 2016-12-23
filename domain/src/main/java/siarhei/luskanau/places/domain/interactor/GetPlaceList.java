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

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.PlaceListBundle;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link siarhei.luskanau.places.domain.Place}.
 */
public class GetPlaceList extends UseCase<PlaceListBundle> {

    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;

    @Inject
    public GetPlaceList(LocationRepository locationRepository, PlaceRepository placeRepository,
                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public Observable<PlaceListBundle> buildUseCaseObservable() {
        return this.locationRepository.location()
                .flatMap(new Func1<LatLng, Observable<PlaceListBundle>>() {
                             @Override
                             public Observable<PlaceListBundle> call(final LatLng latLng) {
                                 if (latLng != null) {
                                     return placeRepository.places(latLng)
                                             .onErrorReturn(new Func1<Throwable, List<Place>>() {
                                                 @Override
                                                 public List<Place> call(Throwable throwable) {
                                                     return Collections.emptyList();
                                                 }
                                             })
                                             .flatMap(new Func1<List<Place>, Observable<PlaceListBundle>>() {
                                                 @Override
                                                 public Observable<PlaceListBundle> call(List<Place> places) {
                                                     return Observable.just(new PlaceListBundle(latLng, places));
                                                 }
                                             });
                                 } else {
                                     return Observable.just(new PlaceListBundle(null, Collections.<Place>emptyList()));
                                 }
                             }
                         }
                );
    }
}
