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
import siarhei.luskanau.places.domain.exception.DefaultErrorBundle;
import siarhei.luskanau.places.domain.exception.ErrorBundle;
import siarhei.luskanau.places.domain.interactor.DefaultSubscriber;
import siarhei.luskanau.places.domain.interactor.GetPlaceDetails;
import siarhei.luskanau.places.domain.interactor.UseCase;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.internal.di.PerActivity;
import siarhei.luskanau.places.presentation.view.placedetails.PlaceDetailsView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class PlaceDetailsPresenter implements Presenter {

    private final GetPlaceDetails getPlaceDetailsUseCase;
    private PlaceDetailsView placeDetailsView;

    @Inject
    public PlaceDetailsPresenter(@Named("placeDetails") UseCase getPlaceDetailsUseCase) {
        this.getPlaceDetailsUseCase = (GetPlaceDetails) getPlaceDetailsUseCase;
    }

    public void setView(@NonNull PlaceDetailsView view) {
        this.placeDetailsView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getPlaceDetailsUseCase.unsubscribe();
        this.placeDetailsView = null;
    }

    public void setPlaceId(String placeId) {
        this.getPlaceDetailsUseCase.setPlaceId(placeId);
        this.getPlaceDetailsUseCase.execute(new PlaceDetailsSubscriber());
    }

    public void onPlacePhoneClicked(CharSequence phoneNumber) {
        this.placeDetailsView.viewPlacePhone(phoneNumber);
    }

    public void onPlaceWebsiteClicked(String url) {
        this.placeDetailsView.viewPlaceWebsite(url);
    }

    public void onPlaceMapClicked(String url) {
        this.placeDetailsView.viewPlaceMap(url);
    }

    public void onPlacePhotoClicked(String placeId, int photoPosition) {
        this.placeDetailsView.viewPlacePhoto(placeId, photoPosition);
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.placeDetailsView.context(), errorBundle.getException());
        this.placeDetailsView.showError(errorMessage);
    }

    private void showPlaceDetailsInView(Place place) {
        this.placeDetailsView.renderPlace(place);
    }

    private final class PlaceDetailsSubscriber extends DefaultSubscriber<Place> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            PlaceDetailsPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(Place place) {
            PlaceDetailsPresenter.this.showPlaceDetailsInView(place);
        }
    }
}