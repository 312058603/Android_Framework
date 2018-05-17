package com.example.wpx.framework.config;

import com.example.wpx.framework.service.parsedata.model.PCCJsonBean;
import java.util.ArrayList;

/**
 * <h3>全局省市县3级联动数据</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 13:59
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class CitysConfig {
    public static ArrayList<PCCJsonBean> optionsProviceItems = new ArrayList<>();
    public static ArrayList<ArrayList<String>> optionsCityItems = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<String>>> optionsCountyItems = new ArrayList<>();
}
