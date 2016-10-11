package siarhei.luskanau.places.ui.places;

import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.DrawerWithToolbarActivity;
import siarhei.luskanau.places.api.PlacesApi;
import siarhei.luskanau.places.api.PlacesApiInterface;

public class PlacesActivity extends DrawerWithToolbarActivity
        implements PlaceDetailsPresenterInterface, PlacesApiInterface {

    private static final String TAG = "PlacesActivity";
    private static final int CONNECTION_RESOLUTION_REQUEST_CODE = 100;
    private PlacesApi placesApi;

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
                                        connectionResult.startResolutionForResult(PlacesActivity.this,
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