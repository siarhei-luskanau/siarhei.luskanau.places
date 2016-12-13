package siarhei.luskanau.places.presentation.view.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.presentation.view.PlaceComponentActivity;
import siarhei.luskanau.places.ui.places.PlaceDetailsPresenterInterface;

public class PlacePhotosActivity extends PlaceComponentActivity
        implements PlaceDetailsPresenterInterface, PlacePhotosPresenterInterface {

    private static final String EXTRA_PLACE_ID = "EXTRA_PLACE_ID";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";

    public static Intent buildIntent(Context context, String placeId, int position) {
        return new Intent(context, PlacePhotosActivity.class)
                .putExtra(EXTRA_PLACE_ID, placeId)
                .putExtra(EXTRA_POSITION, position);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_place_photos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.nav_place_details);
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

    @Override
    public String getPlaceId() {
        return getIntent().getStringExtra(EXTRA_PLACE_ID);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(EXTRA_POSITION, 0);
    }
}
