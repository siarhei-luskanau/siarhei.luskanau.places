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

import io.reactivex.Observable;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to an specific {@link siarhei.luskanau.places.domain.Place}.
 */
public class GetPlaceDetails extends UseCase<Place, GetPlaceDetails.Params> {

    private final PlaceRepository placeRepository;

    public GetPlaceDetails(PlaceRepository placeRepository,
                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }

    @Override
    protected Observable<Place> buildUseCaseObservable(Params params) {
        return this.placeRepository.place(params.placeId);
    }

    public static final class Params {

        private String placeId;

        private Params(String placeId) {
            this.placeId = placeId;
        }

        public static Params forPlace(String placeId) {
            return new Params(placeId);
        }
    }
}
