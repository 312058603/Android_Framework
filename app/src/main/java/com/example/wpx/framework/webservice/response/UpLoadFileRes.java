package com.example.wpx.framework.webservice.response;

/**
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/11/6 20:19
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class UpLoadFileRes {

    /**
     * msg : success
     * returnStatus : 1
     */

    private String msg;

    private String returnStatus;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    @Override
    public String toString() {
        return "UpLoadFileRes{" +
                "msg='" + msg + '\'' +
                ", returnStatus='" + returnStatus + '\'' +
                '}';
    }
}
