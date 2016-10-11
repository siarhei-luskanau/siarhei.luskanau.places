package siarhei.luskanau.places.ui.places;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;

public class PlaceDetailsActivity extends DrawerWithToolbarActivity
        implements PlaceDetailsPresenterInterface, PlacesApiInterface {

    private static final String TAG = "PlaceDetailsActivity";
    private static final String EXTRA_PLACE_ID = "EXTRA_PLACE_ID";
    private static final int CONNECTION_RESOLUTION_REQUEST_CODE = 100;
    private PlacesApi placesApi;

    public static Intent buildIntent(Context context, String placeId) {
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

    @Override
    public PlacesApi getPlacesApi() {
        if (placesApi == null) {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,
                            new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                    Log.e(TAG, String.valueOf(connectionResult));
                                    placesApi = null;
                                    try {
                                        connectionResult.startResolutionForResult(PlaceDetailsActivity.this,
                                                CONNECTION_RESOLUTION_REQUEST_CODE);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG, e.getMessage(), e);
                                    }
                                }
                            })
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            placesApi = new PlacesApi(googleApiClient);
        }
        return placesApi;
    }

}