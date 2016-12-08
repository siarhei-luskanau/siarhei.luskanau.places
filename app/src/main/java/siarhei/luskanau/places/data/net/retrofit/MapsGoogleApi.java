package siarhei.luskanau.places.data.net.retrofit;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.BuildConfig;
import siarhei.luskanau.places.data.entity.BaseResponse;
import siarhei.luskanau.places.data.entity.Photo;
import siarhei.luskanau.places.data.entity.PlaceDetailsResponse;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.entity.PlacesResponse;

public class MapsGoogleApi {

    private static final String TAG = "MapsGoogleApi";
    private static final Gson GSON = new GsonBuilder().create();
    private MapsGoogleApiService mapsGoogleApiService;
    private String geoApiKey;

    public MapsGoogleApi(String geoApiKey) {
        this.geoApiKey = geoApiKey;
    }

    public Observable<List<PlaceEntity>> getPlaces(Location location) {
        String locationString = String.format(Locale.ENGLISH, "%f,%f",
                location.getLatitude(), location.getLongitude());
        return getService().placeNearbySearch(locationString)
                .map(json -> {
                    Log.d(TAG, "placeNearbySearch");
                    PlacesResponse response = GSON.fromJson(json, PlacesResponse.class);
                    checkResponse(response);
                    return response.getResults();
                });
    }

    public Observable<PlaceEntity> getPlaceDetails(String placeId) {
        return getService().placeDetails(placeId)
                .map(json -> {
                    Log.d(TAG, "placeDetails");
                    PlaceDetailsResponse response = GSON.fromJson(json, PlaceDetailsResponse.class);
                    checkResponse(response);
                    return response.getResult();
                })
                .map(placeEntity -> {
                    if (placeEntity.getPhotos() != null) {
                        for (Photo photo : placeEntity.getPhotos()) {
                            photo.setPhotoUrl(getPlacePhotoUrl(photo));
                        }
                    }
                    return placeEntity;
                });
    }

    public String getPlacePhotoUrl(Photo photo) {
        return HttpUrl.parse(MapsGoogleApiService.BASE_URL).newBuilder()
                .addEncodedPathSegments(MapsGoogleApiService.PLACE_PHOTO_SEGMENTS)
                .setQueryParameter("maxwidth", String.valueOf(photo.getWidth()))
                .setQueryParameter("photoreference", photo.getPhotoReference())
                .setQueryParameter("key", geoApiKey)
                .build().toString();
    }

    private void checkResponse(BaseResponse response) {
        if (!BaseResponse.OK.equalsIgnoreCase(response.getStatus())) {
            throw new RuntimeException(response.getStatus());
        }
    }

    private MapsGoogleApiService getService() {
        if (mapsGoogleApiService == null) {
            mapsGoogleApiService = createService(geoApiKey);
        }
        return mapsGoogleApiService;
    }

    private MapsGoogleApiService createService(String key) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        GoogleApiAuthenticatorInterceptor authenticatorInterceptor = new GoogleApiAuthenticatorInterceptor(key);
        okHttpClientBuilder.addInterceptor(authenticatorInterceptor);
        okHttpClientBuilder.authenticator(authenticatorInterceptor);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.BASIC);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MapsGoogleApiService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();

        return retrofit.create(MapsGoogleApiService.class);
    }

    private static class GoogleApiAuthenticatorInterceptor implements Authenticator, Interceptor {

        private String key;

        public GoogleApiAuthenticatorInterceptor(String key) {
            this.key = key;
        }

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            Log.d(TAG, "response: " + response);
            if (responseCount(response) >= 3) {
                // If we've failed 3 times, give up.
                return null;
            }
            Log.e(TAG, "Authenticator not implemented.");
            return buildAuthorizeRequest(response.request(), key);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(buildAuthorizeRequest(chain.request(), key));
        }

        private Request buildAuthorizeRequest(Request request, String key) {
            if (key != null) {
                HttpUrl url = request.url().newBuilder()
                        .setQueryParameter("key", key)
                        .build();
                return request.newBuilder().url(url).build();
            }
            return request;
        }

        private int responseCount(Response response) {
            int result = 1;
            Response prior = response;
            while ((prior = prior.priorResponse()) != null) {
                result++;
            }
            return result;
        }
    }
}
