package siarhei.luskanau.places.abstracts;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {

    public void releaseSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}