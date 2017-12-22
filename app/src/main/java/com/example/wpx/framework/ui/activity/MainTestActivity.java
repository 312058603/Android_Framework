package com.example.wpx.framework.ui.activity;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.wpx.framework.R;
import com.example.wpx.framework.adapter.TestLvAtAdapter;
import com.example.wpx.framework.config.BroadcastFilterConfig;
import com.example.wpx.framework.config.FileConfig;
import com.example.wpx.framework.config.PCCConfig;
import com.example.wpx.framework.http.observer.DownLoadListener;
import com.example.wpx.framework.http.observer.GetOrPostListener;
import com.example.wpx.framework.http.RetrofitClient;
import com.example.wpx.framework.http.model.Request.TestJsonModel;
import com.example.wpx.framework.http.model.Response.TestResponseModel;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.MainTestAtPresenter;
import com.example.wpx.framework.ui.view.IMainTestAtView;
import com.example.wpx.framework.util.DateUtil;
import com.example.wpx.framework.util.IntentUtil;
import com.example.wpx.framework.util.LogUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>测试界面</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class MainTestActivity extends BaseActivity<IMainTestAtView, MainTestAtPresenter> implements OnRefreshListener, OnLoadmoreListener, AdapterView.OnItemClickListener {

    private RefreshLayout refreshLayout;
    private ClassicsHeader classicsHeader;
    private ClassicsFooter classicsFooter;
    private ListView listView;
    private TestLvAtAdapter testLvAtAdapter;
    List<String> list = new ArrayList<>();
    private int pageIndex;
    private int pageSize = 15;

    private TimePickerView pvCustomTime;
    private OptionsPickerView pvOptions;

    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected MainTestAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void addFilters() {
        filter.addAction(BroadcastFilterConfig.ACTION_OPENBLE);
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case BroadcastFilterConfig.ACTION_OPENBLE:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                break;
        }
    }

    @Override
    protected void findView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        classicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
        classicsFooter = (ClassicsFooter) refreshLayout.getRefreshFooter();
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {
        testLvAtAdapter = new TestLvAtAdapter(this, list);
        listView.setAdapter(testLvAtAdapter);
        onRefreshData();
        initCustomTimePicker();
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
                list.add("get请求");
            } else if (i == 1) {
                list.add("post请求");
            } else if (i == 2) {
                list.add("文件下载");
            } else if (i == 3) {
                list.add("json请求");
            } else if (i == 4) {
                list.add("上传单个文件");
            } else if (i == 5) {
                list.add("上传多个文件");
            } else if (i == 6) {
                list.add("时间选择器控件");
            } else if (i == 7) {
                list.add("城市列表控件");
            } else if (i == 8) {
                list.add("数据选择器控件");
            } else if (i == 9) {
                list.add("Ble测试");
            } else {
                list.add("第" + (i + 1) + "条数据");
            }
        }
        testLvAtAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

    /**
     * 上拉加载更多数据
     */
    private void onLoadmoreData() {
        for (int i = pageIndex * pageSize; i < (pageIndex + 1) * pageSize; i++) {
            list.add("第" + (i + 1) + "条数据");
        }
        testLvAtAdapter.notifyDataSetChanged();
        refreshLayout.finishLoadmore();
        pageIndex += 1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //get表单请求测试
        if (position == 0) {
            String methodName = "";
            Map<String, String> pargrams = new HashMap();
            pargrams.put("pageSize", "5");
            pargrams.put("curPage", "1");
            RetrofitClient.getInstance().get(methodName, pargrams, this, true, TestResponseModel.class, new GetOrPostListener<TestResponseModel>() {
                @Override
                public void onSuccess(TestResponseModel testResponseModel) {
                    LogUtil.e(testResponseModel.toString());
                }
            });
        }
        //post表单请求测试
        else if (position == 1) {
            String methodName = "";
            Map<String, String> pargrams = new HashMap();
            pargrams.put("pageSize", "5");
            pargrams.put("curPage", "1");
            RetrofitClient.getInstance().post(methodName, pargrams, this, true, TestResponseModel.class, new GetOrPostListener<TestResponseModel>() {
                @Override
                public void onSuccess(TestResponseModel testResponseModel) {
                    LogUtil.e(testResponseModel.toString());
                }
            });
        }
        //文件下载
        else if (position == 2) {
            String aplUrl = "http://121.37.17.177:27024/Apk/YGT_V1.2.apk";
            RetrofitClient.getInstance().downLoadFile(aplUrl, FileConfig.PATH_UPDATE_APK, new DownLoadListener() {
                @Override
                public void onProgress(long position, long fileSize) {
                    LogUtil.e("position=" + position + ",fileSize=" + fileSize);
                }

                @Override
                public void onDownLoadOver(File file) {
                    LogUtil.e("onDownLoadOver file.exists()=" + file.exists());
                }
            });
        }
        //Json提交
        else if (position == 3) {
            String method = "";
            TestJsonModel testJsonModel = new TestJsonModel();
            testJsonModel.setCurPage("1");
            testJsonModel.setPageSize("5");
            RetrofitClient.getInstance().json(method, this, true, testJsonModel, TestResponseModel.class, new GetOrPostListener<TestResponseModel>() {
                @Override
                public void onSuccess(TestResponseModel testResponseModel) {
                    LogUtil.e(testResponseModel.toString());
                }
            });
        } else if (position == 6) {
            pvCustomTime.show();
        } else if (position == 7) {
            ShowPickerView();
        } else if (position == 8) {
            initOptionPicker();
        } else if (position == 9) {
            IntentUtil.startActivity(this, BleTestActivity.class);
        }
    }

    /**
     * 时间选择器
     */
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtil.e("设置时间=" + DateUtil.getTimeByDate(date));
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    /**
     * 省市县选择器
     */
    private void ShowPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = PCCConfig.optionsProviceItems.get(options1).getPickerViewText() +
                        PCCConfig.optionsCityItems.get(options1).get(options2) +
                        PCCConfig.optionsCountyItems.get(options1).get(options2).get(options3);
                LogUtil.e("省市县设置:" + tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(PCCConfig.optionsProviceItems, PCCConfig.optionsCityItems, PCCConfig.optionsCountyItems);//三级选择器
        pvOptions.show();
    }

    /**
     * 数据选择器
     */
    private void initOptionPicker() {
        ArrayList<Integer> dataItems = new ArrayList<>();
        dataItems.add(180);
        dataItems.add(181);
        dataItems.add(182);
        dataItems.add(183);
        dataItems.add(184);
        dataItems.add(185);
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int data = dataItems.get(options1);
                LogUtil.e("数据设置:" + data);
            }
        })
                .setTitleText("身高选择")
                .setSelectOptions(0)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("厘米", null, null)
                .build();
        pvOptions.setPicker(dataItems);//一级选择器
        pvOptions.show();
    }

}
