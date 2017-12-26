package com.example.wpx.framework.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.example.wpx.framework.R;
import com.example.wpx.framework.app.App;
import com.example.wpx.framework.config.BluetoothUUIDConfig;
import com.example.wpx.framework.config.HandlerMsgConfig;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.BleServerAtPresenter;
import com.example.wpx.framework.ui.view.IBleServerAtView;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.HandlerUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.ToastUtil;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class BleServerActivity extends BaseActivity<IBleServerAtView,BleServerAtPresenter>{

    private TextView txt_Buffer;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGattServer bluetoothGattServer;
    private BluetoothGattCharacteristic readCharacteristic;

    private static final int REQUEST_ENABLE_BT = 1;

    private MyHandler handler;
    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case HandlerMsgConfig.BLE_SERVER_GETDATA:
                    byte[] clientData = (byte[]) msg.obj;
                    txt_Buffer.append("收到客户端数据:" + ByteConvertUtil.bytesToHexString(clientData) + "\n");
                    int offsetChangeData = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offsetChangeData > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offsetChangeData - txt_Buffer.getHeight());
                    }
                    break;
                case HandlerMsgConfig.BLE_SERVER_RESPONSEDATA:
                    byte[] responseData = (byte[]) msg.obj;
                    txt_Buffer.append("响应客户端数据:" + ByteConvertUtil.bytesToHexString(responseData) + "\n");
                    int offsetWriteData = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offsetWriteData > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offsetWriteData - txt_Buffer.getHeight());
                    }
                    break;
            }
        }
    }

    @Override
    protected BleServerAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bleserver;
    }

    @Override
    protected void addFilters() {

    }

    @Override
    protected void onReceive(Context context, Intent intent) {

    }

    @Override
    protected void findView() {
        txt_Buffer = (TextView) findViewById(R.id.txt_Buffer);
        txt_Buffer.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {
        handler=new MyHandler(this);
        initBle();
        initGATTServer();
    }

    private void initBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.showShort("不支持BLE");
            finish();
        }
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            ToastUtil.showShort("不支持蓝牙");
            finish();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void initGATTServer() {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .build();

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .build();

        AdvertiseData scanResponseData = new AdvertiseData.Builder()
                .addServiceUuid(new ParcelUuid(BluetoothUUIDConfig.uuidService))
                .setIncludeTxPowerLevel(true)
                .build();

        AdvertiseCallback callback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                LogUtil.e("BLE advertisement added successfully");
                initServices(App.getContext());
            }
            @Override
            public void onStartFailure(int errorCode) {
                LogUtil.e("Failed to add BLE advertisement, reason: " + errorCode);
            }
        };
        BluetoothLeAdvertiser bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        bluetoothLeAdvertiser.startAdvertising(settings, advertiseData, scanResponseData, callback);
    }

    private void initServices(Context context) {
        bluetoothGattServer = bluetoothManager.openGattServer(context, bluetoothGattServerCallback);
        BluetoothGattService service = new BluetoothGattService(BluetoothUUIDConfig.uuidService, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //add a read characteristic.
        readCharacteristic = new BluetoothGattCharacteristic(BluetoothUUIDConfig.uuidCharRead, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        //add a descriptor
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(BluetoothUUIDConfig.uuidDescriptor, BluetoothGattCharacteristic.PERMISSION_WRITE);
        readCharacteristic.addDescriptor(descriptor);
        service.addCharacteristic(readCharacteristic);

        //add a write characteristic.
        BluetoothGattCharacteristic characteristicWrite = new BluetoothGattCharacteristic(BluetoothUUIDConfig.uuidCharWrite, BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_WRITE);
        service.addCharacteristic(characteristicWrite);

        bluetoothGattServer.addService(service);
        LogUtil.e("initServices ok");
    }

    /**
     * 服务事件的回调
     */
    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {

        /**
         * 连接状态变化回调
         * @param device
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            LogUtil.e("onConnectionStateChange");
            LogUtil.e("连接蓝牙名称:"+device.getName()+",连接蓝牙地址:"+device.getAddress()+",status:"+status+",newState:"+newState);
        }

        /**
         * 服务添加回调
         * @param status
         * @param service
         */
        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            LogUtil.e("onServiceAdded");
        }

        /**
         * 特征收到读回调
         * @param device
         * @param requestId
         * @param offset
         * @param characteristic
         */
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            LogUtil.e("onCharacteristicReadRequest");
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        }

        /**
         * 特征收到写回调
         * @param device
         * @param requestId
         * @param characteristic
         * @param preparedWrite
         * @param responseNeeded
         * @param offset
         * @param requestBytes
         */
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] requestBytes) {
            LogUtil.e("onCharacteristicWriteRequest");
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, requestBytes);
            LogUtil.e("收到客户端数据:"+ByteConvertUtil.bytesToHexString(requestBytes));
            HandlerUtil.sendMessage(handler, HandlerMsgConfig.BLE_SERVER_GETDATA, requestBytes);
            readCharacteristic.setValue(requestBytes);
            LogUtil.e("响应客户端数据:"+ByteConvertUtil.bytesToHexString(requestBytes));
            HandlerUtil.sendMessage(handler, HandlerMsgConfig.BLE_SERVER_RESPONSEDATA,requestBytes);
            bluetoothGattServer.notifyCharacteristicChanged(device, readCharacteristic, false);
        }

        /**
         * 描述收到写回调
         * @param device
         * @param requestId
         * @param descriptor
         * @param preparedWrite
         * @param responseNeeded
         * @param offset
         * @param value
         */
        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            LogUtil.e("onDescriptorWriteRequest");
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
        }

        /**
         * 描述收到读回调
         * @param device
         * @param requestId
         * @param offset
         * @param descriptor
         */
        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            LogUtil.e("onDescriptorReadRequest");
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        /**
         * 调用notifyCharacteristicChanged回调
         * @param device
         * @param status
         */
        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            LogUtil.e("onNotificationSent");
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            LogUtil.e("onMtuChanged");
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            LogUtil.e("onExecuteWrite");
        }
    };

}
