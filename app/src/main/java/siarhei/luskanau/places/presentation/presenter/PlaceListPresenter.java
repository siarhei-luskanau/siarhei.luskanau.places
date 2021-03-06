package siarhei.luskanau.places.presentation.presenter;

import android.support.annotation.NonNull;

import siarhei.luskanau.places.domain.PlaceListBundle;
import siarhei.luskanau.places.domain.exception.DefaultErrorBundle;
import siarhei.luskanau.places.domain.exception.ErrorBundle;
import siarhei.luskanau.places.domain.interactor.DefaultSubscriber;
import siarhei.luskanau.places.domain.interactor.GetPlaceList;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.view.placelist.PlaceListView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation layer.
 */
public class PlaceListPresenter implements Presenter {

    private final GetPlaceList getPlaceListUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private PlaceListView placeListView;

    public PlaceListPresenter(GetPlaceList getPlaceListUseCase,
                              ErrorMessageFactory errorMessageFactory) {
        this.getPlaceListUseCase = getPlaceListUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull PlaceListView view) {
        this.placeListView = view;
    }

    @Override
    public void destroy() {
        this.getPlaceListUseCase.dispose();
        this.placeListView = null;
    }

    public void updatePlaceList() {
        this.getPlaceListUseCase.execute(new PlaceListSubscriber(), null);
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = errorMessageFactory.create(errorBundle.getException());
        this.placeListView.showError(errorMessage);
    }

    private final class PlaceListSubscriber extends DefaultSubscriber<PlaceListBundle> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            PlaceListPresenter.this.placeListView.showRefreshing(false);
            PlaceListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(PlaceListBundle placeListBundle) {
            PlaceListPresenter.this.placeListView.showRefreshing(false);
            PlaceListPresenter.this.placeListView
                    .renderPlaceList(placeListBundle.getLatLng(), placeListBundle.getPlaces());
        }
    }
}
