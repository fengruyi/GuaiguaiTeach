package com.guaiwa.guaiguaiteach.base;

/**
 * Created by 80151689 on 2017-10-17.
 * server data construct
 *
 */

public class ResultData<T> {

    public int code;  // 错误码
    public String message;  // 返回信息
    public T data;  // 包装的对象

}
