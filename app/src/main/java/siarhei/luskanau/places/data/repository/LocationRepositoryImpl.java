package siarhei.luskanau.places.data.repository;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import siarhei.luskanau.places.domain.repository.LocationRepository;

public class LocationRepositoryImpl implements LocationRepository {

    private static final String TAG = "LocationRepositoryImpl";
    private final Context context;

    @Inject
    public LocationRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Location> location() {
        Observable<Location> observable = Observable.create(subscriber -> {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            for (String provider : locationManager.getAllProviders()) {
                try {
                    Log.d(TAG, "provider: " + provider);
                    if (LocationManager.GPS_PROVIDER.equals(provider)) {
                        continue;
                    }

                    //noinspection MissingPermission
                    Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
                    Log.d(TAG, "LastKnownLocation: " + lastKnownLocation);
                    subscriber.onNext(lastKnownLocation);

                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "onLocationChanged: " + location);
                            if (!isUnsubscribed(subscriber)) {
                                subscriber.onNext(location);
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.d(TAG, "onStatusChanged: " + provider + " " + status + " " + extras);
                            isUnsubscribed(subscriber);
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.d(TAG, "onProviderEnabled: " + provider);
                            isUnsubscribed(subscriber);
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.d(TAG, "onProviderDisabled: " + provider);
                            isUnsubscribed(subscriber);
                        }

                        private boolean isUnsubscribed(Subscriber subscriber) {
                            boolean isUnsubscribed = subscriber.isUnsubscribed();
                            if (isUnsubscribed) {
                                Log.d(TAG, "locationManager.removeUpdates for subscriber: " + subscriber);
                                //noinspection MissingPermission
                                locationManager.removeUpdates(this);
                            }
                            return isUnsubscribed;
                        }
                    };
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

        return observable.subscribeOn(AndroidSchedulers.mainThread());
    }
}
