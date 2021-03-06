package siarhei.luskanau.places.domain.repository;

import io.reactivex.Observable;
import siarhei.luskanau.places.domain.LatLng;

public interface LocationRepository {

    /**
     * Get an {@link Observable} which will emit a {@link LatLng}.
     */
    Observable<LatLng> location();
}
