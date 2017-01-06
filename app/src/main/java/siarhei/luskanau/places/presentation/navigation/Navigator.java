/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package siarhei.luskanau.places.presentation.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import siarhei.luskanau.places.presentation.view.photos.PlacePhotosActivity;
import siarhei.luskanau.places.presentation.view.placedetails.PlaceDetailsActivity;
import siarhei.luskanau.places.presentation.view.placelist.PlacesActivity;
import siarhei.luskanau.places.ui.web.WebActivity;

/**
 * Class used to navigate through the application.
 */
public class Navigator {

    public Intent getPlacesIntent(Context context) {
        return PlacesActivity.getCallingIntent(context);
    }

    public Intent getPlaceDetailsIntent(Context context, String placeId) {
        return PlaceDetailsActivity.getCallingIntent(context, placeId);
    }

    public Intent getPlacePhotosIntent(Context context, String placeId, int position) {
        return PlacePhotosActivity.buildIntent(context, placeId, position);
    }

    public Intent getWebIntent(Context context, String url, CharSequence title) {
        return WebActivity.buildIntent(context, url, title);
    }

    public void startActivityWithAnimations(Activity activity, Intent intent) {
        Bundle options = ActivityOptionsCompat
                .makeCustomAnimation(activity, android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }
}
