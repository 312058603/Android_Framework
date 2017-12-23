package com.example.wpx.framework.util;

import android.widget.Toast;
import com.example.wpx.framework.app.App;


/**
 * <h3>Toast工具</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/7/18 10:41
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class ToastUtil {

    public static void showLong(String msg){
        Toast.makeText(App.getContext(),msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg){
        Toast.makeText(App.getContext(),msg, Toast.LENGTH_SHORT).show();
    }

}
