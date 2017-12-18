package com.example.wpx.framework.util;

/**
 * Created by Administrator on 2017/12/17 0017.
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
