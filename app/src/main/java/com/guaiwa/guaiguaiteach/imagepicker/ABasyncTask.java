package com.guaiwa.guaiguaiteach.imagepicker;

import android.os.AsyncTask;

/**
 * Created by 80151689 on 2017-01-24.
 */
public abstract class ABasyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
    private Callback<Result> mCallback;

    public ABasyncTask(Callback<Result> callback) {
        mCallback = callback;
    }

    public void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mCallback != null) {
            mCallback.onPostExecute(result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mCallback != null) {
            mCallback.onTaskCancelled();
        }
        //无法放到 cancelTask()中，因为此方法会在cancelTask()后执行，所以如果放到cancelTask()中，则此字段永远是空，也就不会调用 onCancel()方法了
        mCallback = null;
    }

    public Callback<Result> getCallback() {
        return mCallback;
    }

    public interface Callback<Result> {
        /**
         * 当结果返回的时候执行
         *
         * @param result 返回的结果
         */
        void onPostExecute(Result result);

        /**
         * 当请求被取消的时候执行
         */
        void onTaskCancelled();
    }
}
