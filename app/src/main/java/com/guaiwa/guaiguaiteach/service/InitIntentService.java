package com.guaiwa.guaiguaiteach.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class InitIntentService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InitIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void checkUpgrade(){

    }
}
