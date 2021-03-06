package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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
import com.example.wpx.framework.ui.presenter.Bluetooth2ServerAtPersenter;
import com.example.wpx.framework.ui.view.IBluetooth2ServerAtView;
import com.example.wpx.framework.util.ByteConvertUtil;
import com.example.wpx.framework.util.HandlerUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.otherutil.ToastUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 18:25
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class Bluetooth2ServerActivity extends BaseActivity<IBluetooth2ServerAtView, Bluetooth2ServerAtPersenter> {

    private static TextView txt_Buffer;
    private Button btn_Send;
    private EditText edt_Content;

    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread;
    private ServerThread serverThread;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERY = 2;
    private static final String NAME = "wpx";
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandlerMsgConfig.BLUETOOTH_CLIENT_RECEIVEDATA:
                    byte[] data = (byte[]) msg.obj;
                    txt_Buffer.append("收到客户端数据:" + ByteConvertUtil.bytesToHexString(data) + "\n");
                    int offset = txt_Buffer.getLineCount() * txt_Buffer.getLineHeight();
                    if (offset > txt_Buffer.getHeight()) {
                        txt_Buffer.scrollTo(0, offset - txt_Buffer.getHeight());
                    }
                    break;
            }
        }
    };

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
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                String stateStr = "";
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        stateStr = "蓝牙正在打开...";
                        break;
                    case BluetoothAdapter.STATE_ON:
                        stateStr = "蓝牙已打开";
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        stateStr = "蓝牙正在关闭...";
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        stateStr = "蓝牙已关闭";
                        break;
                }
                LogUtil.e(String.format("蓝牙状态变化: %s", stateStr));
                break;
            case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                int preScanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0);
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                LogUtil.e(String.format("扫描模式改变：%s => %s", scanModeToString(preScanMode), scanModeToString(scanMode)));
                break;
        }
    }

    private String scanModeToString(int scanMode) {
        String str = "未知";
        switch (scanMode) {
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                str = "SCAN_MODE_CONNECTABLE_DISCOVERABLE";
                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                str = "SCAN_MODE_CONNECTABLE";
                break;
            case BluetoothAdapter.SCAN_MODE_NONE:
                str = "SCAN_MODE_NONE";
                break;
        }
        return str;
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
            public void onClick(View v) {
                ServerThread.write(edt_Content.getText().toString().getBytes());
            }
        });
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {
        initBluetooth();
        startAcceptThread();
        enableBeDiscovery();
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
    }

    private void startAcceptThread() {
        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    /**
     * 默认情况下，设备将变为可检测到并持续 120 秒钟。 您可以通过添加 EXTRA_DISCOVERABLE_DURATION Intent Extra 来定义不同的持续时间。 应用可以设置的最大持续时间为 3600 秒，值为 0 则表示设备始终可检测到。 任何小于 0 或大于 3600 的值都会自动设为 120 秒。 例如，以下片段会将持续时间设为 300 秒：
     */
    private void enableBeDiscovery() {
        int MAX = 300;
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, MAX);
        startActivityForResult(discoverableIntent, REQUEST_DISCOVERY);
        ToastUtils.showShortToast(this, String.format("开启蓝牙可见 %s秒", MAX));
    }

    private class AcceptThread extends Thread {

        private BluetoothServerSocket ServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tempServerSocket = null;
            try {
                tempServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, SPP_UUID);
            } catch (IOException e) {
            }
            ServerSocket = tempServerSocket;
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = ServerSocket.accept();
                } catch (IOException e) {
                    LogUtil.e_Throwable(e);
                    break;
                }
                if (socket != null) {
                    manageConnectedSocket(socket);
                    cancel();
                    break;
                }
            }
        }

        public void cancel() {
            try {
                ServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e_Throwable(e);
            }
        }
    }


    private void manageConnectedSocket(BluetoothSocket socket) {
        serverThread = new ServerThread(socket);
        serverThread.start();
    }

    /**
     * 服务端线程
     */
    private static class ServerThread extends Thread {

        BluetoothSocket socket;

        public ServerThread(BluetoothSocket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                while (true) {
                    if (bis.available() > 0) {
                        byte[] buffer = new byte[1024];
                        int len = bis.read(buffer);
                        byte[] data = new byte[len];
                        for (int i = 0; i < len; i++) {
                            data[i] = buffer[i];
                        }
                        LogUtil.e("收到客户端数据:" + ByteConvertUtil.bytesToHexString(data));
                        HandlerUtil.sendMessage(handler, HandlerMsgConfig.BLUETOOTH_CLIENT_RECEIVEDATA, data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e_Throwable(e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e_Throwable(e);
                }
            }
        }

        public static void write(byte[] bytes) {
            try {
                LogUtil.e("服务端写出数据:" + ByteConvertUtil.bytesToHexString(bytes));
                BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                bos.write(bytes);
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

