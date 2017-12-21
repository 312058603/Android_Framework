package com.example.wpx.framework.hardware.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.example.wpx.framework.app.App;
import com.example.wpx.framework.config.BroadcastFilterConfig;
import com.example.wpx.framework.util.IntentUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class BleClient {

    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    private BleClient() {

    }

    private static BleClient instance;

    public static BleClient getInstance() {
        if (instance == null) {
            synchronized (BleClient.class) {
                if (instance == null) {
                    instance = new BleClient();
                }
            }
        }
        return instance;
    }

    public void initSdk(Context context) {
        this.context = context;
        initBle();
    }

    /**
     * 初始化Ble
     */
    public void initBle(){
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtils.showShortToastSafe(context, "不支持Ble蓝牙");
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (bluetoothAdapter == null) {
            ToastUtils.showShortToastSafe(context, "不支持Ble蓝牙 mBluetoothAdapter=null");
        }
    }

    /**
     * 开始扫描蓝牙
     * @param scanCallback
     */
    public void startScan(BluetoothAdapter.LeScanCallback scanCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断蓝牙是否打开
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                } else {
                    LogUtil.e("蓝牙已打开");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        bluetoothAdapter.startLeScan(scanCallback);
                    }
                }
            }
        }).start();
    }

    /**
     * 停止扫描蓝牙
     */
    public void stopScan(){
        if(bluetoothAdapter!=null && bluetoothAdapter.isEnabled()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bluetoothAdapter.stopLeScan(scanCallback);
            }
        }
    }

    /**
     * 扫描回调
     */
    private BluetoothAdapter.LeScanCallback scanCallback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(device.getName().equals("123456")){
                device.connectGatt(context,false,bluetoothGattCallback);
            }
        }
    };

    /**
     * 连接回调
     */
    private BluetoothGattCallback bluetoothGattCallback=new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState){
                case BluetoothGatt.STATE_CONNECTING:
                    LogUtil.e("蓝牙连接中");
                    break;
                case BluetoothGatt.STATE_CONNECTED:
                    LogUtil.e("蓝牙已连接");
                    break;
                case BluetoothGatt.STATE_DISCONNECTING:
                    LogUtil.e("蓝牙断开中");
                    break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    LogUtil.e("蓝牙已断开");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> list=gatt.getServices();
            for (BluetoothGattService service: list) {
                LogUtil.e("service=");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };

}
