package com.guaiwa.guaiguaiteach.config;

public class UrlConfig {

    public final static Env ENV = Env.RELEASE;

    public static final boolean DEBUG = true;


    public enum Env {
        RELEASE,            // 正式环境
        PRE,                // 预发布环境
        DEVTEST,            // 开发环境
        TEST;               // 测试环境

        public String host;

    }

    static {
        switch (ENV) {
            case PRE:
                ENV.host = "http://pretest.i.api.wanyol.com";

                break;

            case TEST:
                ENV.host = "http://test.comm.wanyol.com";

                break;

            case DEVTEST:
                ENV.host = "http://devtest.oppo.cn";
                break;

            case RELEASE:
            default:
                ENV.host = "https://www.oppo.cn";
                break;
        }
    }

}