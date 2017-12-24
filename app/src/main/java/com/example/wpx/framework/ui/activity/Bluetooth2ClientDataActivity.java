package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.config.HandlerMsgConfig;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.Bluetooth2ClientDataAtPresenter;
import com.example.wpx.framework.ui.view.IBluetooth2ClientDataAtView;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.HandlerUtil;
import com.example.wpx.framework.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

    private static TextView txt_Buffer;
    private Button btn_Send;
    private EditText edt_Content;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private ClientThread clientThread;

    private static BluetoothSocket bluetoothSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandlerMsgConfig.RECEIVE_BLUETOOTH_SERVER_DATA:
                    byte[] data = (byte[]) msg.obj;
                    txt_Buffer.append("收到服务端数据:" + ByteConvertUtil.bytesToHexString(data) + "\n");
                    int offset = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offset > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offset - txt_Buffer.getHeight());
                    }
                    break;
            }
        }
    };

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
        txt_Buffer.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_Send = (Button) findViewById(R.id.btn_Send);
        edt_Content = (EditText) findViewById(R.id.edt_Content);
    }

    @Override
    protected void initListener() {
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientThread.write(edt_Content.getText().toString().getBytes());
            }
        });
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

        private BluetoothDevice bluetoothDevice;

        public ConnectThread(BluetoothDevice device) {
            this.bluetoothDevice = device;
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            manageConnectedSocket();
        }
    }

    /**
     * 连接处理
     */
    private void manageConnectedSocket() {
        clientThread = new ClientThread();
        clientThread.start();
    }


    /**
     * 客户端线程
     */
    public static class ClientThread extends Thread {

        public ClientThread() {

        }

        public void run() {
            while (true) {
                try {
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    if (bis.available() > 0) {
                        byte[] buffer = new byte[1024];
                        int len = bis.read(buffer);
                        byte[] data = new byte[len];
                        for (int i = 0; i < len; i++) {
                            data[i] = buffer[i];
                        }
                        LogUtil.e("收到蓝牙服务端数据:" + ByteConvertUtil.bytesToHexString(data));
                        HandlerUtil.sendMessage(handler, HandlerMsgConfig.RECEIVE_BLUETOOTH_SERVER_DATA, data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void write(byte[] bytes) {
            try {
                LogUtil.e("客户端写出数据:" + ByteConvertUtil.bytesToHexString(bytes));
                BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                bos.write(bytes);
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
