package siarhei.luskanau.places.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GetPlaceListTest {

    private static final double FAKE_LAT = 1.11;
    private static final double FAKE_LNG = 2.22;
    private final LatLng FAKE_LAT_LNG = new LatLng(FAKE_LAT, FAKE_LNG);

    private GetPlaceList getPlaceList;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;
    @Mock
    private LocationRepository mockLocationRepository;
    @Mock
    private PlaceRepository mockPlaceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getPlaceList = new GetPlaceList(mockLocationRepository, mockPlaceRepository,
                mockThreadExecutor, mockPostExecutionThread);
        given(mockLocationRepository.location()).willReturn(Observable.just(FAKE_LAT_LNG));
    }

    @Test
    public void testGetUserListUseCaseObservableHappyCase() {
        getPlaceList.buildUseCaseObservable();

        verify(mockLocationRepository).location();
        verifyNoMoreInteractions(mockLocationRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
