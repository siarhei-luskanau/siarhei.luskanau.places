package siarhei.luskanau.places.presentation.presenter;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

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
    private final ErrorMessageFactory errorMessageFactory;
    private PlaceListView placeListView;

    @Inject
    public PlaceListPresenter(@Named("placeList") UseCase getPlaceListUseCase,
                              ErrorMessageFactory errorMessageFactory) {
        this.getPlaceListUseCase = (GetPlaceList) getPlaceListUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull PlaceListView view) {
        this.placeListView = view;
    }

    @Override
    public void destroy() {
        this.getPlaceListUseCase.unsubscribe();
        this.placeListView = null;
    }

    public void updatePlaceList() {
        this.getPlaceListUseCase.execute(new PlaceListSubscriber());
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = errorMessageFactory.create(this.placeListView.context(), errorBundle.getException());
        this.placeListView.showError(errorMessage);
    }

    private final class PlaceListSubscriber extends DefaultSubscriber<Pair<Location, List<Place>>> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            PlaceListPresenter.this.placeListView.showRefreshing(false);
            PlaceListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(Pair<Location, List<Place>> pair) {
            PlaceListPresenter.this.placeListView.showRefreshing(false);
            if (pair != null && pair.first != null) {
                PlaceListPresenter.this.placeListView.renderPlaceList(pair.first, pair.second);
            }
        }
    }
}
