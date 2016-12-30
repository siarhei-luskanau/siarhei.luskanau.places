package siarhei.luskanau.places.presenter;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.observers.DisposableObserver;
import siarhei.luskanau.places.data.exception.NetworkConnectionException;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.interactor.GetPlaceDetails;
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.presenter.PhotosPresenter;
import siarhei.luskanau.places.presentation.view.photos.PhotosView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class PhotosPresenterTest {

    private static final String FAKE_PLACE_ID = "place_id";
    private static Place PLACE = new Place(FAKE_PLACE_ID);

    @Mock
    private GetPlaceDetails mockGetPlaceDetails;
    @Mock
    private ErrorMessageFactory mockErrorMessageFactory;
    @Mock
    private EspressoIdlingResource mockEspressoIdlingResource;
    @Mock
    private PhotosView photosView;

    @Captor
    private ArgumentCaptor<DisposableObserver<Place>> subscriberArgumentCaptor;
    @Captor
    private ArgumentCaptor<GetPlaceDetails.Params> paramsArgumentCaptor;

    private PhotosPresenter photosPresenter;

    @Before
    public void setupPhotosPresenter() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);

        photosPresenter = new PhotosPresenter(mockGetPlaceDetails, mockErrorMessageFactory, mockEspressoIdlingResource);
        photosPresenter.setView(photosView);
    }

    @Test
    public void destroyPresenter() {
        photosPresenter.destroy();
        verify(mockGetPlaceDetails).dispose();
    }

    @Test
    public void loadPlaceFromRepositoryAndLoadIntoView() {
        photosPresenter.setPlaceId(FAKE_PLACE_ID);
        verify(mockGetPlaceDetails).execute(subscriberArgumentCaptor.capture(), paramsArgumentCaptor.capture());

        subscriberArgumentCaptor.getValue().onNext(PLACE);
        verify(photosView).renderPlace(PLACE);
    }

    @Test
    public void loadPlaceFromRepositoryAndShowErrorMessage() {
        photosPresenter.setPlaceId(FAKE_PLACE_ID);
        verify(mockGetPlaceDetails).execute(subscriberArgumentCaptor.capture(), paramsArgumentCaptor.capture());

        subscriberArgumentCaptor.getValue().onError(new NetworkConnectionException());
        verify(photosView).showError(any(String.class));
    }
}
