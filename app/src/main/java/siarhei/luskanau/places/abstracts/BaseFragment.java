package siarhei.luskanau.places.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.Subscription;
import siarhei.luskanau.places.presentation.internal.di.HasComponent;
import siarhei.luskanau.places.presentation.navigation.Navigator;

public class BaseFragment extends Fragment {

    protected Navigator navigator;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigator = ((BaseActivity) getActivity()).navigator;
    }

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
