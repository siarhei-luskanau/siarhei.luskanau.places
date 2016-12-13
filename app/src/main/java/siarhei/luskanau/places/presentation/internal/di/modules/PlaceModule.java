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
package siarhei.luskanau.places.presentation.internal.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.interactor.GetLocations;
import siarhei.luskanau.places.domain.interactor.GetPlaceDetails;
import siarhei.luskanau.places.domain.interactor.GetPlaceList;
import siarhei.luskanau.places.domain.interactor.UseCase;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

/**
 * Dagger module that provides place related collaborators.
 */
@Module
public class PlaceModule {

    public PlaceModule() {
    }

    @Provides
    @Named("placeList")
    UseCase provideGetPlaceListUseCase(GetPlaceList getPlaceList) {
        return getPlaceList;
    }

    @Provides
    @Named("placeDetails")
    UseCase provideGetPlaceDetailsUseCase(
            PlaceRepository placeRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new GetPlaceDetails(placeRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    GetLocations provideGetLocationsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetLocations(threadExecutor, postExecutionThread);
    }
}
