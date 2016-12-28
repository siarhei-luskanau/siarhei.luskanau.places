package siarhei.luskanau.places.domain.repository;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.Place;

public interface PlaceRepository {

    /**
     * Get an {@link Observable} which will emit a List of {@link Place}.
     */
    Observable<List<Place>> places(LatLng location);

    /**
     * Get an {@link Observable} which will emit a {@link Place}.
     *
     * @param placeId The user id used to retrieve user data.
     */
    Observable<Place> place(final String placeId);
}
