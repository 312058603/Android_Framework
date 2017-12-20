package com.example.wpx.framework.http.Observer;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/20 17:30
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public interface GetOrPostListener<T> {
    void onSuccess(T t);
}
