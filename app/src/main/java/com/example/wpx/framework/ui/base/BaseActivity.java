package com.example.wpx.framework.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.wpx.framework.app.App;
import com.example.wpx.framework.app.base.BaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected BroadcastReceiver receiver;

    protected IntentFilter filter;

    protected List<String> actionList = new ArrayList<>();

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        //判断是否使用MVP模式
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
        //子类不再需要设置布局ID，也不再需要使用ButterKnife.bind()
        setContentViewBefore();
        setContentView(getContentViewId());
        initReceiver();
        findView();
        initListener();
        initIntentData();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        App.activities.add(this);
        //判断是否使用MVP模式
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
        //子类不再需要设置布局ID，也不再需要使用ButterKnife.bind()
        setContentViewBefore();
        setContentView(getContentViewId());
        initReceiver();
        findView();
        initListener();
        initIntentData();
        initData();
    }

    //在setContentView()调用之前调用，可以设置WindowFeature(如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
    protected void setContentViewBefore() {
    }

    private void initReceiver() {
        filter = new IntentFilter();
        addFilters();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseActivity.this.onReceive(context, intent);
            }
        };
        registerReceiver(receiver, filter);
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int getContentViewId();

    protected abstract void addFilters();

    protected abstract void onReceive(Context context, Intent intent);

    protected abstract void findView();

    protected abstract void initListener();

    protected abstract void initIntentData();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除对应的activity
        App.activities.remove(this);

        if (presenter != null) {
            presenter.detachView();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

    }

}
