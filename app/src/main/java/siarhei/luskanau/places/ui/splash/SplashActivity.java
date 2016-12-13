package siarhei.luskanau.places.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import siarhei.luskanau.places.abstracts.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        next();
    }

    private void next() {
        Intent intent = navigator.getPlacesIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        navigator.startActivityWithAnimations(this, intent);
        finish();
    }
}
