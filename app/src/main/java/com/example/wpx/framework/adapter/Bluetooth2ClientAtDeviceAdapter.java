package com.example.wpx.framework.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wpx.framework.R;

import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/23 17:15
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class Bluetooth2ClientAtDeviceAdapter extends AbsListViewAdapter<BluetoothDevice> {
    public Bluetooth2ClientAtDeviceAdapter(Context context, List<BluetoothDevice> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewParent) {
        BluetoothDeviceHolder holder;
        if (convertView == null) {
            holder = new BluetoothDeviceHolder();
            convertView = View.inflate(viewParent.getContext(), R.layout.item_bluetoothdevice, null);
            holder.txt_DeviceName = (TextView) convertView.findViewById(R.id.txt_DeviceName);
            holder.txt_DeviceAdd = (TextView) convertView.findViewById(R.id.txt_DeviceAdd);
            convertView.setTag(holder);
        } else {
            holder = (BluetoothDeviceHolder) convertView.getTag();
        }
        //设置Item数据
        holder.txt_DeviceName.setText(list.get(position).getName());
        holder.txt_DeviceAdd.setText(list.get(position).getAddress());
        return convertView;
    }

    public static class BluetoothDeviceHolder {
        public TextView txt_DeviceName;
        public TextView txt_DeviceAdd;
    }
}
