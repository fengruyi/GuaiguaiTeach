package com.guaiwa.guaiguaiteach.base;


/**
 * Created by 80151689 on 2017-10-17.
 * mvp-p
 */

public abstract class BasePreSenter <T extends BaseAction> {
    protected T loadAction;


    public BasePreSenter(T loadAction) {
        this.loadAction = loadAction;
    }



    public abstract void onDestroy();
}
