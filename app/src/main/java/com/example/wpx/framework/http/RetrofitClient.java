package com.example.wpx.framework.http;


import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.wpx.framework.app.App;
import com.example.wpx.framework.http.apiService.BaseApiService;
import com.example.wpx.framework.http.config.CacheInterceptor;
import com.example.wpx.framework.http.config.CookieJarImp;
import com.example.wpx.framework.http.config.HeaderInterceptor;
import com.example.wpx.framework.http.observer.BaseObserver;
import com.example.wpx.framework.http.observer.DownLoadListener;
import com.example.wpx.framework.http.observer.GetOrPostListener;
import com.example.wpx.framework.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h3>Retrofit网络请求</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class RetrofitClient {

    //Retrofit
    private Retrofit retrofit;
    //API服务
    private BaseApiService apiService;
    //缓存文件目录
    private File httpCacheDirectory;
    //ohHttp3缓存
    private Cache cache;
    //网络超时限制
    private static final int DEFAULT_TIMEOUT = 20;
    //默认根URL 测试 get post表单
    private static final String baseUrl = "http://112.124.22.238:8081/course_api/wares/hot/";//BaseUrl一定要以"/"结尾 真他么坑
    //单例RetrofitClient
    private static RetrofitClient instance;

    //定义一个共有的静态方法，返回该类型实例
    public static RetrofitClient getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (RetrofitClient.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    private RetrofitClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImp(App.getContext()))//配置Cookie
                .cache(getCache())//配置缓存文件
                .addInterceptor(new HeaderInterceptor(null))//配置请求头
                .addNetworkInterceptor(new CacheInterceptor(App.getContext()))//配置网络缓存
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//配置连接超时
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//配置读数据超时
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//配置写数据超时
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        apiService = retrofit.create(BaseApiService.class);

    }

    /**
     * 初始化http缓存文件
     *
     * @return
     */
    private Cache getCache() {
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(App.getContext().getCacheDir(), "http_cache");
        }
        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        return cache;
    }

    /**
     * get请求
     *
     * @param method
     * @param parameters
     * @param tClass
     * @param listener
     * @param <T>
     */
    public <T> void get(String method, Map<String, String> parameters, Context context, boolean isShow, Class<T> tClass, GetOrPostListener<T> listener) {
        apiService.executeGet(method, parameters)
                .compose(switchThread())
                .subscribe(new BaseObserver<ResponseBody>(context, isShow) {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        String content = "";
                        try {
                            content = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.e_Throwable(e);
                        }
                        LogUtil.e("ResponseBody", content);
                        listener.onSuccess(JSON.parseObject(content, tClass));
                    }
                });
    }

    /**
     * get请求
     *
     * @param method
     * @param parameters
     * @param tClass
     * @param listener
     * @param <T>
     */
    public <T> void get(String method, Map<String, String> parameters, Context context, Class<T> tClass, GetOrPostListener<T> listener) {
        this.get(method, parameters, context, false, tClass, listener);
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @param listener
     */
    public void downLoadFile(String fileUrl, String saveFilePath, DownLoadListener listener) {
        apiService.downloadFile(fileUrl)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        try {
                            long fileSize = responseBody.contentLength();
                            InputStream is = responseBody.byteStream();
                            File apkFile = new File(saveFilePath);
                            FileOutputStream fos = new FileOutputStream(apkFile);
                            byte buf[] = new byte[1024];
                            long count = 0;
                            int tempLen = 0;
                            while ((tempLen = is.read(buf)) != -1) {
                                count += tempLen;
                                if (listener != null) {
                                    listener.onProgress(count, fileSize);//文件下载进度监听
                                }
                                fos.write(buf, 0, tempLen);
                            }
                            if (tempLen == -1) {
                                if (listener != null) {
                                    listener.onDownLoadOver(apkFile);//文件下载完成监听
                                }
                            }
                            fos.close();
                            is.close();
                            return apkFile;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .compose(switchThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull File file) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * post请求
     *
     * @param method
     * @param parameters
     * @param tClass
     * @param listener
     * @param <T>
     */
    public <T> void post(String method, Map<String, String> parameters, Context context, boolean isShow, Class<T> tClass, GetOrPostListener<T> listener) {
        apiService.executePost(method, parameters)
                .compose(switchThread())
                .subscribe(new BaseObserver<ResponseBody>(context, isShow) {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        String content = "";
                        try {
                            content = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.e_Throwable(e);
                        }
                        LogUtil.e("ResponseBody", content);
                        listener.onSuccess(JSON.parseObject(content, tClass));
                    }
                });
    }

    /**
     * post请求
     *
     * @param method
     * @param parameters
     * @param tClass
     * @param listener
     * @param <T>
     */
    public <T> void post(String method, Map<String, String> parameters, Context context, Class<T> tClass, GetOrPostListener<T> listener) {
        this.post(method, parameters, context, false, tClass, listener);
    }


    /**
     * post提交json字符串
     *
     * @param method
     * @param context
     * @param isShow
     * @param s
     * @param tClass
     * @param listener
     * @param <S>
     * @param <R>
     */
    public <S, R> void json(String method, Context context, boolean isShow, S s, Class<R> tClass, GetOrPostListener<R> listener) {
        String jsonStr = JSON.toJSONString(s);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        apiService.executeJson(method, requestBody)
                .compose(switchThread())
                .subscribe(new BaseObserver<ResponseBody>(context, isShow) {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        String content = "";
                        try {
                            content = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.e_Throwable(e);
                        }
                        LogUtil.e("ResponseBody", content);
                        listener.onSuccess(JSON.parseObject(content, tClass));
                    }
                });
    }

    /**
     * post提交json字符串
     *
     * @param method
     * @param context
     * @param s
     * @param tClass
     * @param listener
     * @param <S>
     * @param <R>
     */
    public <S, R> void json(String method, Context context, S input, Class<R> tClass, GetOrPostListener<R> listener) {
        String jsonStr = JSON.toJSONString(input);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        apiService.executeJson(method, requestBody)
                .compose(switchThread())
                .subscribe(new BaseObserver<ResponseBody>(context) {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        String content = "";
                        try {
                            content = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.e_Throwable(e);
                        }
                        LogUtil.e("ResponseBody", content);
                        listener.onSuccess(JSON.parseObject(content, tClass));
                    }
                });
    }


    /**
     * 组合Rx线程切换封装
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> switchThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
