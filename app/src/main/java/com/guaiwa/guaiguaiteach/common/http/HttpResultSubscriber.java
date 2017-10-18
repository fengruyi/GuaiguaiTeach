package com.guaiwa.guaiguaiteach.common.http;



import com.guaiwa.guaiguaiteach.common.utils.Logger;

import java.net.ConnectException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by 80151689 on 2016-11-28.
 * 减少回调函数 onSuccess必须，其他可选
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Logger.d("HttpResultSubscriber", "onError:" + e);
        try {
            if (e instanceof UnknownHostException) {
                onFailue(new ConnectException("UnknownHostException converto ConnectException"));
            } else {
                onFailue(e);
            }
            unsubscribe();
        } catch (Throwable e2) {

        }

    }

    @Override
    public void onNext(T t) {
        try {
            if (t == null) {
                onFailue(new NullPointerException("get a null object!"));
            } else {
                onSuccess(t);
            }
            unsubscribe();
        } catch (Throwable e) {

        }
    }

    protected void onFailue(Throwable e) {

    }

    protected abstract void onSuccess(T t);
}

