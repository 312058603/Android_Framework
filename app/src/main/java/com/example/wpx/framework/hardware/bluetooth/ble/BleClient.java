package com.example.wpx.framework.hardware.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleClient {

    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService testBluetoothGattService;
    private BluetoothGattCharacteristic testBluetoothGattCharacteristic;

    private static final String TEST_DEVICENAME = "";
    private static final String TEST_SERVICEUUID = "";
    private static final String TEST_CHARACTERISTICUUID = "";

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

    public void init(Context context) throws InterruptedException {
        this.context = context;
        initBle();
    }

    /**
     * 初始化蓝牙
     */
    public void initBle() throws InterruptedException {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtils.showShortToastSafe(context, "不支持蓝牙");
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            ToastUtils.showShortToastSafe(context, "蓝牙初始化失败");
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Thread.sleep(3 & 1000);
    }

    /**
     * 开始扫描蓝牙
     *
     * @param scanCallback
     */
    public void startScan(BluetoothAdapter.LeScanCallback scanCallback) {
        LogUtil.e("蓝牙已打开");
        bluetoothAdapter.startLeScan(scanCallback);
    }

    /**
     * 停止扫描蓝牙
     */
    public void stopScan() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bluetoothAdapter.stopLeScan(scanCallback);
            }
        }
    }

    /**
     * 扫描回调
     */
    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            LogUtil.e("蓝牙名称:" + device.getName() + ",蓝牙地址:" + device.getAddress() + ",蓝牙信号:" + rssi);
            if (device.getName().equals(TEST_DEVICENAME)) {
                //停止扫描
                stopScan();
                bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
                if (bluetoothGatt.connect()) {
                    stopScan();
                } else {
                    LogUtil.e("蓝牙连接失败");
                }
            }
        }
    };

    /**
     * 连接回调
     */
    public BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

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
            switch (newState) {
                case BluetoothGatt.STATE_CONNECTED:
                    LogUtil.e("蓝牙已连接");
                    LogUtil.e("开始扫描服务");
                    gatt.discoverServices();
                    break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    LogUtil.e("蓝牙已断开");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            //获取感兴趣的特征
            testBluetoothGattCharacteristic = getCharacteristic(TEST_SERVICEUUID, TEST_CHARACTERISTICUUID);
            if (testBluetoothGattCharacteristic == null) {
                LogUtil.e("GattCharacteristic not initialized");
            }
            //对感兴趣的特征订阅
            bluetoothGatt.setCharacteristicNotification(testBluetoothGattCharacteristic, true);
            //手动读取特征数据
            bluetoothGatt.readCharacteristic(testBluetoothGattCharacteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            byte[] value = characteristic.getValue();
            LogUtil.e("onCharacteristicRead():" + ByteConvertUtil.bytesToHexString(value));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            byte[] value = characteristic.getValue();
            LogUtil.e("onCharacteristicWrite():" + ByteConvertUtil.bytesToHexString(value));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();
            LogUtil.e("onCharacteristicChanged():" + ByteConvertUtil.bytesToHexString(value));
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            LogUtil.e("onDescriptorRead():" + ByteConvertUtil.bytesToHexString(descriptor.getValue()));
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.e("onDescriptorWrite():" + ByteConvertUtil.bytesToHexString(descriptor.getValue()));
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            LogUtil.e("onReliableWriteCompleted():" + status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.e("onReadRemoteRssi():" + rssi);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            LogUtil.e("onReadRemoteRssi():mtu=" + mtu);
        }
    };

    /**
     * 获取服务
     *
     * @param serviceUUID
     * @return
     */
    public BluetoothGattService getService(String serviceUUID) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            LogUtil.e("BluetoothAdapter not initialized");
            return null;
        }
        return bluetoothGatt.getService(UUID.fromString(serviceUUID));
    }

    /**
     * 获取特征
     *
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    public BluetoothGattCharacteristic getCharacteristic(String serviceUUID, String characteristicUUID) {
        BluetoothGattService service = getService(serviceUUID);
        if (service == null) {
            LogUtil.e("Can not find 'BluetoothGattService");
            return null;
        }
        return service.getCharacteristic(UUID.fromString(serviceUUID));
    }

    /**
     * 手动读取特征数据
     *
     * @param characteristic
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (bluetoothAdapter != null || bluetoothGatt != null) {
            LogUtil.e("BluetoothAdapter not initialized");
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * 手动向终端写特征数据
     * @param data
     */
    public void write(byte[] data,BluetoothGattCharacteristic characteristic) {
        characteristic.setValue(data);
        bluetoothGatt.writeCharacteristic(characteristic);
    }

}
