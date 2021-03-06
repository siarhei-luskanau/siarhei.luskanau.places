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
import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.data.cache.FileManager;
import siarhei.luskanau.places.data.cache.PlaceCache;
import siarhei.luskanau.places.data.cache.PlaceCacheImpl;
import siarhei.luskanau.places.data.cache.serializer.JsonSerializer;
import siarhei.luskanau.places.data.entity.mapper.PlaceEntityDataMapper;
import siarhei.luskanau.places.data.executor.JobExecutor;
import siarhei.luskanau.places.data.repository.PlaceDataRepository;
import siarhei.luskanau.places.data.repository.datasource.PlaceDataStoreFactory;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.PlaceRepository;
import siarhei.luskanau.places.presentation.UIThread;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.utils.AppUtils;

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
    AppApplication provideApplicationContext() {
        return this.application;
    }

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    PlaceRepository providePlaceRepository(ThreadExecutor threadExecutor) {
        String geoApiKey = AppUtils.getGeoApiKey(this.application);
        PlaceCache placeCache = new PlaceCacheImpl(this.application,
                new JsonSerializer(),
                new FileManager(),
                threadExecutor);
        PlaceDataStoreFactory dataStoreFactory = new PlaceDataStoreFactory(this.application, placeCache, geoApiKey);
        return new PlaceDataRepository(dataStoreFactory, new PlaceEntityDataMapper());
    }

    @Provides
    ErrorMessageFactory provide() {
        return new ErrorMessageFactory(application);
    }
}
