package com.example.wpx.framework.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * <h3>Description</h3>
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2016/11/25 22:15
 * <h3>Copyright</h3> Copyright (c)2016 Shenzhen GuoMaiChangXing Information Technology Co., Ltd. Inc. All rights reserved.
 */
public  abstract class AbsListViewAdapter<T> extends BaseAdapter {

    protected List<T> list;
    protected Context context;

    public AbsListViewAdapter(Context context, List<T> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置数据源
     * @return
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 获取数据源
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * 添加一条数据
     * @param t
     */
    public void addOne(T t){
        list.add(t);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集合
     * @param arrayList
     */
    public void addList(List<T> arrayList){
        list.addAll(arrayList);
        notifyDataSetChanged();
    }

    /**
     * 移除一条数据(下标删除)
     * @param position
     */
    public void removeOne(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除一条数据(对象删除)
     * 注意对象必须实现toString方法 否则删除不掉
     * @param t
     */
    public void removeOne(T t){
        list.remove(t);
        notifyDataSetChanged();
    }

    /**
     * 移除数据集合
     * @param arrayList
     */
    public void removList(List<T> arrayList){
        list.removeAll(arrayList);
        notifyDataSetChanged();
    }

    /**
     * 列表全部数据
     */
    public void removAll(){
        list.clear();
        notifyDataSetChanged();
    }

}
