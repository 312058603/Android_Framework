package com.example.wpx.framework.util;


import android.util.Log;

import com.gmcx.safetyproject.configs.App;
import com.gmcx.safetyproject.utils.thridutil.SDCardUtils;

import org.apache.log4j.Logger;

/**
 * <h3>打印调试信息</h3>
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2016/11/21 15:19
 * <h3>Copyright</h3> Copyright (c)2016 Shenzhen GuoMaiChangXing Information Technology Co., Ltd. Inc. All rights reserved.
 */
public class LogUtil {

    private static final boolean isDebug = true;
    private static final String TAG = "mylog";
    //是否写日志到文件
    private static final boolean isFileDebug = false;

    private static int LOG_MAXLENGTH = 2000;

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void long_e(String tag, String msg) {
        if (isDebug) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.e(tag + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(tag, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void writeToFile(final Object txt) {
        if (isFileDebug) {
            if(SDCardUtils.isSDCardEnable()){
                final Logger log = Logger.getLogger(App.getInstance().getPackageName());
                log.debug(txt);
            }
        }
    }
}
