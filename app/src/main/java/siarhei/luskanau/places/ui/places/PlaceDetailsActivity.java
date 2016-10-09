package siarhei.luskanau.places.ui.places;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class PlaceDetailsActivity extends DrawerWithToolbarActivity {

    @Override
    public int getContentResId() {
        return R.layout.activity_place_details;
    }

    @Override
    public int getDrawerMenuItemId() {
        return R.id.nav_map;
    }

}