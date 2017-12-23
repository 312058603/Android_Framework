package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.adapter.Bluetooth2ClientAtDeviceAdapter;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.Bluetooth2ClientAtPresenter;
import com.example.wpx.framework.ui.presenter.Bluetooth2ClientDataAtPresenter;
import com.example.wpx.framework.ui.view.IBluetooth2ClientAtView;
import com.example.wpx.framework.util.IntentUtil;
import com.example.wpx.framework.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 16:55
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class Bluetooth2ClientActivity extends BaseActivity<IBluetooth2ClientAtView, Bluetooth2ClientAtPresenter> implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btn_Search;
    private ListView lv_Devices;

    private BluetoothAdapter bluetoothAdapter;
    private Bluetooth2ClientAtDeviceAdapter bluetoothDeviceAdapter;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();


    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected Bluetooth2ClientAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_blue2client;
    }

    @Override
    protected void addFilters() {
        filter.addAction(BluetoothDevice.ACTION_FOUND);
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtil.e("蓝牙名称:"+bluetoothDevice.getName()+",蓝牙地址:"+bluetoothDevice.getAddress());
                if(!bluetoothDeviceList.contains(bluetoothDevice)){
                    bluetoothDeviceAdapter.addOne(bluetoothDevice);
                }
                break;
        }
    }

    @Override
    protected void findView() {
        btn_Search = (Button) findViewById(R.id.btn_Search);
        lv_Devices = (ListView) findViewById(R.id.lv_Devices);
    }

    @Override
    protected void initListener() {
        btn_Search.setOnClickListener(this);
        lv_Devices.setOnItemClickListener(this);
    }

    @Override
    protected void initIntentData() {
        bluetoothDeviceAdapter = new Bluetooth2ClientAtDeviceAdapter(this, bluetoothDeviceList);
        lv_Devices.setAdapter(bluetoothDeviceAdapter);
    }

    @Override
    protected void initData() {
        initBluetooth();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Search:
                bluetoothDeviceList.clear();
                bluetoothAdapter.startDiscovery();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BluetoothDevice bluetoothDevice=bluetoothDeviceList.get(position);
        Bundle bundle=new Bundle();
        bundle.putParcelable("bluetoothDevice",bluetoothDevice);
        IntentUtil.startActivity(this,Bluetooth2ClientDataActivity.class,bundle);
    }

}
