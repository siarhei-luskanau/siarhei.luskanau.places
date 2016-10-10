package siarhei.luskanau.places.utils.glide;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

public class PlacePhotoIdModelLoader implements StreamModelLoader<PlacePhotoId> {

    @Override
    public DataFetcher<InputStream> getResourceFetcher(PlacePhotoId model, int width, int height) {
        return new PlacePhotoIdDataFetcher(model, width, height);
    }

    public static class Factory implements ModelLoaderFactory<PlacePhotoId, InputStream> {
        public Factory() {
        }

        public ModelLoader<PlacePhotoId, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new PlacePhotoIdModelLoader();
        }

        @Override
        public void teardown() {
        }
    }

}