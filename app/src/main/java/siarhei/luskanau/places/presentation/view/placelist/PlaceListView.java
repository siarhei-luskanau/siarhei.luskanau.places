package siarhei.luskanau.places.presentation.view.placelist;

import android.content.Context;

import java.util.List;

import siarhei.luskanau.places.domain.LatLng;
import siarhei.luskanau.places.domain.Place;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a place profile.
 */
public interface PlaceListView {

    /**
     * Get a {@link android.content.Context}.
     */
    Context context();

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    void showError(String message);

    /**
     * Show refreshing progress
     *
     * @param refreshing is refreshing.
     */
    void showRefreshing(boolean refreshing);

    /**
     * Render a place in the UI.
     *
     * @param places The list of {@link Place} that will be shown.
     */
    void renderPlaceList(LatLng location, List<Place> places);
}
