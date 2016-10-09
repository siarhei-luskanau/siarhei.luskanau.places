package siarhei.luskanau.places.ui.places;

import android.os.Bundle;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class PlacesActivity extends DrawerWithToolbarActivity {

    @Override
    public int getContentResId() {
        return R.layout.activity_places;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.nav_places);
    }

    @Override
    public int getDrawerMenuItemId() {
        return R.id.nav_map;
    }

}