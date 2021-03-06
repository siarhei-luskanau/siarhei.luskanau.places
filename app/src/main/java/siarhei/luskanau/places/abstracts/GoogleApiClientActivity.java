package siarhei.luskanau.places.abstracts;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public abstract class GoogleApiClientActivity extends DrawerWithToolbarActivity {

    private static final String TAG = "GoogleApiClientActivity";
    private static final int CONNECTION_RESOLUTION_REQUEST_CODE = 100;
    private static final int PERMISSIONS_REQUEST = 200;
    private GoogleApiClient googleApiClient;

    public boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsGranted();
                } else {
                    onPermissionsNotGranted();
                }
                break;

            default:
        }
    }

    protected void onPermissionsGranted() {
    }

    protected void onPermissionsNotGranted() {
        finish();
    }

    public GoogleApiClient getGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, connectionResult -> {
                        Log.e(TAG, String.valueOf(connectionResult));
                        googleApiClient = null;
                        try {
                            connectionResult.startResolutionForResult(GoogleApiClientActivity.this,
                                    CONNECTION_RESOLUTION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    })
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        return googleApiClient;
    }
}
