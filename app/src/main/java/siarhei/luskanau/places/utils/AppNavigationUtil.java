package siarhei.luskanau.places.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import siarhei.luskanau.places.ui.home.HomeActivity;
import siarhei.luskanau.places.ui.web.WebActivity;

public final class AppNavigationUtil {

    private AppNavigationUtil() {
    }

    public static Intent getHomeIntent(Context context) {
        return HomeActivity.buildIntent(context);
    }

    public static Intent getWebIntent(Context context, String url) {
        return WebActivity.buildIntent(context, url);
    }

    public static void startActivityWithAnimations(Activity activity, Intent intent) {
        Bundle options = ActivityOptionsCompat
                .makeCustomAnimation(activity, android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

}