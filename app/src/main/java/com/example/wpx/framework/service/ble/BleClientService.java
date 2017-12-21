package com.example.wpx.framework.service.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.wpx.framework.app.App;
import com.example.wpx.framework.config.BroadcastFilterConfig;
import com.example.wpx.framework.service.base.BaseService;
import com.example.wpx.framework.util.IntentUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 11:57
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class BleClientService extends BaseService {

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void setFilterActions() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    ToastUtils.showShortToastSafe(App.getContext(), "不支持Ble蓝牙");
                }
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    mBluetoothAdapter = bluetoothManager.getAdapter();
                }
                if (mBluetoothAdapter == null) {
                    ToastUtils.showShortToastSafe(App.getContext(), "不支持Ble蓝牙 mBluetoothAdapter=null");
                    return;
                }
                //判断蓝牙是否打开
                if (!mBluetoothAdapter.isEnabled()) {
                    IntentUtil.sendBroadcast(App.getContext(), BroadcastFilterConfig.ACTION_OPENBLE);
                } else {
                    LogUtil.e("蓝牙已打开");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                            @Override
                            public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
                                LogUtil.e("蓝牙地址:" + bluetoothDevice.getAddress() + ",蓝牙名称:" + bluetoothDevice.getName());
                            }
                        });
                    }
                }
            }
        }).start();
    }


}
