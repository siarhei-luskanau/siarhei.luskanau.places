package siarhei.luskanau.places.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.Observable;
import rx.observers.TestSubscriber;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.Place;
import siarhei.luskanau.places.domain.PlaceListBundle;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GetPlaceListTest {

    private final LatLng FAKE_LAT_LNG = new LatLng(1.11, 2.22);

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
    }

    @Test
    public void testGetUserListUseCaseObservableHappyCase() {
        given(mockLocationRepository.location()).willReturn(Observable.<LatLng>empty());
        getPlaceList.buildUseCaseObservable();

        verify(mockLocationRepository).location();
        verifyNoMoreInteractions(mockLocationRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }

    @Test
    public void testUseCaseObservableByTestSubscriberHappyCase() {
        given(mockLocationRepository.location())
                .willReturn(Observable.just(FAKE_LAT_LNG));
        given(mockPlaceRepository.places(any(LatLng.class)))
                .willReturn(Observable.just(Collections.<Place>emptyList()));
        TestSubscriber<PlaceListBundle> testSubscriber = new TestSubscriber<>();

        getPlaceList.buildUseCaseObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertNoErrors();
    }
}
