package siarhei.luskanau.places.presentation.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.exception.DefaultErrorBundle;
import siarhei.luskanau.places.domain.exception.ErrorBundle;
import siarhei.luskanau.places.domain.interactor.DefaultSubscriber;
import siarhei.luskanau.places.domain.interactor.GetPlaceList;
import siarhei.luskanau.places.domain.interactor.UseCase;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.view.placelist.PlaceListView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation layer.
 */
public class PlaceListPresenter implements Presenter {

    private final GetPlaceList getPlaceListUseCase;
    private PlaceListView placeListView;

    @Inject
    public PlaceListPresenter(@Named("placeList") UseCase getPlaceListUseCase) {
        this.getPlaceListUseCase = (GetPlaceList) getPlaceListUseCase;
    }

    public void setView(@NonNull PlaceListView view) {
        this.placeListView = view;
    }

    @Override
    public void resume() {
        updatePlaceList();
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getPlaceListUseCase.unsubscribe();
        this.placeListView = null;
    }

    public void updatePlaceList() {
        this.getPlaceListUseCase.unsubscribe();
        this.getPlaceListUseCase.execute(new PlaceListSubscriber());
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.placeListView.context(), errorBundle.getException());
        this.placeListView.showError(errorMessage);
    }

    private void showPlaceListInView(List<Place> places) {
        this.placeListView.renderPlaceList(places);
    }

    private final class PlaceListSubscriber extends DefaultSubscriber<List<Place>> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            PlaceListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(List<Place> places) {
            PlaceListPresenter.this.showPlaceListInView(places);
        }
    }
}
