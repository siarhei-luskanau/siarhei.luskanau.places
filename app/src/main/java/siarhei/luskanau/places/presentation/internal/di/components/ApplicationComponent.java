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
import siarhei.luskanau.places.AppApplication;
import siarhei.luskanau.places.domain.executor.PostExecutionThread;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;
import siarhei.luskanau.places.domain.repository.PlaceRepository;
import siarhei.luskanau.places.presentation.exception.ErrorMessageFactory;
import siarhei.luskanau.places.presentation.internal.di.modules.ApplicationModule;
import siarhei.luskanau.places.presentation.internal.di.scope.ApplicationScope;

/**
 * A component whose lifetime is the life of the application.
 */
@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    AppApplication appApplication();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    PlaceRepository placeRepository();

    ErrorMessageFactory errorMessageFactory();
}