package siarhei.luskanau.places.presentation.view.placedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.GoogleApiClientActivity;
import siarhei.luskanau.places.presentation.internal.di.HasComponent;
import siarhei.luskanau.places.presentation.internal.di.components.DaggerPlaceComponent;
import siarhei.luskanau.places.presentation.internal.di.components.PlaceComponent;
import siarhei.luskanau.places.presentation.internal.di.modules.PlaceModule;
import siarhei.luskanau.places.ui.places.PlaceDetailsPresenterInterface;

public class PlaceDetailsActivity extends GoogleApiClientActivity
        implements PlaceDetailsPresenterInterface, HasComponent<PlaceComponent> {

    private static final String EXTRA_PLACE_ID = "EXTRA_PLACE_ID";

    private PlaceComponent placeComponent;

    public static Intent getCallingIntent(Context context, String placeId) {
        return new Intent(context, PlaceDetailsActivity.class)
                .putExtra(EXTRA_PLACE_ID, placeId);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_place_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initializeInjector();

        onToolbarTitle(null);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeDetailsFragment);
        if (fragment instanceof PlaceDetailsFragment) {
            PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) fragment;
            placeDetailsFragment.onPlaceIdUpdated(getIntent().getStringExtra(EXTRA_PLACE_ID));
        }
    }

    @Override
    public int getDrawerMenuItemId() {
        return R.id.nav_map;
    }

    @Override
    public boolean isDrawerShowed() {
        return false;
    }

    @Override
    public void onToolbarTitle(CharSequence placeTitle) {
        if (TextUtils.isEmpty(placeTitle)) {
            getSupportActionBar().setTitle(R.string.nav_place_details);
        } else {
            getSupportActionBar().setTitle(placeTitle);
        }
    }

    private void initializeInjector() {
        this.placeComponent = DaggerPlaceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .placeModule(new PlaceModule())
                .build();
    }

    @Override
    public PlaceComponent getComponent() {
        return placeComponent;
    }
}
