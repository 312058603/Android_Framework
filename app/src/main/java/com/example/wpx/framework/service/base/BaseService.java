package com.example.wpx.framework.service.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 15:34
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseService extends Service{

    protected BroadcastReceiver mReceiver;

    protected IntentFilter mFilter = new IntentFilter();

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
    }

    protected void registerReceiver() {
        setFilterActions();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseService.this.onReceive(context, intent);
            }
        };
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    public abstract void setFilterActions();

    public abstract void onReceive(Context context, Intent intent);


}
