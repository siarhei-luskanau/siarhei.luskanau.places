package siarhei.luskanau.places.data.repository;

import android.location.Location;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import siarhei.luskanau.places.domain.repository.LocationRepository;

public class LocationRepositoryImpl implements LocationRepository {

    @Inject
    public LocationRepositoryImpl() {
    }

    @Override
    public Observable<Location> location() {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(aLong -> {
                    Location location = new Location("");
                    location.setLatitude(52.4217741);
                    location.setLongitude(31.0047937);
                    return Observable.just(location);
                });
    }
}
