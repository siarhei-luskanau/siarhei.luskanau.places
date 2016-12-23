package siarhei.luskanau.places.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlaceTest {

    private static final String FAKE_PLACE_ID = "place_id";

    private Place place;

    @Before
    public void setUp() {
        place = new Place(FAKE_PLACE_ID);
    }

    @Test
    public void testUserConstructorHappyCase() {
        String userId = place.getId();

        assertThat(userId, is(FAKE_PLACE_ID));
    }
}
