package siarhei.luskanau.places.data.net.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MapsGoogleApiService {

    String BASE_URL = "https://maps.googleapis.com/";
    String PLACE_PHOTO_SEGMENTS = "maps/api/place/photo";

    @GET("maps/api/place/nearbysearch/json?rankby=distance")
    Observable<String> placeNearbySearch(@Query("location") String location);

    @GET("maps/api/place/details/json")
    Observable<String> placeDetails(@Query("placeid") String placeid);

}