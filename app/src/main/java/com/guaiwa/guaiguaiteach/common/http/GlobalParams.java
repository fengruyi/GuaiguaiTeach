package com.guaiwa.guaiguaiteach.common.http;


import android.text.TextUtils;


import com.guaiwa.guaiguaiteach.common.utils.TimeSynCheck;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;


/**
 * Created by 80151689 on 2016-11-25.
 * 请求固定参数
 */
public class GlobalParams {


    private static String encriptKey;

    /**
     * Get方法加上全局参数
     *
     * @param builder
     * @return
     */
    public static HttpUrl addGlobalParams(HttpUrl.Builder builder) {
        builder.addQueryParameter("key", "value");
        return builder.build();
    }


    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     */
    public static String getSignature(String data, String key) {
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] rawHmac = mac.doFinal(data.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(byteToHexString(b));
        }
        return sb.toString();
    }

    private static String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0f];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }


    private static String getKey() {
        if (TextUtils.isEmpty(encriptKey)) {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append(getKey1()).append(getKey2()).append(getKey3()).toString();
        }
        return encriptKey;
    }

    private static String getKey1() {
        return "";
    }

    private static String getKey2() {
        return "";
    }

    private static String getKey3() {
        return "";
    }

    public static long getServerTime() {
        return TimeSynCheck.getInstance().getServiceTime();
    }

    /**
     * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<
     *
     * @param paraMap 要排序的Map对象
     * @return
     */
    public static String getSortParams(Map<String, String> paraMap) {
        String buff;
        Map<String, String> tmpMap = paraMap;
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            buf.append(item.getKey() + "=" + item.getValue());
            buf.append("&");
        }
        buff = buf.toString();
        if (buff.isEmpty() == false) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }
}
