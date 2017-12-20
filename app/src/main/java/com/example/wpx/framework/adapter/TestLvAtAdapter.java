package com.example.wpx.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wpx.framework.R;
import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/20 13:22
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class TestLvAtAdapter extends AbsListViewAdapter<String>{

    public TestLvAtAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewParent) {
        TestLvItemHolder holder;
        if (convertView == null) {
            holder = new TestLvItemHolder();
            convertView = View.inflate(viewParent.getContext(), R.layout.item_testlv, null);
            holder.testLvItem_content = (TextView) convertView.findViewById(R.id.testLvItem_content);
            convertView.setTag(holder);
        } else {
            holder = (TestLvItemHolder) convertView.getTag();
        }
        //设置Item数据
        holder.testLvItem_content.setText(list.get(position));
        return convertView;
    }

    public static class TestLvItemHolder{
        public TextView testLvItem_content;
    }

}
