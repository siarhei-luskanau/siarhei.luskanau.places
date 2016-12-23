package siarhei.luskanau.places.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LatLngTest {

    private static final double FAKE_LAT = 1.11;
    private static final double FAKE_LNG = 2.22;

    private LatLng latLng;

    @Before
    public void setUp() {
        latLng = new LatLng(FAKE_LAT, FAKE_LNG);
    }

    @Test
    public void testUserConstructorHappyCase() {
        assertThat(latLng.getLatitude(), is(FAKE_LAT));
        assertThat(latLng.getLongitude(), is(FAKE_LNG));
    }
}
