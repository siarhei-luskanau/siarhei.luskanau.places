package siarhei.luskanau.places.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import rx.Subscription;
import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.presentation.internal.di.components.ApplicationComponent;
import siarhei.luskanau.places.presentation.navigation.Navigator;

public class BaseFragment extends Fragment {

    @Inject
    protected Navigator navigator;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getActivity().getApplication()).getApplicationComponent();
    }

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}