package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.Bluetooth2ServerAtPersenter;
import com.example.wpx.framework.ui.view.IBluetooth2ServerAtView;
import com.example.wpx.framework.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 18:25
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class Bluetooth2ServerActivity extends BaseActivity<IBluetooth2ServerAtView, Bluetooth2ServerAtPersenter> {

    private TextView txt_Buffer;
    private Button btn_Send;

    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERY = 2;
    private static final String NAME = "ZYF-test";
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected Bluetooth2ServerAtPersenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_blue2serverdata;
    }

    @Override
    protected void addFilters() {
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                String stateStr = "未知";
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        stateStr = "STATE_TURNING_ON";
                        break;
                    case BluetoothAdapter.STATE_ON:
                        stateStr = "STATE_ON";
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        stateStr = "STATE_TURNING_OFF";
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        stateStr = "STATE_OFF";
                        break;
                }
                LogUtil.e(String.format("蓝牙状态变化: %s", stateStr));
                break;
        }
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

    }

    @Override
    protected void initData() {
        initBluetooth();
        startAcceptThread();
    }

    private void initBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            LogUtil.e("不支持蓝牙");
        }
        //打开蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void startAcceptThread(){
        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, SPP_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }


    ServerReadThread mServerReadThread;

    private void manageConnectedSocket(BluetoothSocket socket) {
        mServerReadThread = new ServerReadThread(socket);
        mServerReadThread.start();
    }

    private static final int MSG_SERVER_READ = 1;
    private static final int MSG_APPEND = 2;

    // 读取数据
    private class ServerReadThread extends Thread {
        BluetoothSocket mSocket;

        public ServerReadThread(BluetoothSocket socket) {
            this.mSocket = socket;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {
                is = mSocket.getInputStream();
                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {

                        byte[] result = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            result[i] = buffer[i];
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
