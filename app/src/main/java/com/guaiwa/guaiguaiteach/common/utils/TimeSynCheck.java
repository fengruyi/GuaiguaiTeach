package com.guaiwa.guaiguaiteach.common.utils;

import android.os.SystemClock;


/**
 * Created by 80151689 on 2017-09-28.
 * 与服务器时间同步校验
 */

public class TimeSynCheck {
    private static TimeSynCheck instance;
    private long differenceTime;        //以前服务器时间 - 以前服务器时间的获取时刻的系统启动时间
    private boolean isServerTime;       //是否是服务器时间

    private TimeSynCheck() {
    }

    public static TimeSynCheck getInstance() {
        if (instance == null) {
            synchronized (TimeSynCheck.class) {
                if (instance == null) {
                    instance = new TimeSynCheck();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前时间
     *
     * @return the time
     */
    public synchronized long getServiceTime() {
        long serverTime = SystemClock.elapsedRealtime() + differenceTime;
        if (!isServerTime || serverTime < 1506566462000L) {
            //todo 这里可以加上触发获取服务器时间操作
            return System.currentTimeMillis();
        }
        //时间差加上当前手机启动时间就是准确的服务器时间了
        return serverTime;
    }

    /**
     * 时间校准
     *
     * @param lastServiceTime 当前服务器时间
     * @return the long
     */
    public synchronized long initServerTime(long lastServiceTime) {
        //记录时间差
        differenceTime = lastServiceTime - SystemClock.elapsedRealtime();
        isServerTime = true;
        return lastServiceTime;
    }
}
