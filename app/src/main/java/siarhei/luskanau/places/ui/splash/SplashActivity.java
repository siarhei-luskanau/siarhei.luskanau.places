package siarhei.luskanau.places.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.AppConstants;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseActivity;
import siarhei.luskanau.places.rx.SimpleObserver;
import siarhei.luskanau.places.ui.web.WebPresenterInterface;
import siarhei.luskanau.places.utils.AppNavigationUtil;

public class SplashActivity extends BaseActivity implements WebPresenterInterface {

    private static final long SPLASH_DURATION_SEC = 3;
    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        releaseSubscription(subscription);
        subscription = Observable.interval(SPLASH_DURATION_SEC, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        next();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSubscription(subscription);
        subscription = null;
    }

    @Override
    public String getWebUrl() {
        return AppConstants.TEST_TASK_URL;
    }

    private void next() {
        Intent intent = AppNavigationUtil.getPlacesIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        AppNavigationUtil.startActivityWithAnimations(this, intent);
        finish();
    }

}