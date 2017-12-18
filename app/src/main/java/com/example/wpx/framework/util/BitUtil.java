package com.example.wpx.framework.util;

/**
 * Created by Administrator on 2017/12/17 0017.
 */
public class BitUtil {

    /**
     * 一个2字节数据某一位置1操作
     *
     * @param data
     * @param position
     */
    public static int setBit1(int data, int position) {
        return data = data | (1 << position);
    }

    /**
     * 一个4字节数据某一位置1操作
     *
     * @param data
     * @param position
     */
    public static long setBit1(long data, int position) {
        return data = data | (1 << position);
    }

    /**
     * 给一个2字节数据某一位置0操作
     *
     * @param data
     * @param position
     */
    public static int setBit0(int data, int position) {
        return data = data &= ~(1 << position);
    }

    /**
     * 给一个4字节数据某一位置0操作
     *
     * @param data
     * @param position
     */
    public static long setBit0(long data, int position) {
        return data = data &= ~(1 << position);
    }

    /**
     * 判断某一位是否置位1
     *
     * @param data
     * @param position
     * @return
     */
    public static boolean isBit1(long data, int position) {
        if ((data & (1 << position)) == 1 << position) {
            return true;
        }
        return false;
    }

}
