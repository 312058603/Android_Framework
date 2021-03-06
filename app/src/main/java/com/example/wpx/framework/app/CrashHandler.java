package com.example.wpx.framework.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.wpx.framework.util.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <h3>异常信息处理</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static Thread.UncaughtExceptionHandler defaultHandler = null;

    private Context context = null;

    private final String TAG = CrashHandler.class.getSimpleName();

    public CrashHandler(Context context) {
        this.context = context;
    }

    /**
     * 初始化,设置该CrashHandler为程序的默认处理器
     */
    public static void init(CrashHandler crashHandler) {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //TODO 将报错信息上传到服务器
        LogUtil.e("Exception",ex.toString());
        LogUtil.e("Exception",collectCrashDeviceInfo());
        LogUtil.e("Exception",getCrashInfo(ex));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ex.toString());
        stringBuffer.append(collectCrashDeviceInfo());
        stringBuffer.append(getCrashInfo(ex));
        // 调用系统错误机制
        defaultHandler.uncaughtException(thread, ex);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 得到程序崩溃的详细信息
     */
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.setStackTrace(ex.getStackTrace());
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    public String collectCrashDeviceInfo() {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = pi.versionName;
            String model = android.os.Build.MODEL;
            String androidVersion = android.os.Build.VERSION.RELEASE;
            String manufacturer = android.os.Build.MANUFACTURER;
            return versionName + "  " + model + "  " + androidVersion + "  " + manufacturer;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
