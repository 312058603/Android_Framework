package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import com.example.wpx.framework.config.BroadcastFilterConfig;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.BleTestAtPresenter;
import com.example.wpx.framework.ui.view.IBleTestAtView;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 16:14
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class BleTestActivity extends BaseActivity<IBleTestAtView, BleTestAtPresenter>{

    @Override
    protected BleTestAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected void addFilters() {
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {

    }
}
