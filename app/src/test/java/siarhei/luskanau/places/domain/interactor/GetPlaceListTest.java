package siarhei.luskanau.places.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.LocationRepository;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GetPlaceListTest {

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
        getPlaceList.buildUseCaseObservable();

        verify(mockLocationRepository).location();
        verifyNoMoreInteractions(mockLocationRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
