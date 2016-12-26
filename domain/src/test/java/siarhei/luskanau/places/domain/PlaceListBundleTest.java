package siarhei.luskanau.places.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlaceListBundleTest {

    private final LatLng FAKE_LAT_LNG = new LatLng(1.11, 2.22);
    private final List<Place> FAKE_LIST = Collections.EMPTY_LIST;

    private PlaceListBundle placeListBundle;

    @Before
    public void setUp() {
        placeListBundle = new PlaceListBundle(FAKE_LAT_LNG, FAKE_LIST);
    }

    @Test
    public void testConstructorHappyCase() {
        assertThat(placeListBundle.getLatLng(), is(FAKE_LAT_LNG));
        assertThat(placeListBundle.getPlaces(), is(FAKE_LIST));
    }
}
