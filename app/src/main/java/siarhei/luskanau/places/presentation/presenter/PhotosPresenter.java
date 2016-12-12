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
import siarhei.luskanau.places.presentation.view.photos.PhotosView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class PhotosPresenter implements Presenter {

    private final GetPlaceDetails getPlaceDetailsUseCase;
    private PhotosView photosView;

    @Inject
    public PhotosPresenter(@Named("placeDetails") UseCase getPlaceDetailsUseCase) {
        this.getPlaceDetailsUseCase = (GetPlaceDetails) getPlaceDetailsUseCase;
    }

    public void setView(@NonNull PhotosView view) {
        this.photosView = view;
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
        this.photosView = null;
    }

    public void setPlaceId(String placeId) {
        this.getPlaceDetailsUseCase.setPlaceId(placeId);
        this.getPlaceDetailsUseCase.execute(new PhotosPresenter.PhotosSubscriber());
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.photosView.context(), errorBundle.getException());
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
    }
}