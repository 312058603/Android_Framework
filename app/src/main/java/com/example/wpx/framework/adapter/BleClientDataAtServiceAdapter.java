package com.example.wpx.framework.adapter;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wpx.framework.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class BleClientDataAtServiceAdapter extends AbsListViewAdapter<BluetoothGattService> {

    public BleClientDataAtServiceAdapter(Context context, List<BluetoothGattService> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewParent) {
        BluetoothServiceHolder holder;
        if (convertView == null) {
            holder = new BluetoothServiceHolder();
            convertView = View.inflate(viewParent.getContext(), R.layout.item_bleservice, null);
            holder.txt_ServiceName = (TextView) convertView.findViewById(R.id.txt_ServiceName);
            holder.txt_ServiceUUID = (TextView) convertView.findViewById(R.id.txt_ServiceUUID);
            holder.txt_ServiceInstanceId = (TextView) convertView.findViewById(R.id.txt_ServiceInstanceId);
            holder.txt_ServiceType = (TextView) convertView.findViewById(R.id.txt_ServiceType);
            convertView.setTag(holder);
        } else {
            holder = (BluetoothServiceHolder) convertView.getTag();
        }
        //设置Item数据
        holder.txt_ServiceName.setText("Unkwon Service");
        holder.txt_ServiceUUID.setText(list.get(position).getUuid().toString());
        holder.txt_ServiceInstanceId.setText(list.get(position).getInstanceId()+"");
        holder.txt_ServiceType.setText(list.get(position).getType()+"");
        return convertView;
    }

    public static class BluetoothServiceHolder {
        public TextView txt_ServiceName;
        public TextView txt_ServiceUUID;
        public TextView txt_ServiceInstanceId;
        public TextView txt_ServiceType;
    }

}
