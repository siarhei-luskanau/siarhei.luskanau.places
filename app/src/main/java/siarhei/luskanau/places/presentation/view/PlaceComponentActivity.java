package siarhei.luskanau.places.presentation.view;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import javax.inject.Inject;

import siarhei.luskanau.places.abstracts.GoogleApiClientActivity;
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.internal.di.HasComponent;
import siarhei.luskanau.places.presentation.internal.di.components.DaggerPlaceComponent;
import siarhei.luskanau.places.presentation.internal.di.components.PlaceComponent;
import siarhei.luskanau.places.presentation.internal.di.modules.PlaceModule;

public abstract class PlaceComponentActivity extends GoogleApiClientActivity implements HasComponent<PlaceComponent> {

    @Inject
    protected EspressoIdlingResource espressoIdlingResource;

    private PlaceComponent placeComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initializeInjector();
        placeComponent.inject(this);
    }

    private void initializeInjector() {
        this.placeComponent = DaggerPlaceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .placeModule(new PlaceModule())
                .build();
    }

    @Override
    public PlaceComponent getComponent() {
        return placeComponent;
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return espressoIdlingResource.getIdlingResource();
    }
}
