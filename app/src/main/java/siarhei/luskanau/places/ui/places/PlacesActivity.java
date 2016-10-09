package siarhei.luskanau.places.ui.places;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class PlacesActivity extends DrawerWithToolbarActivity {

    @Override
    public int getContentResId() {
        return R.layout.activity_places;
    }

    @Override
    public int getDrawerMenuItemId() {
        return R.id.nav_map;
    }

}