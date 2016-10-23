package siarhei.luskanau.places.api.web;

import android.content.Context;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import siarhei.luskanau.places.BuildConfig;
import siarhei.luskanau.places.api.web.model.BaseResponse;
import siarhei.luskanau.places.api.web.model.Place;
import siarhei.luskanau.places.api.web.model.PlacesResponse;
import siarhei.luskanau.places.utils.AppUtils;

public final class MapsGoogleApi {

    private static final String TAG = "MapsGoogleApi";
    private static final Gson GSON = new GsonBuilder().create();
    private static MapsGoogleApi instance;
    private MapsGoogleApiService mapsGoogleApiService;
    private String geoApiKey;

    private MapsGoogleApi(Context context) {
        geoApiKey = AppUtils.getGeoApiKey(context);
    }

    public static void init(Context context) {
        if (instance != null) {
            throw new IllegalStateException("Instance isn't null. "
                    + "It means init method is called multiple times.");
        }
        instance = new MapsGoogleApi(context);
    }

    public static MapsGoogleApi get() {
        if (instance == null) {
            throw new IllegalStateException("Instance is null. Call init before use this method.");
        }
        return instance;
    }

    public Observable<List<Place>> getPlaces(Location location) {
        String locationString = String.format(Locale.ENGLISH, "%f,%f",
                location.getLatitude(), location.getLongitude());
        return getService().placeNearbySearch(locationString)
                .map(new Func1<String, List<Place>>() {
                    @Override
                    public List<Place> call(String json) {
                        Log.d(TAG, "placeNearbySearch: " + json);
                        PlacesResponse response = GSON.fromJson(json, PlacesResponse.class);
                        checkResponse(response);
                        return response.results;
                    }
                });
    }

    private void checkResponse(BaseResponse response) {
        if (!BaseResponse.OK.equalsIgnoreCase(response.status)) {
            throw new RuntimeException(response.status);
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
            Log.d(TAG, "response: " + String.valueOf(response));
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