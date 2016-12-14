package siarhei.luskanau.places.abstracts;

import com.google.android.gms.common.api.GoogleApiClient;

public interface GoogleApiInterface {

    boolean isPermissionsGranted();

    void requestPermissions();

    GoogleApiClient getGoogleApiClient();
}
