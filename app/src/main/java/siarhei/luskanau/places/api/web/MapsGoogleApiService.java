package siarhei.luskanau.places.api.web;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MapsGoogleApiService {

    String BASE_URL = "https://maps.googleapis.com/";

    @GET("maps/api/place/nearbysearch/json?rankby=distance")
    Observable<String> placeNearbySearch(@Query("location") String location);

}