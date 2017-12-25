package com.example.wpx.framework.ui.activity;

import android.content.Context;
import android.content.Intent;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.BleServerAtPresenter;
import com.example.wpx.framework.ui.view.IBleServerAtView;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class BleServerActivity extends BaseActivity<IBleServerAtView,BleServerAtPresenter>{
    @Override
    protected BleServerAtPresenter createPresenter() {
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
