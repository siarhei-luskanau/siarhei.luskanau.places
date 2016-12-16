package siarhei.luskanau.places.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public final class AppUtils {

    private static final String TAG = "AppUtils";
    private static final String GEO_API_KEY = "com.google.android.geo.API_KEY";

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

    public static String buildMapUrl(double latitude, double longitude) {
        return String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=%f,%f", latitude, longitude);
    }

    public static String getGeoApiKey(Context context) {
        Object key = getManifestMetadata(context, GEO_API_KEY);
        if (key != null) {
            return String.valueOf(key);
        }
        return null;
    }

    public static Object getManifestMetadata(Context context, String key) {
        try {
            Bundle metaData = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
            if (metaData != null && metaData.containsKey(key)) {
                return metaData.get(key);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static int getScreenLayoutSize(Context context) {
        return context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public static void checkScreenSize(Context context) {
        switch (getScreenLayoutSize(context)) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Toast.makeText(context, "Small sized screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Toast.makeText(context, "Normal sized screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Toast.makeText(context, "Large screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                Toast.makeText(context, "Xlarge screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
            default:
                Toast.makeText(context, "Screen size is undefined", Toast.LENGTH_LONG).show();
        }
    }
}
