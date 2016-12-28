package siarhei.luskanau.places.rx;

import android.util.Log;

import io.reactivex.Observer;

public abstract class SimpleObserver<T> implements Observer<T> {

    private static final String TAG = "SimpleObserver";

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: ", e);
    }
}
