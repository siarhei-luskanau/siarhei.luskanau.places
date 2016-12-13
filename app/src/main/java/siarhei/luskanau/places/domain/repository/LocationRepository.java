package siarhei.luskanau.places.domain.repository;

import android.location.Location;

import rx.Observable;

public interface LocationRepository {

    /**
     * Get an {@link Observable} which will emit a {@link Location}.
     */
    Observable<Location> location();
}
