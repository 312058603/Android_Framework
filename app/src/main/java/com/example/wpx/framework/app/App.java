package com.example.wpx.framework.app;

import com.example.wpx.framework.app.base.BaseApp;

import org.litepal.LitePal;

/**
 * <h3>App应用初始化</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class App extends BaseApp{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LitePal
        LitePal.initialize(this);
        //错误信息处理
        CrashHandler.init(new CrashHandler(App.getContext()));
    }

}