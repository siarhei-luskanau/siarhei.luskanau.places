package siarhei.luskanau.places.data.repository;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.repository.LocationRepository;

public class LocationRepositoryImpl implements LocationRepository {

    private static final String TAG = "LocationRepositoryImpl";
    private static final int DISTANCE_IN_METERS = 100;
    private final Context context;
    private Location lastLocation;

    public LocationRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<LatLng> location() {
        Observable<Location> observable = Observable.create(emitter -> {
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
                    if (lastKnownLocation != null) {
                        emitter.onNext(lastKnownLocation);
                    }

                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "onLocationChanged: " + location);
                            if (!isDisposed(emitter)) {
                                emitter.onNext(location);
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.d(TAG, "onStatusChanged: " + provider + " " + status + " " + extras);
                            isDisposed(emitter);
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.d(TAG, "onProviderEnabled: " + provider);
                            isDisposed(emitter);
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.d(TAG, "onProviderDisabled: " + provider);
                            isDisposed(emitter);
                        }

                        private boolean isDisposed(ObservableEmitter emitter) {
                            boolean isDisposed = emitter.isDisposed();
                            if (isDisposed) {
                                Log.d(TAG, "locationManager.removeUpdates for subscriber: " + emitter);
                                //noinspection MissingPermission
                                locationManager.removeUpdates(this);
                            }
                            return isDisposed;
                        }
                    };
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                if (lastLocation == null) {
                    Location location = new Location("");
                    location.setLatitude(37.7757028);
                    location.setLongitude(-122.4156366);
                    emitter.onNext(location);
                }
            }
        });

        observable = observable.subscribeOn(AndroidSchedulers.mainThread());
        return filterLocations(observable);
    }

    public Observable<LatLng> filterLocations(Observable<Location> observable) {
        lastLocation = null;
        return observable
                .filter(location -> {
                    float distanceTo = -1;
                    if (lastLocation != null) {
                        distanceTo = lastLocation.distanceTo(location);
                    }
                    if (distanceTo < 0 || distanceTo >= DISTANCE_IN_METERS) {
                        lastLocation = location;
                        Log.d(TAG, "newLocation: " + distanceTo + "m " + location);
                        return true;
                    }
                    return false;
                })
                .map(location -> {
                    if (location != null) {
                        return new LatLng(location.getLatitude(), location.getLongitude());
                    }
                    return null;
                });
    }
}
