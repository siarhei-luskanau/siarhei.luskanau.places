package siarhei.luskanau.places.abstracts;

import android.support.v4.app.Fragment;

import rx.Subscription;

public class BaseFragment extends Fragment {

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}