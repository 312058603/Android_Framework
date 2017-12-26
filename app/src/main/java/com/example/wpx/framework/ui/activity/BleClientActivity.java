package com.example.wpx.framework.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.wpx.framework.R;
import com.example.wpx.framework.adapter.BleClientAtDeviceAdapter;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.BleClientAtPresenter;
import com.example.wpx.framework.ui.view.IBleClientAtView;
import com.example.wpx.framework.util.IntentUtil;
import com.example.wpx.framework.util.LogUtil;
import com.example.wpx.framework.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 16:14
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class BleClientActivity extends BaseActivity<IBleClientAtView, BleClientAtPresenter> implements View.OnClickListener, AdapterView.OnItemClickListener, BluetoothAdapter.LeScanCallback {

    private Button btn_Search;
    private ListView lv_Devices;

    private BluetoothAdapter bluetoothAdapter;
    private BleClientAtDeviceAdapter bleClientAtDeviceAdapter;
    private LeScanCallbackImp leScanCallbackImp;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();

    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected BleClientAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bleclient;
    }

    @Override
    protected void addFilters() {
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
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

    }

    @Override
    protected void initData() {
        initBle();
        bleClientAtDeviceAdapter = new BleClientAtDeviceAdapter(this, bluetoothDeviceList);
        lv_Devices.setAdapter(bleClientAtDeviceAdapter);
        leScanCallbackImp=new LeScanCallbackImp();
    }


    private void initBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.showShort("不支持BLE");
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
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Search:
                bluetoothDeviceList.clear();
                bluetoothAdapter.startLeScan(leScanCallbackImp);
                break;
        }
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        LogUtil.e("蓝牙名称:"+bluetoothDevice.getName()+",蓝牙地址:"+bluetoothDevice.getAddress());
        if(!bluetoothDeviceList.contains(bluetoothDevice)){
            bleClientAtDeviceAdapter.addOne(bluetoothDevice);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice bluetoothDevice=bluetoothDeviceList.get(position);
        Bundle bundle=new Bundle();
        bundle.putParcelable("bluetoothDevice",bluetoothDevice);
        stopScan();
        IntentUtil.startActivity(this,BleClientDataActivity.class,bundle);
    }

    /**
     * 停止扫描蓝牙
     */
    public void stopScan() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bluetoothAdapter.stopLeScan(leScanCallbackImp);
            }
        }
    }

    /**
     * 扫描回调
     */
    private class LeScanCallbackImp implements BluetoothAdapter.LeScanCallback{

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            LogUtil.e("蓝牙名称:"+bluetoothDevice.getName()+",蓝牙地址:"+bluetoothDevice.getAddress());
            if(!bluetoothDeviceList.contains(bluetoothDevice)){
                bleClientAtDeviceAdapter.addOne(bluetoothDevice);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScan();
    }
}
