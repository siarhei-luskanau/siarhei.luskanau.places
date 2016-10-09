package siarhei.luskanau.places.ui.web;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import siarhei.luskanau.places.AppConstants;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class WebActivity extends DrawerWithToolbarActivity implements WebPresenterInterface {

    private static final String EXTRA_URL = "EXTRA_URL";

    public static Intent buildIntent(Context context, String url) {
        return new Intent(context, WebActivity.class)
                .putExtra(EXTRA_URL, url);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_web;
    }

    @Override
    public int getDrawerMenuItemId() {
        String url = getWebUrl();
        if (AppConstants.TEST_TASK_URL.equals(url)) {
            return R.id.nav_attachment;
        } else if (AppConstants.GITHUB_URL.equals(url)) {
            return R.id.nav_github;
        } else if (AppConstants.LINKEDIN_URL.equals(url)) {
            return R.id.nav_linkedin;
        }
        return View.NO_ID;
    }

    @Override
    public boolean isDrawerShowed() {
        return false;
    }

    @Override
    public String getWebUrl() {
        return getIntent().getStringExtra(EXTRA_URL);
    }

}