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
package siarhei.luskanau.places.presentation.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.interactor.DefaultSubscriber;
import siarhei.luskanau.places.domain.interactor.UseCase;
import siarhei.luskanau.places.model.PlaceModel;
import siarhei.luskanau.places.presentation.internal.di.PerActivity;
import siarhei.luskanau.places.presentation.mapper.PlaceModelDataMapper;
import siarhei.luskanau.places.presentation.view.PlaceDetailsView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class PlaceDetailsPresenter implements Presenter {

    private PlaceDetailsView viewDetailsView;

    private final UseCase getPlaceDetailsUseCase;
    private final PlaceModelDataMapper placeModelDataMapper;

    @Inject
    public PlaceDetailsPresenter(@Named("placeDetails") UseCase getPlaceDetailsUseCase,
                                 PlaceModelDataMapper placeModelDataMapper) {
        this.getPlaceDetailsUseCase = getPlaceDetailsUseCase;
        this.placeModelDataMapper = placeModelDataMapper;
    }

    public void setView(@NonNull PlaceDetailsView view) {
        this.viewDetailsView = view;
    }

    @Override
    public void resume() {
        getPlaceDetailsUseCase.execute(new PlaceDetailsSubscriber());
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getPlaceDetailsUseCase.unsubscribe();
        this.viewDetailsView = null;
    }

    /**
     * Initializes the presenter by start retrieving place details.
     */
    public void initialize() {
        this.loadPlaceDetails();
    }

    /**
     * Loads place details.
     */
    private void loadPlaceDetails() {
        this.getPlaceDetails();
    }

    private void showPlaceDetailsInView(Place place) {
        final PlaceModel placeModel = this.placeModelDataMapper.transform(place);
        this.viewDetailsView.renderPlace(placeModel);
    }

    private void getPlaceDetails() {
        this.getPlaceDetailsUseCase.execute(new PlaceDetailsSubscriber());
    }

    //  @RxLogSubscriber
    private final class PlaceDetailsSubscriber extends DefaultSubscriber<Place> {
        @Override
        public void onNext(Place place) {
            PlaceDetailsPresenter.this.showPlaceDetailsInView(place);
        }
    }
}
