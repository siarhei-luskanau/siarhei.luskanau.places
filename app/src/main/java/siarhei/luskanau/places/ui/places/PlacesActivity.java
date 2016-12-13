package siarhei.luskanau.places.ui.places;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.presentation.view.PlaceComponentActivity;

public class PlacesActivity extends PlaceComponentActivity
        implements PlaceDetailsPresenterInterface {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PlacesActivity.class);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_places;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onToolbarTitle(null);
    }

    @Override
    public int getDrawerMenuItemId() {
        return R.id.nav_map;
    }

    @Override
    public void onToolbarTitle(CharSequence placeTitle) {
        if (TextUtils.isEmpty(placeTitle)) {
            getSupportActionBar().setTitle(R.string.nav_places);
        } else {
            getSupportActionBar().setTitle(placeTitle);
        }
    }

    @Override
    protected void onPermissionsGranted() {
        super.onPermissionsGranted();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placesPresenterFragment);
        if (fragment instanceof PlacesPresenterFragment) {
            PlacesPresenterFragment placesPresenterFragment = (PlacesPresenterFragment) fragment;
            placesPresenterFragment.loadData();
        }
    }
}
