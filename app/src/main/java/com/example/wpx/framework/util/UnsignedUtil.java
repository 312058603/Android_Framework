package com.example.wpx.framework.util;

/**
 * <h3>无符号数据转换工具</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class UnsignedUtil {

    public static int getUnsignedByte(byte data) {
        return data & 0x0FF;
    }

    public static int getUnsignedShort(short data) {
        return data & 0x0FFFF;
    }

    public static long getUnsignedInt(int data) {
        return data & 0x0FFFFFFFFl;
    }

}
