package siarhei.luskanau.places.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.PlaceRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GetPlaceDetailsTest {

    private static final String FAKE_PLACE_ID = "place_id";

    private GetPlaceDetails getPlaceDetails;

    @Mock
    private PlaceRepository mockPlaceRepository;
    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getPlaceDetails = new GetPlaceDetails(mockPlaceRepository,
                mockThreadExecutor, mockPostExecutionThread);
    }

    @Test
    public void testGetPlaceDetailsUseCaseObservableHappyCase() {
        getPlaceDetails.buildUseCaseObservable(GetPlaceDetails.Params.forPlace(FAKE_PLACE_ID));

        verify(mockPlaceRepository).place(FAKE_PLACE_ID);
        verifyNoMoreInteractions(mockPlaceRepository);
        verifyZeroInteractions(mockPostExecutionThread);
        verifyZeroInteractions(mockThreadExecutor);
    }
}
