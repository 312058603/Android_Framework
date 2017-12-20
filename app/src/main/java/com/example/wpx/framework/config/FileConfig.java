package com.example.wpx.framework.config;

import android.os.Environment;

import com.example.wpx.framework.util.otherutil.FileUtils;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class FileConfig {

    //应用文件目录
    public static final String PATH_BASE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "com.gmcx" + "/";
    /**
     * 应用Log日志
     */
    public static final String PATH_LOG = PATH_BASE + "Log/";
    /**
     * 临时文件夹
     */
    public static final String PATH_TEMP = PATH_BASE + "Temp/";
    /**
     * 下载文件文件夹
     */
    public static final String PATH_DOWNLOAD = PATH_BASE + "Download/";
    /**
     * 拍照文件夹
     */
    public static final String PATH_CAMERA = PATH_BASE + "Camera/";
    /**
     * 应用图片文件夹
     */
    public static final String PATH_IMAGES = PATH_BASE + "Images/";
    /**
     * 更新包文件夹
     */
    public static String PATH_UPDATE = PATH_BASE + "Update/";
    /**
     * 系统图片文件夹
     */
    public static String PATH_SYSTEM_IMAGE = PATH_BASE + "SystemImage";

    //文件路径

    /**
     *  更新Apk路径
     */
    public static final String PATH_UPDATE_APK=PATH_UPDATE+"update.apk";

    public static void createFile() {
        FileUtils.createOrExistsDir(PATH_LOG);
        FileUtils.createOrExistsDir(PATH_TEMP);
        FileUtils.createOrExistsDir(PATH_DOWNLOAD);
        FileUtils.createOrExistsDir(PATH_CAMERA);
        FileUtils.createOrExistsDir(PATH_IMAGES);
        FileUtils.createOrExistsDir(PATH_UPDATE);
        FileUtils.createOrExistsDir(PATH_SYSTEM_IMAGE);
    }

}
