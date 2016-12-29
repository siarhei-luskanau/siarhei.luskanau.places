package siarhei.luskanau.places.view.activity;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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

    @Test
    public void listGoesOverTheFold() {
        onView(withId(R.id.placePhotosFragment)).check(matches(isDisplayed()));
    }
}
