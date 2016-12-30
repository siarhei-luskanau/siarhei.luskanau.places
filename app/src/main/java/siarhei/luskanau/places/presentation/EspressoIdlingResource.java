package siarhei.luskanau.places.presentation;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.BuildConfig;
import android.support.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    private final CountingIdlingResource countingIdlingResource;

    public EspressoIdlingResource(String resourceName) {
        this.countingIdlingResource = new CountingIdlingResource(resourceName, BuildConfig.DEBUG);
    }

    public void increment() {
        countingIdlingResource.increment();
    }

    public void decrement() {
        countingIdlingResource.decrement();
    }

    public IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }
}
