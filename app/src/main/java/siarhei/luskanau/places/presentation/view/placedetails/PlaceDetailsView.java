/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package siarhei.luskanau.places.presentation.view.placedetails;

import android.content.Context;

import siarhei.luskanau.places.domain.Place;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a place profile.
 */
public interface PlaceDetailsView {

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
     * Render a place in the UI.
     *
     * @param place The {@link Place} that will be shown.
     */
    void renderPlace(Place place);

    /**
     * View a {@link Place} phone number.
     *
     * @param phoneNumber The place phone number that will be shown.
     */
    void viewPlacePhone(CharSequence phoneNumber);

    /**
     * View a {@link Place} website.
     *
     * @param url The place website that will be shown.
     */
    void viewPlaceWebsite(String url);

    /**
     * View a {@link Place} map.
     *
     * @param url The place map that will be shown.
     */
    void viewPlaceMap(String url);

    /**
     * View a {@link Place} photo.
     *
     * @param placeId The place id that will be shown.
     * @param photoPosition The place position that will be shown.
     */
    void viewPlacePhoto(String placeId, int photoPosition);
}
