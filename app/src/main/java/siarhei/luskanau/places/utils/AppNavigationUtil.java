package siarhei.luskanau.places.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import siarhei.luskanau.places.ui.photo.PlacePhotosActivity;
import siarhei.luskanau.places.ui.places.PlaceDetailsActivity;
import siarhei.luskanau.places.ui.places.PlacesActivity;
import siarhei.luskanau.places.ui.web.WebActivity;

public final class AppNavigationUtil {

    private AppNavigationUtil() {
    }

    public static Intent getPlacesIntent(Context context) {
        return new Intent(context, PlacesActivity.class);
    }

    public static Intent getPlaceDetailsIntent(Context context, String placeId) {
        return PlaceDetailsActivity.buildIntent(context, placeId);
    }

    public static Intent getPlacePhotosIntent(Context context, String placeId, int position) {
        return PlacePhotosActivity.buildIntent(context, placeId, position);
    }

    public static Intent getWebIntent(Context context, String url, CharSequence title) {
        return WebActivity.buildIntent(context, url, title);
    }

    public static void startActivityWithAnimations(Activity activity, Intent intent) {
        Bundle options = ActivityOptionsCompat
                .makeCustomAnimation(activity, android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

}