package com.example.wpx.framework.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.adapter.TestLvAtAdapter;
import com.example.wpx.framework.http.RetrofitClient;
import com.example.wpx.framework.http.model.TestModel;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.TestAtPresenter;
import com.example.wpx.framework.ui.view.ITestAtView;
import com.example.wpx.framework.util.LogUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class TestActivity extends BaseActivity<ITestAtView, TestAtPresenter> implements OnRefreshListener, OnLoadmoreListener, AdapterView.OnItemClickListener {

    private RefreshLayout mRefreshLayout;
    private ClassicsHeader mClassicsHeader;
    private ClassicsFooter mClassicsFooter;
    private ListView mListView;
    private TestLvAtAdapter mTestLvAtAdapter;
    List<String> list = new ArrayList<>();
    private int pageIndex;
    private int pageSize = 15;

    @Override
    protected TestAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void addFilters() {

    }

    @Override
    protected void onReceive(Context context, Intent intent) {

    }

    @Override
    protected void findView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        mClassicsFooter = (ClassicsFooter) mRefreshLayout.getRefreshFooter();
        mListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {
        mTestLvAtAdapter = new TestLvAtAdapter(this, list);
        mListView.setAdapter(mTestLvAtAdapter);
        onRefreshData();
    }

    //下拉刷新监听
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        LogUtil.e("下拉刷新监听");
        onRefreshData();
    }

    //上拉加载更多
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        LogUtil.e("上拉加载更多");
        onLoadmoreData();
    }

    /**
     * 下拉刷新数据
     */
    private void onRefreshData() {
        pageIndex = 1;
        list.clear();
        for (int i = 0; i < pageIndex * pageSize; i++) {
            if (i == 0) {
                list.add("测试get请求");
            } else if (i == 1) {
                list.add("测试post请求");
            } else {
                list.add("第" + (i + 1) + "条数据");
            }
        }
        mTestLvAtAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
    }

    /**
     * 上拉加载更多数据
     */
    private void onLoadmoreData() {
        for (int i = pageIndex * pageSize; i < (pageIndex + 1) * pageSize; i++) {
            list.add("第" + (i + 1) + "条数据");
        }
        mTestLvAtAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadmore();
        pageIndex += 1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (position == 0) {
            String methodName = "";
            Map<String, String> pargrams = new HashMap();
            pargrams.put("pageSize", "5");
            pargrams.put("curPage", "1");
            RetrofitClient.getInstance().get(methodName, pargrams, TestModel.class, new RetrofitClient.RequstLisenerImp<TestModel>(this, true) {
                @Override
                public void onSuccess(TestModel testModel) {
                    LogUtil.e(testModel.toString());
                }
            });
        } else if (position == 1) {

        }
    }

}
