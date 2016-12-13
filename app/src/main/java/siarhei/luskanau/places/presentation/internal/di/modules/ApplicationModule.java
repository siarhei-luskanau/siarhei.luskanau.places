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

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.data.cache.PlaceCache;
import siarhei.luskanau.places.data.cache.PlaceCacheImpl;
import siarhei.luskanau.places.data.executor.JobExecutor;
import siarhei.luskanau.places.data.repository.PlaceDataRepository;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.PlaceRepository;
import siarhei.luskanau.places.presentation.UIThread;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
    private final AppApplication application;

    public ApplicationModule(AppApplication application) {
        this.application = application;
    }

    @Provides
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    PlaceCache providePlaceCache(PlaceCacheImpl placeCache) {
        return placeCache;
    }

    @Provides
    PlaceRepository providePlaceRepository(PlaceDataRepository placeDataRepository) {
        return placeDataRepository;
    }
}
