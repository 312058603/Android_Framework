package com.example.wpx.framework.hardware.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;

import java.util.UUID;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 12:32
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleServer {

    private Context context;
    private BluetoothManager bluetoothManager;
    private BluetoothGattServer bluetoothGattServer;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic readAndWriteCharacteristic;

    private static final UUID UUID_SERVER = UUID.fromString("75BEB663-74FC-4871-9737-AD184157450E");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BleServer(Context context) {
        this.context = context;

    }
}
