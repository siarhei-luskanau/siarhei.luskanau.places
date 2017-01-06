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
package siarhei.luskanau.places.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.places.abstracts.BaseActivity;
import siarhei.luskanau.places.presentation.EspressoIdlingResource;
import siarhei.luskanau.places.presentation.internal.di.scope.ActivityScope;
import siarhei.luskanau.places.presentation.navigation.Navigator;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    BaseActivity provideBaseActivity() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @ActivityScope
    EspressoIdlingResource provideEspressoIdlingResource() {
        return new EspressoIdlingResource(activity.getClass().getSimpleName());
    }
}
