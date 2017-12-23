package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.Bluetooth2ClientDataAtPresenter;
import com.example.wpx.framework.ui.view.IBluetooth2ClientDataAtView;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 17:40
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class Bluetooth2ClientDataActivity extends BaseActivity<IBluetooth2ClientDataAtView, Bluetooth2ClientDataAtPresenter> {

    private TextView txt_Buffer;
    private Button btn_Send;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private ConnectedThread connectedThread;

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected Bluetooth2ClientDataAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_blue2clientdata;
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
        btn_Send = (Button) findViewById(R.id.btn_Send);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        bluetoothDevice = bundle.getParcelable("bluetoothDevice");
    }

    @Override
    protected void initData() {
        initBluetooth();
    }

    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            LogUtil.e("不支持蓝牙");
        }
        //打开蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        connect(bluetoothDevice);
    }

    private void connect(BluetoothDevice bluetoothDevice) {
        ConnectThread connectThread = new ConnectThread(bluetoothDevice);
        connectThread.start();
    }

    /**
     * 连接线程
     */
    private class ConnectThread extends Thread {

        private BluetoothSocket bluetoothSocket;
        private BluetoothDevice bluetoothDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket bs = null;
            bluetoothDevice = device;
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
            }
            bluetoothSocket = bs;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                this.bluetoothSocket.connect();
            } catch (IOException connectException) {
                try {
                    this.bluetoothSocket.close();
                } catch (IOException closeException) {

                }
                return;
            }
            manageConnectedSocket(bluetoothSocket);
        }

        public void cancel() {
            try {
                this.bluetoothSocket.close();
            } catch (IOException e) {
            }
        }

    }

    /**
     * 连接处理
     *
     * @param mmSocket
     */
    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }


    /**
     * 已连接线程
     */
    private class ConnectedThread extends Thread {
        private BluetoothSocket bluetoothSocket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public ConnectedThread(BluetoothSocket bluetoothSocket) {
            this.bluetoothSocket = bluetoothSocket;
            InputStream is = null;
            OutputStream os = null;
            try {
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
            }
            inputStream = is;
            outputStream = os;
        }

        public void run() {
            while (true) {
                try {
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    if (bis.available() > 0) {
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        len = bis.read(buffer);
                        LogUtil.e("收到蓝牙服务端的数据:" + ByteConvertUtil.bytesToHexString(buffer));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            LogUtil.e("客户端写出数据: " + new String(bytes) + "\r\n");
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
            }
        }

    }


}
