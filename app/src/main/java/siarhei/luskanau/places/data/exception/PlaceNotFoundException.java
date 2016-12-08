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
package siarhei.luskanau.places.data.exception;

/**
 * Exception throw by the application when a Place search can't return a valid result.
 */
public class PlaceNotFoundException extends Exception {

    public PlaceNotFoundException() {
        super();
    }

    public PlaceNotFoundException(final String message) {
        super(message);
    }

    public PlaceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PlaceNotFoundException(final Throwable cause) {
        super(cause);
    }

}