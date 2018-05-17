package com.example.wpx.framework.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.wpx.framework.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseFragmentActivity<V, T extends BaseFragmentPresenter<V>> extends FragmentActivity {

    protected BroadcastReceiver mReceiver;

    protected IntentFilter mFilter;

    protected List<String> mActionList = new ArrayList<>();

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        init();
        initReceiver();
        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
        //子类不再需要设置布局ID，也不再需要使用ButterKnife.bind()
        setContentView(getContentViewId());
        findView();
        initIntentData();
        initData();
        initListener();
    }


    //在setContentView()调用之前调用，可以设置WindowFeature(如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
    protected void init() {
    }

    private void initReceiver() {
        mFilter = new IntentFilter();
        initActions();
        addFilter(mActionList);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseFragmentActivity.this.onReceive(context, intent);
            }
        };
        registerReceiver(mReceiver, mFilter);
    }

    private void addFilter(List<String> actionList) {
        if (mActionList != null && mActionList.size() > 0) {
            for (String action : actionList) {
                mFilter.addAction(action);
            }
        }
    }

    protected void initActions() {

    }

    protected void onReceive(Context context, Intent intent) {

    }

    protected void findView() {
    }

    protected void initIntentData() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int getContentViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        App.activities.remove(this);

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

    }

}
