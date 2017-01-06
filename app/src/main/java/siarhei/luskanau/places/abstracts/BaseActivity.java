package siarhei.luskanau.places.abstracts;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.internal.di.components.ActivityComponent;
import siarhei.luskanau.places.presentation.internal.di.components.ApplicationComponent;
import siarhei.luskanau.places.presentation.internal.di.components.DaggerActivityComponent;
import siarhei.luskanau.places.presentation.internal.di.modules.ActivityModule;
import siarhei.luskanau.places.presentation.navigation.Navigator;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected EspressoIdlingResource espressoIdlingResource;

    @Inject
    protected Navigator navigator;

    protected ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeActivityComponent();
        this.activityComponent.inject(this);
    }

    private void initializeActivityComponent() {
        this.activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return espressoIdlingResource.getIdlingResource();
    }
}
