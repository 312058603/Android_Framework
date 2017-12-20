package com.example.wpx.framework.http;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/4/28 12:44
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class CookieJarImp implements CookieJar {

    private static final String TAG = "NovateCookieManger";

    private Context mContext;

    private static PersistentCookieStore cookieStore;

    /**
     * Mandatory constructor for the NovateCookieManger
     */
    public CookieJarImp(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(mContext);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

}