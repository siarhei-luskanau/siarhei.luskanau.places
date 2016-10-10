package siarhei.luskanau.places.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public final class AppUtils {

    private AppUtils() {
    }

    public static Drawable getTintDrawable(Context context, @DrawableRes int drawableResId, @ColorRes int tintResId) {
        return getTintDrawable(
                ContextCompat.getDrawable(context, drawableResId),
                ContextCompat.getColor(context, tintResId)
        );
    }

    public static Drawable getTintDrawable(@NonNull Drawable drawable, @ColorInt int tint) {
        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrapDrawable, tint);
        return wrapDrawable;
    }

    public static <T> T getParentInterface(Class<T> parentClass, Object... parentObjects) {
        for (Object parentObject : parentObjects) {
            if (parentClass != null && parentObject != null && parentClass.isInstance(parentObject)) {
                return (T) parentObject;
            }
        }

        StringBuilder message = new StringBuilder(128);
        message.append("Parent ").append(parentClass).append(" not found in:");
        for (Object parentObject : parentObjects) {
            message.append(' ').append(parentObject);
        }
        throw new IllegalStateException(message.toString());
    }

    public static FragmentActivity findFragmentActivity(Context context) {
        Context forContext = context;
        while (true) {
            if (forContext instanceof FragmentActivity) {
                return (FragmentActivity) forContext;
            } else if (forContext instanceof ContextWrapper) {
                forContext = ((ContextWrapper) forContext).getBaseContext();
            } else {
                return null;
            }
        }
    }

    public static String buildMapUrl(LatLng latLng) {
        return String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=%f,%f",
                latLng.latitude, latLng.longitude);
    }

}