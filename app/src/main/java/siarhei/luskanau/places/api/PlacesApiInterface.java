package siarhei.luskanau.places.api;

public interface PlacesApiInterface {

    boolean isPermissionsGranted();

    void requestPermissions();

    PlacesApi getPlacesApi();

}