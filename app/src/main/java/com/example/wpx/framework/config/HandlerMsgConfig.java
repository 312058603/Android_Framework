package com.example.wpx.framework.config;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class HandlerMsgConfig {
    //收到蓝牙服务端数据
    public static final int BLUETOOTH_SERVER_RECEIVEDATA = 1;
    //收到蓝牙客户端数据
    public static final int BLUETOOTH_CLIENT_RECEIVEDATA = 2;

    //获取Ble服务
    public static final int BLE_CLIENT_SENDDATA=1;
    public static final int BLE_CLIENT_GETRESPONSEDATA=2;

    public static final int BLE_SERVER_GETDATA=3;
    public static final int BLE_SERVER_RESPONSEDATA=4;
}