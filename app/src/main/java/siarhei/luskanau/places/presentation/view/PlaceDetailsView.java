/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package siarhei.luskanau.places.presentation.view;

import siarhei.luskanau.places.model.PlaceModel;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a place profile.
 */
public interface PlaceDetailsView {

  /**
   * Render a place in the UI.
   *
   * @param placeModel The {@link PlaceModel} that will be shown.
   */
  void renderPlace(PlaceModel placeModel);
}
