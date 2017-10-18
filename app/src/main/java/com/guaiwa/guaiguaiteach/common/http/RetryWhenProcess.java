package com.guaiwa.guaiguaiteach.common.http;

import android.util.Log;

import com.google.gson.Gson;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by 80151689 on 2017-10-17.
 * 接口重试复用#.retryWhen(new RetryWhenProcess(5))
 */

public class RetryWhenProcess implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private long mInterval;

    public RetryWhenProcess(long interval) {
        mInterval = interval;
    }

    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
//                        if (throwable instanceof UnknownHostException) {
//                            return observable.error(throwable);
//                        }
                        return Observable.just(throwable).zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {
                            @Override
                            public Integer call(Throwable throwable, Integer i) {
                                Log.d("","retry count:"+i);
                                return i;
                            }
                        }).flatMap(new Func1<Integer, Observable<? extends Long>>() {
                            @Override
                            public Observable<? extends Long> call(Integer retryCount) {

                                return Observable.timer((long) Math.pow(mInterval, retryCount), TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                });
            }
        });
    }
}
