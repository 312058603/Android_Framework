package com.example.wpx.framework.util;

import android.os.Handler;
import android.os.Message;


public class HandlerUtil {

    /**
     * 发送消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:34
     * <h3>UpdateTime</h3> 16/3/23,15:34
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     */
    public static void sendMessage(Handler handler, int what) {
        Message message = handler.obtainMessage();
        message.what = what;
        handler.sendMessage(message);
    }

    /**
     * 发送延时消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:34
     * <h3>UpdateTime</h3> 16/3/23,15:34
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     */
    public static void sendMessageDelayed(Handler handler, int what, long delayMillis) {
        Message message = handler.obtainMessage();
        message.what = what;
        handler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * 发送消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:34
     * <h3>UpdateTime</h3> 16/3/23,15:34
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     * @param object  消息附带的对象.
     */
    public static void sendMessage(Handler handler, int what, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }


    /**
     * 发送消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:35
     * <h3>UpdateTime</h3> 16/3/23,15:35
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     * @param arg1    消息附带的信息1
     * @param object  消息附带的对象.
     */
    public static void sendMessage(Handler handler, int what, int arg1, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = object;
        message.arg1 = arg1;
        handler.sendMessage(message);
    }

    /**
     * 发送消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:35
     * <h3>UpdateTime</h3> 16/3/23,15:35
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     * @param arg1    消息附带的信息1
     * @param arg2    消息附带的信息2
     */
    public static void sendMessage(Handler handler, int what, int arg1, int arg2) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        handler.sendMessage(message);
    }

    /**
     * 发送消息
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/23,15:35
     * <h3>UpdateTime</h3> 16/3/23,15:35
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param handler handler对象
     * @param what    消息类型
     * @param arg1    消息附带的信息1
     * @param arg2    消息附带的信息2
     * @param object  消息附带的对象.
     */
    public static void sendMessage(Handler handler, int what, int arg1, int arg2, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = object;
        handler.sendMessage(message);
    }


}
