package com.example.yy.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yy on 2017/10/1.
 */

public class HttpUtil {
    public static void sendHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
