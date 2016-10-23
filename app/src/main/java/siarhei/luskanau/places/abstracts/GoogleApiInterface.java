package siarhei.luskanau.places.abstracts;

import siarhei.luskanau.places.api.GoogleApi;

public interface GoogleApiInterface {

    boolean isPermissionsGranted();

    void requestPermissions();

    GoogleApi getGoogleApi();

}