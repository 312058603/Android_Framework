package com.example.wpx.framework.http.Observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.wpx.framework.app.App;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/20 17:17
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private WeakReference<Context> context;
    private ProgressDialog dialig;
    private boolean isShow;

    public BaseObserver(Context context, boolean isShow) {
        this.context = new WeakReference<>(context);
        this.isShow = isShow;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (isShow) {
            if (dialig == null) {
                dialig = new ProgressDialog(context.get());
                dialig.setMessage("正在请求数据");
            }
            dialig.show();
        }
    }

    @Override
    public abstract void onNext(@NonNull T t);

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e_Throwable(e);
        ToastUtils.showShortToast(App.getContext(), "网络请求异常");
        closeDialog();
    }

    @Override
    public void onComplete() {
        closeDialog();
    }

    private void closeDialog() {
        if (isShow) {
            if (dialig != null && dialig.isShowing()) {
                dialig.dismiss();
            }
        }
    }

}
