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
package siarhei.luskanau.places.presentation.internal.di.components;

import dagger.Component;
import siarhei.luskanau.places.presentation.internal.di.PerActivity;
import siarhei.luskanau.places.presentation.internal.di.modules.ActivityModule;
import siarhei.luskanau.places.presentation.internal.di.modules.PlaceModule;
import siarhei.luskanau.places.presentation.view.placedetails.PlaceDetailsFragment;
import siarhei.luskanau.places.ui.places.PlaceListFragment;

/**
 * A scope {@link PerActivity} component.
 * Injects place specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, PlaceModule.class})
public interface PlaceComponent extends ActivityComponent {

    void inject(PlaceListFragment placeListFragment);

    void inject(PlaceDetailsFragment placeDetailsFragment);
}
