package com.example.wpx.framework.app;

import com.example.wpx.framework.app.base.BaseApp;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/12/17 0017.
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
