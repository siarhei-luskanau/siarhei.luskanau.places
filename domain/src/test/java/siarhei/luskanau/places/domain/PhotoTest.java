package siarhei.luskanau.places.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PhotoTest {

    private static final String FAKE_PHOTO_URL = "place_id";

    private Photo photo;

    @Before
    public void setUp() {
        photo = new Photo(FAKE_PHOTO_URL);
    }

    @Test
    public void testConstructorHappyCase() {
        String photoUrl = photo.getPhotoUrl();

        assertThat(photoUrl, is(FAKE_PHOTO_URL));
    }
}