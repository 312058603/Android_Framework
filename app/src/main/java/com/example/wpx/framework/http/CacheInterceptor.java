package com.example.wpx.framework.http;

import android.content.Context;
import android.util.Log;

import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <h3>缓存拦截器配置</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/4/28 12:44
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class CacheInterceptor implements Interceptor {

    private Context context;

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //在有网络的情况下 设置缓存超时时间为60S
        if (NetworkUtils.isWifiAvailable(context)) {
            Response response = chain.proceed(request);
            // read from cache for 60 s
            int maxAge = 60;
            String cacheControl = request.cacheControl().toString();
            LogUtil.e("CacheRequest", "60s load cahe" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }
        //在没网络的情况下设置缓存超时时间为3天
        else {
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, "当前无网络! 为你智能加载缓存", Toast.LENGTH_SHORT).show();
//                }
//            });
            LogUtil.e("CacheRequest", " no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
