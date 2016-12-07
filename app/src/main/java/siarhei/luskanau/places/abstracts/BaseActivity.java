package siarhei.luskanau.places.abstracts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import rx.Subscription;
import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.presentation.internal.di.components.ApplicationComponent;
import siarhei.luskanau.places.presentation.navigation.Navigator;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}