package siarhei.luskanau.places.data.repository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.entity.mapper.PlaceEntityDataMapper;
import siarhei.luskanau.places.data.repository.datasource.PlaceDataStore;
import siarhei.luskanau.places.data.repository.datasource.PlaceDataStoreFactory;
import siarhei.luskanau.places.domain.LatLng;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class PlaceDataRepositoryTest {

    private static final LatLng FAKE_LAT_LNG = new LatLng(1.11, 2.22);
    private static final String FAKE_PLACE_ID = "place_id";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private PlaceDataStoreFactory mockPlaceDataStoreFactory;
    @Mock
    private PlaceEntityDataMapper mockPlaceEntityDataMapper;
    @Mock
    private PlaceDataStore mockPlaceDataStore;

    private PlaceDataRepository placeDataRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        placeDataRepository = new PlaceDataRepository(mockPlaceDataStoreFactory,
                mockPlaceEntityDataMapper);

        given(mockPlaceDataStoreFactory.create(anyString())).willReturn(mockPlaceDataStore);
        given(mockPlaceDataStoreFactory.createCloudDataStore()).willReturn(mockPlaceDataStore);
    }

    @Test
    public void testGetPlacesHappyCase() {
        List<PlaceEntity> placesList = new ArrayList<>();
        placesList.add(new PlaceEntity());
        given(mockPlaceDataStore.placeEntityList(FAKE_LAT_LNG)).willReturn(Observable.just(placesList));

        placeDataRepository.places(FAKE_LAT_LNG);

        verify(mockPlaceDataStoreFactory).createCloudDataStore();
        verify(mockPlaceDataStore).placeEntityList(FAKE_LAT_LNG);
    }

    @Test
    public void testGetPlaceHappyCase() {
        PlaceEntity placeEntity = new PlaceEntity();
        given(mockPlaceDataStore.placeEntityDetails(FAKE_PLACE_ID)).willReturn(Observable.just(placeEntity));
        placeDataRepository.place(FAKE_PLACE_ID);

        verify(mockPlaceDataStoreFactory).create(FAKE_PLACE_ID);
        verify(mockPlaceDataStore).placeEntityDetails(FAKE_PLACE_ID);
    }
}
