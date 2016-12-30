package siarhei.luskanau.places.view.activity;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.presentation.view.photos.PlacePhotosActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PlacePhotosActivityTest {

    @Rule
    public ActivityTestRule<PlacePhotosActivity> mActivityRule = new ActivityTestRule(PlacePhotosActivity.class);

    private static final String FAKE_PLACE_ID = "ChIJw3lIVZyAhYARGtSW4pX2Dvg";

    @Before
    public void intentWithStubbedNoteId() {
        Intent intent = new Intent();
        PlacePhotosActivity.buildIntent(intent, FAKE_PLACE_ID, 0);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void launchActivity() {
        onView(withId(R.id.contentFrame)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()));
    }

}
