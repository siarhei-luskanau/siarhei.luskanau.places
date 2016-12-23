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
package siarhei.luskanau.places.data.cache;

import android.content.Context;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import siarhei.luskanau.places.data.cache.serializer.JsonSerializer;
import siarhei.luskanau.places.data.entity.PlaceEntity;
import siarhei.luskanau.places.data.exception.PlaceNotFoundException;
import siarhei.luskanau.places.domain.executor.ThreadExecutor;

/**
 * {@link PlaceCache} implementation.
 */
public class PlaceCacheImpl implements PlaceCache {

    private static final String SETTINGS_FILE_NAME = "siarhei.luskanau.places.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "place_";
    private static final long EXPIRATION_TIME = 60 * 10 * 1000;

    private final Context context;
    private final File cacheDir;
    private final JsonSerializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    /**
     * Constructor of the class {@link PlaceCacheImpl}.
     *
     * @param context              A
     * @param placeCacheSerializer {@link JsonSerializer} for object serialization.
     * @param fileManager          {@link FileManager} for saving serialized objects to the file system.
     */
    @Inject
    public PlaceCacheImpl(Context context, JsonSerializer placeCacheSerializer,
                          FileManager fileManager, ThreadExecutor executor) {
        if (context == null || placeCacheSerializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = placeCacheSerializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public Observable<PlaceEntity> get(final String placeId) {
        return Observable.create(subscriber -> {
            File placeEntityFile = PlaceCacheImpl.this.buildFile(placeId);
            String fileContent = PlaceCacheImpl.this.fileManager.readFileContent(placeEntityFile);
            PlaceEntity placeEntity = PlaceCacheImpl.this.serializer.deserialize(fileContent);

            if (placeEntity != null) {
                subscriber.onNext(placeEntity);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new PlaceNotFoundException());
            }
        });
    }

    @Override
    public void put(PlaceEntity placeEntity) {
        if (placeEntity != null) {
            File placeEntityFile = this.buildFile(placeEntity.getPlaceId());
            if (!isCached(placeEntity.getPlaceId())) {
                String jsonString = this.serializer.serialize(placeEntity);
                this.executeAsynchronously(new CacheWriter(this.fileManager, placeEntityFile,
                        jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }

    @Override
    public boolean isCached(String placeId) {
        File placeEntitiyFile = this.buildFile(placeId);
        return this.fileManager.exists(placeEntitiyFile);
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @param placeId The id place to build the file.
     * @return A valid file.
     */
    private File buildFile(String placeId) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath())
                .append(File.separator)
                .append(DEFAULT_FILE_NAME)
                .append(placeId);

        return new File(fileNameBuilder.toString());
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private void setLastCacheUpdateTimeMillis() {
        long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    /**
     * Executes a {@link Runnable} in another Thread.
     *
     * @param runnable {@link Runnable} to execute
     */
    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    /**
     * {@link Runnable} class for writing to disk.
     */
    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }
}
