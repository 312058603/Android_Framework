package com.example.wpx.framework.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.config.BluetoothUUIDConfig;
import com.example.wpx.framework.config.HandlerMsgConfig;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.BleClientDataAtPresenter;
import com.example.wpx.framework.ui.view.IBleClientDataAtView;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.HandlerUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.ToastUtil;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class BleClientDataActivity extends BaseActivity<IBleClientDataAtView, BleClientDataAtPresenter> implements View.OnClickListener {

    private TextView txt_Buffer;
    private Button btn_Send;
    private EditText edt_Content;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic readCharacteristic;
    private BluetoothGattCharacteristic writeCharacteristic;

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
                case HandlerMsgConfig.BLE_CLIENT_GETRESPONSEDATA:
                    byte[] responseData = (byte[]) msg.obj;
                    txt_Buffer.append("收到响应数据:" + ByteConvertUtil.bytesToHexString(responseData) + "\n");
                    int offsetChangeData = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offsetChangeData > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offsetChangeData - txt_Buffer.getHeight());
                    }
                    break;
                case HandlerMsgConfig.BLE_CLIENT_SENDDATA:
                    byte[] sendData = (byte[]) msg.obj;
                    txt_Buffer.append("客户端写出数据:" + ByteConvertUtil.bytesToHexString(sendData) + "\n");
                    int offsetWriteData = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offsetWriteData > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offsetWriteData - txt_Buffer.getHeight());
                    }
                    break;
            }
        }
    }

    @Override
    protected BleClientDataAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bleclientdata;
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
        btn_Send = (Button) findViewById(R.id.btn_Send);
        edt_Content = (EditText) findViewById(R.id.edt_Content);
    }

    @Override
    protected void initListener() {
        btn_Send.setOnClickListener(this);
    }

    @Override
    protected void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        bluetoothDevice = bundle.getParcelable("bluetoothDevice");
    }

    @Override
    protected void initData() {
        handler = new MyHandler(this);
        initBle();
    }

    private void initBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.showShort("不支持Ble");
            finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            ToastUtil.showShort("不支持蓝牙");
            finish();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            bluetoothGatt = bluetoothDevice.connectGatt(this, false, bluetoothGattCallback);
        }
    }

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
                    bluetoothGatt.discoverServices();
                    LogUtil.e("开始扫描服务");
                    break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    LogUtil.e("蓝牙已断开");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            LogUtil.e("onServicesDiscovered");
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                LogUtil.e("服务名字:" + "UnkownService");
                LogUtil.e("ServiceUUID:" + service.getUuid().toString());
                LogUtil.e("ServiceInstanceId:" + service.getInstanceId());
                LogUtil.e("ServiceType:" + service.getType());
            }
            BluetoothGattService service = gatt.getService(BluetoothUUIDConfig.uuidService);
            readCharacteristic = service.getCharacteristic(BluetoothUUIDConfig.uuidCharRead);
            writeCharacteristic = service.getCharacteristic(BluetoothUUIDConfig.uuidCharWrite);
            bluetoothGatt.setCharacteristicNotification(readCharacteristic, true);
            BluetoothGattDescriptor descriptor = readCharacteristic.getDescriptor(BluetoothUUIDConfig.uuidDescriptor);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            LogUtil.e("onCharacteristicRead");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            LogUtil.e("onCharacteristicWrite");
            LogUtil.e("客户端写出数据:"+ ByteConvertUtil.bytesToHexString(characteristic.getValue()));
            HandlerUtil.sendMessage(handler, HandlerMsgConfig.BLE_CLIENT_SENDDATA, characteristic.getValue());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            LogUtil.e("onCharacteristicChanged");
            LogUtil.e("收到服务端数据:"+ ByteConvertUtil.bytesToHexString(characteristic.getValue()));
            HandlerUtil.sendMessage(handler, HandlerMsgConfig.BLE_CLIENT_GETRESPONSEDATA, characteristic.getValue());
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            LogUtil.e("onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.e("onDescriptorWrite");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            LogUtil.e("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.e("onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            LogUtil.e("onMtuChanged");
        }
    };

    public void sendMsg(byte[] data){
        writeCharacteristic.setValue(data);
        writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        bluetoothGatt.writeCharacteristic(writeCharacteristic);
    }

    @Override
    public void onClick(View v) {
        sendMsg(edt_Content.getText().toString().getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothGatt.close();
    }
}
