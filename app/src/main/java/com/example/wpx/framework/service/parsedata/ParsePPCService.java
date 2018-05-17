package com.example.wpx.framework.service.parsedata;

import android.app.IntentService;
import android.content.Intent;
import com.example.wpx.framework.config.CitysConfig;
import com.example.wpx.framework.service.parsedata.model.PCCJsonBean;
import com.example.wpx.framework.util.ParsePPCDataUtil;
import com.example.wpx.framework.util.LogUtil;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.util.ArrayList;

/**
 * <h3>解析城市列表数据</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 13:32
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class ParsePPCService extends IntentService {

    //这是个坑 必须添加默认构造器
    public ParsePPCService() {
        this("ParsePPCService");
    }

    public ParsePPCService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initJsonData();
            }
        }).start();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new ParsePPCDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据

        ArrayList<PCCJsonBean> PCCJsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        CitysConfig.optionsProviceItems = PCCJsonBean;

        for (int i = 0; i< PCCJsonBean.size(); i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c< PCCJsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = PCCJsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (PCCJsonBean.get(i).getCityList().get(c).getArea() == null
                        || PCCJsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d = 0; d < PCCJsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = PCCJsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            CitysConfig.optionsCityItems.add(CityList);

            /**
             * 添加地区数据
             */
            CitysConfig.optionsCountyItems.add(Province_AreaList);
        }
    }

    public ArrayList<PCCJsonBean> parseData(String result) {//Gson 解析
        ArrayList<PCCJsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                PCCJsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), PCCJsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e_Throwable(e);
        }
        return detail;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("Service_ParseCityData服务已销毁");
    }
}
