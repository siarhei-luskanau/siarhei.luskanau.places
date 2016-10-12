package siarhei.luskanau.places.api;

public interface RxGoogleApiInterface {

    boolean isPermissionsGranted();

    void requestPermissions();

    RxGoogleApi getRxGoogleApi();

}