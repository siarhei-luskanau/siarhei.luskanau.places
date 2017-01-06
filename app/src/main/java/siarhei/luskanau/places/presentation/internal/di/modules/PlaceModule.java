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

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.places.abstracts.BaseActivity;
import siarhei.luskanau.places.data.repository.LocationRepositoryImpl;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.interactor.GetPlaceDetails;
import siarhei.luskanau.places.domain.interactor.GetPlaceList;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.presenter.PhotosPresenter;
import siarhei.luskanau.places.presentation.presenter.PlaceDetailsPresenter;
import siarhei.luskanau.places.presentation.presenter.PlaceListPresenter;

/**
 * Dagger module that provides place related collaborators.
 */
@Module
public class PlaceModule {

    @Provides
    LocationRepository provideLocationRepository(BaseActivity context) {
        return new LocationRepositoryImpl(context);
    }

    @Provides
    GetPlaceList provideGetPlaceList(LocationRepository locationRepository, PlaceRepository placeRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetPlaceList(locationRepository, placeRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    GetPlaceDetails provideGetPlaceDetails(PlaceRepository placeRepository,
                                           ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread) {
        return new GetPlaceDetails(placeRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    PlaceListPresenter providePlaceListPresenter(GetPlaceList getPlaceListUseCase,
                                                 ErrorMessageFactory errorMessageFactory) {
        return new PlaceListPresenter(getPlaceListUseCase, errorMessageFactory);
    }

    @Provides
    PlaceDetailsPresenter providePlaceDetailsPresenter(GetPlaceDetails getPlaceDetailsUseCase,
                                                       ErrorMessageFactory errorMessageFactory) {
        return new PlaceDetailsPresenter(getPlaceDetailsUseCase, errorMessageFactory);
    }

    @Provides
    PhotosPresenter providePhotosPresenter(GetPlaceDetails getPlaceDetailsUseCase,
                                           ErrorMessageFactory errorMessageFactory,
                                           EspressoIdlingResource espressoIdlingResource) {
        return new PhotosPresenter(getPlaceDetailsUseCase, errorMessageFactory, espressoIdlingResource);
    }
}
