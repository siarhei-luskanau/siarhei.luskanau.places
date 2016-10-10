package siarhei.luskanau.places.abstracts;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.utils.AppUtils;

public abstract class DrawerWithToolbarActivity extends DrawerActivity {

    @Override
    public int getDrawerContentResId() {
        return R.layout.activity_toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(AppUtils.getTintDrawable(
                this,
                isDrawerShowed() ? R.drawable.ic_menu : R.drawable.ic_arrow_back,
                R.color.app_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout contentContainer = (FrameLayout) findViewById(R.id.content_container);
        View view = LayoutInflater.from(this).inflate(getContentResId(), contentContainer, false);
        contentContainer.addView(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isDrawerShowed()) {
                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isDrawerShowed() {
        return true;
    }

    public abstract int getContentResId();

}