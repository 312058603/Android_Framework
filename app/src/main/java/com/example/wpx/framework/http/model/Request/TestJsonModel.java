package com.example.wpx.framework.http.model.Request;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/21 11:17
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class TestJsonModel {
    private String pageSize;
    private String curPage;

    public TestJsonModel() {
    }

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "TestJsonModel{" +
                "pageSize=" + pageSize +
                ", curPage=" + curPage +
                '}';
    }
}
