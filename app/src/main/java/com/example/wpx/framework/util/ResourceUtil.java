package com.example.wpx.framework.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * 资源文件工具类
 * <h3>Description</h3>
 * <h3>Author</h3> Str1ng
 * <h3>Date</h3> 16/3/15 10:26
 * <h3>Copyright</h3> Copyright (c) 2016 Shenzhen GuoMaiChangXing Information Technology Co., Ltd. Inc. All rights reserved.
 */
public class ResourceUtil {
    /**
     * 获取String
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/15,10:30
     * <h3>UpdateTime</h3> 16/3/15,10:30
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     * @param context
     * @param id
     * @return
     */
    public static String getString(Context context, int id) {
        return getResource(context).getString(id);
    }

    /**
     * 获取String[]
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/15,10:31
     * <h3>UpdateTime</h3> 16/3/15,10:31
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     * @param context
     * @param id
     * @return
     */
    public static String[] getStringArray(Context context, int id) {
        return getResource(context).getStringArray(id);
    }

    /**
     * 获取Color
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 16/3/15,10:32
     * <h3>UpdateTime</h3> 16/3/15,10:32
     * <h3>CreateAuthor</h3> Str1ng
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     * @param context
     * @param id
     * @return
     */
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private static Resources getResource(Context context) {
        return context.getResources();
    }
}
