package siarhei.luskanau.places.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import siarhei.luskanau.places.AppConstants;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.ui.home.HomeActivity;
import siarhei.luskanau.places.ui.places.PlacesActivity;
import siarhei.luskanau.places.utils.AppNavigationUtil;
import siarhei.luskanau.places.utils.RoundedBitmapImageViewTarget;

public abstract class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        setupNavigationView((NavigationView) findViewById(R.id.nav_view));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View view = LayoutInflater.from(this).inflate(getDrawerContentResId(), drawerLayout, false);
        drawerLayout.addView(view, 0);
    }

    public void setupNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(getDrawerMenuItemId());
        for (int i = 0; i < navigationView.getHeaderCount(); i++) {
            View headerView = navigationView.getHeaderView(i);
            ImageView profileImageView = (ImageView) headerView.findViewById(R.id.profile_image);
            if (profileImageView != null) {
                Glide.with(this)
                        .load(AppConstants.PROFILE_IMAGE_URL)
                        .asBitmap()
                        .fitCenter()
                        .placeholder(R.drawable.ic_android)
                        .into(new RoundedBitmapImageViewTarget(profileImageView));
                break;
            }
        }
    }

    public abstract int getDrawerContentResId();

    public abstract int getDrawerMenuItemId();

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (!(this instanceof HomeActivity)) {
                    intent = AppNavigationUtil.getHomeIntent(this);
                }
                break;

            case R.id.nav_map:
                if (!(this instanceof PlacesActivity)) {
                    intent = AppNavigationUtil.getPlacesIntent(this);
                }
                break;

            case R.id.nav_attachment:
                intent = AppNavigationUtil.getWebIntent(this, AppConstants.TEST_TASK_URL, item.getTitle());
                break;

            case R.id.nav_github:
                intent = AppNavigationUtil.getWebIntent(this, AppConstants.GITHUB_URL, item.getTitle());
                break;

            case R.id.nav_linkedin:
                intent = AppNavigationUtil.getWebIntent(this, AppConstants.LINKEDIN_URL, item.getTitle());
                break;

            default:
                Toast.makeText(this, "Item clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (intent != null) {
            AppNavigationUtil.startActivityWithAnimations(this, intent);
        }

        return false;
    }

}