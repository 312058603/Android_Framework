package com.example.wpx.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.wpx.framework.R;


/**
 * Intent跳转工具类
 * <h3>Description</h3> 用于简化页面跳转
 * <h3>Author</h3> Str1ng
 * <h3>Date</h3> 16/3/14 16:32
 * <h3>Copyright</h3> Copyright (c) 2016 Shenzhen GuoMaiChangXing Information Technology Co., Ltd. Inc. All rights reserved.
 */
public class IntentUtil {
    /**
     * 跳转到下一个Activity并且不关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:34
     * <h3>UpdateTime</h3> 16/3/14,16:34
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 需要跳转的Activity
     */
    public static void startActivity(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 带数据跳转到下一个Activity并且不关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:34
     * <h3>UpdateTime</h3> 16/3/14,16:34
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 需要跳转的Activity
     * @param bundle    需要携带的数据
     */
    public static void startActivity(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 跳转到下一个Activity并关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:57
     * <h3>UpdateTime</h3> 16/3/14,16:57
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 需要跳转的Activity
     */
    public static void startActivityAndFinish(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 携带数据跳转到下一个Activity并关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,17:01
     * <h3>UpdateTime</h3> 16/3/14,17:01
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 要跳转的Activity
     * @param bundle    需要携带的数据
     */
    public static void startActivityAndFinish(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 打开一个Activity并且关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:35
     * <h3>UpdateTime</h3> 16/3/14,16:35
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 需要打开的Activity
     */
    public static void startActivityToTopAndFinish(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 带数据打开一个Activity并且关闭当前Activity
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:40
     * <h3>UpdateTime</h3> 16/3/14,16:40
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context   上下文
     * @param gotoClass 需要打开的Activity
     * @param bundle    需携带的数据
     */
    public static void startActivityToTopAndFinish(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 带返回数据的跳转
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:44
     * <h3>UpdateTime</h3> 16/3/14,16:44
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context     上下文
     * @param gotoClass   需要跳转的Activity
     * @param requestCode 请求码
     */
    public static void startActivityForResult(Context context, Class<?> gotoClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 带返回数据的跳转并携带数据
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/14,16:49
     * <h3>UpdateTime</h3> 16/3/14,16:49
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context     上下文
     * @param gotoClass   需要跳转的Activity
     * @param requestCode 请求码
     * @param bundle      携带的数据
     */
    public static void startActivityForResult(Context context, Class<?> gotoClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    public static void sendBroadcast(Context context, String strAction) {
        Intent intent = new Intent();
        intent.setAction(strAction);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String strAction, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(strAction);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }


}
