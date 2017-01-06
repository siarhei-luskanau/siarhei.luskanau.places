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

import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.exception.DefaultErrorBundle;
import siarhei.luskanau.places.domain.exception.ErrorBundle;
import siarhei.luskanau.places.domain.interactor.DefaultSubscriber;
import siarhei.luskanau.places.domain.interactor.GetPlaceDetails;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.view.placedetails.PlaceDetailsView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class PlaceDetailsPresenter implements Presenter {

    private final GetPlaceDetails getPlaceDetailsUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private PlaceDetailsView placeDetailsView;

    public PlaceDetailsPresenter(GetPlaceDetails getPlaceDetailsUseCase,
                                 ErrorMessageFactory errorMessageFactory) {
        this.getPlaceDetailsUseCase = getPlaceDetailsUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull PlaceDetailsView view) {
        this.placeDetailsView = view;
    }

    @Override
    public void destroy() {
        this.getPlaceDetailsUseCase.dispose();
        this.placeDetailsView = null;
    }

    public void setPlaceId(String placeId) {
        this.getPlaceDetailsUseCase.execute(new PlaceDetailsSubscriber(),
                GetPlaceDetails.Params.forPlace(placeId));
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
        String errorMessage = errorMessageFactory.create(errorBundle.getException());
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
