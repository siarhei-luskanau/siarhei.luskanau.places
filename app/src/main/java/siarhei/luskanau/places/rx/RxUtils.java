package siarhei.luskanau.places.rx;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class RxUtils {

    private RxUtils() {
    }

    public static <T> Func1<List<T>, Observable<T>> flatList() {
        return new Func1<List<T>, Observable<T>>() {
            @Override
            public Observable<T> call(List<T> ts) {
                return Observable.from(ts);
            }
        };
    }

}