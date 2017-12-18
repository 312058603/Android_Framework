package com.example.wpx.framework.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class RetrofitClient {

    private static RetrofitClient mRetrofitClient;

    private static class sinalInstance {
        public static final RetrofitClient instance = new RetrofitClient();
    }

    public  static RetrofitClient getInstance(){
        return sinalInstance.instance;
    }

    public RetrofitClient(){
        OkHttpClient client=new OkHttpClient();
        client.newBuilder()
              ;
    }

}
