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
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.internal.di.PerActivity;
import siarhei.luskanau.places.presentation.view.photos.PhotosView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class PhotosPresenter implements Presenter {

    private final GetPlaceDetails getPlaceDetailsUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private final EspressoIdlingResource espressoIdlingResource;
    private PhotosView photosView;

    @Inject
    public PhotosPresenter(@Named("placeDetails") UseCase getPlaceDetailsUseCase,
                           ErrorMessageFactory errorMessageFactory,
                           EspressoIdlingResource espressoIdlingResource) {
        this.getPlaceDetailsUseCase = (GetPlaceDetails) getPlaceDetailsUseCase;
        this.errorMessageFactory = errorMessageFactory;
        this.espressoIdlingResource = espressoIdlingResource;
    }

    public void setView(@NonNull PhotosView view) {
        this.photosView = view;
    }

    @Override
    public void destroy() {
        this.getPlaceDetailsUseCase.dispose();
        this.photosView = null;
    }

    public void setPlaceId(String placeId) {
        espressoIdlingResource.increment();
        this.getPlaceDetailsUseCase.execute(new PhotosPresenter.PhotosSubscriber(),
                GetPlaceDetails.Params.forPlace(placeId));
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = errorMessageFactory.create(this.photosView.context(), errorBundle.getException());
        this.photosView.showError(errorMessage);
    }

    private void showPlaceDetailsInView(Place place) {
        this.photosView.renderPlace(place);
    }

    private final class PhotosSubscriber extends DefaultSubscriber<Place> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            PhotosPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(Place place) {
            PhotosPresenter.this.showPlaceDetailsInView(place);
        }

        @Override
        public void onComplete() {
            super.onComplete();
            espressoIdlingResource.decrement();
        }
    }
}
