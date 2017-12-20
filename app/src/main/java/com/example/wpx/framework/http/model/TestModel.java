package com.example.wpx.framework.http.model;

import java.util.List;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class TestModel {

    /**
     * copyright : 本API接口只允许菜鸟窝(https://www.cniao5.com)用户使用,其他机构或者个人使用均为侵权行为
     * totalCount : 28
     * currentPage : 1
     * totalPage : 1
     * pageSize : 5
     * orders : []
     * list : [{"id":1,"categoryId":5,"campaignId":1,"name":"联想（Lenovo）拯救者14.0英寸游戏本（i7-4720HQ 4G 1T硬盘 GTX960M 2G独显 FHD IPS屏 背光键盘）黑","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1e8f7N4b99de71.jpg","price":5979,"sale":8654},{"id":2,"categoryId":6,"campaignId":1,"name":"奥林巴斯（OLYMPUS）E-M10-1442-EZ 微单电电动变焦套机 银色 内置WIFI 翻转触摸屏 EM10复古高雅","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_rBEhWlMFnG0IAAAAAAIqnbSuyAAAAIxLwJ57aQAAiq1705.jpg","price":3799,"sale":3020},{"id":3,"categoryId":6,"campaignId":3,"name":"Apple iPad mini 2 ME276CH/A （配备 Retina 显示屏 7.9英寸 16G WLAN 机型 深空灰色）","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_548574edNc366ff4a.jpg","price":1938,"sale":9138},{"id":4,"categoryId":6,"campaignId":10,"name":"华为（HUAWEI）P8max 移动联通双4G 双卡双待 星光银）","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55ac5bc9N7dc9657c.jpg","price":3388,"sale":6631},{"id":5,"categoryId":6,"campaignId":4,"name":"联想（Lenovo）7英寸平板电脑 （四核1.3GHz 1G/8G 蓝牙 GPS ） A7-10 (WiFi版 8G存储 黑色) 官方标配","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5535f890Ndfd89dff.jpg","price":499,"sale":5742}]
     */

    private String copyright;
    private int totalCount;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private List<?> orders;
    private List<ListBean> list;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getOrders() {
        return orders;
    }

    public void setOrders(List<?> orders) {
        this.orders = orders;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * categoryId : 5
         * campaignId : 1
         * name : 联想（Lenovo）拯救者14.0英寸游戏本（i7-4720HQ 4G 1T硬盘 GTX960M 2G独显 FHD IPS屏 背光键盘）黑
         * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1e8f7N4b99de71.jpg
         * price : 5979
         * sale : 8654
         */

        private int id;
        private int categoryId;
        private int campaignId;
        private String name;
        private String imgUrl;
        private int price;
        private int sale;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(int campaignId) {
            this.campaignId = campaignId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id=" + id +
                    ", categoryId=" + categoryId +
                    ", campaignId=" + campaignId +
                    ", name='" + name + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", price=" + price +
                    ", sale=" + sale +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "copyright='" + copyright + '\'' +
                ", totalCount=" + totalCount +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", pageSize=" + pageSize +
                ", orders=" + orders +
                ", list=" + list +
                '}';
    }

}
