package com.example.wpx.framework.thread;


/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class ThreadPoolFactory {

    static ThreadPoolProxy mNormalPool;
    static ThreadPoolProxy mDownLoadPool;

    /**
     * 得到一个普通的线程池
     *
     * @return
     */
    public static ThreadPoolProxy getNormalPool() {
        if (mNormalPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * 得到一个下载的线程池
     *
     * @return
     */
    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDownLoadPool == null) {
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownLoadPool;
    }
}
