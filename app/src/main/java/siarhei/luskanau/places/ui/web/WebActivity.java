package siarhei.luskanau.places.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import siarhei.luskanau.places.AppConstants;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;

public class WebActivity extends DrawerWithToolbarActivity implements WebPresenterInterface {

    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static Intent buildIntent(Context context, String url, CharSequence title) {
        return new Intent(context, WebActivity.class)
                .putExtra(EXTRA_URL, url)
                .putExtra(EXTRA_TITLE, title);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CharSequence title = getIntent().getCharSequenceExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public int getDrawerMenuItemId() {
        String url = getWebUrl();
        if (AppConstants.GITHUB_URL.equals(url)) {
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