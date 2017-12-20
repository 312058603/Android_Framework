package com.example.wpx.framework.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.example.wpx.framework.app.App;
import com.example.wpx.framework.http.ApiService.BaseApiService;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

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
    public BaseApiService apiService;
    //缓存文件目录
    private File httpCacheDirectory;
    //ohHttp3缓存
    private Cache cache;
    //网络超时限制
    private static final int DEFAULT_TIMEOUT = 20;
    //默认根URL
    private static final String baseUrl = "http://192.168.1.120:8066/api/";
    //单例RetrofitClient
    private static RetrofitClient mRetrofitClient;

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
                .cookieJar(new CookieJarImp(App.getContext()))
                .cache(getCache())
                .addInterceptor(new HeaderInterceptor(null))
                .addNetworkInterceptor(new CacheInterceptor(App.getContext()))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(BaseApiService.class);

    }


    /**
     * 初始化缓存
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
     * @param listener
     */
    public void get(String method, Map parameters, RequstLisenerImp listener) {
        apiService.executeGet(method, parameters)
                .compose(switchThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        listener.onSuccess(responseBody);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }

    /**
     * post请求
     *
     * @param method
     * @param parameters
     * @param listener
     */
    public void post(String method, Map parameters, RequstLisenerImp listener) {
        apiService.executePost(method, parameters)
                .compose(switchThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        listener.onSuccess(responseBody);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }


    /**
     * 组合Rx
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

    /**
     * 请求回调
     */
    public interface RequstLisener<T> {
        void onSubscribe(Disposable d);
        void Next(RequestBody requestBody, Class<T> tClass);
        void onError(Throwable e);
        void onComplete();
    }

    public abstract class RequstLisenerImp<T> implements RequstLisener<T> {
        private WeakReference<Context> context;
        private ProgressDialog dialig;
        private boolean isShow;

        public RequstLisenerImp(WeakReference<Context> context, boolean isShow) {
            this.context = context;
            this.isShow = isShow;
        }

        public void onSubscribe(Disposable d) {
            if (isShow) {
                if (dialig == null) {
                    dialig = new ProgressDialog(context.get());
                }
                dialig.show();
            }
        }

        public void Next(RequestBody requestBody, Class<T> tClass) {
            try {
                T t = JSON.parseObject(requestBody.toString(), tClass);
                onSuccess(t);
            } catch (Exception e) {
                onError(e);
            }
        }

        public abstract void onSuccess(T t);

        public void onError(Throwable e) {
            LogUtil.e_Throwable("onFailure", "网络请求异常", e);
            ToastUtils.showShortToast(App.getContext(),"网络请求异常");
        }

        public void onComplete() {
            if (isShow) {
                if (dialig != null && dialig.isShowing()) {
                    dialig.dismiss();
                }
            }
        }

    }


}
