package siarhei.luskanau.places.ui.places;

import android.os.Bundle;
import android.text.TextUtils;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class PlacesActivity extends DrawerWithToolbarActivity
        implements PlaceDetailsPresenterInterface {

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

}