package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class ProductGroupWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long           serialVersionUID = -977644926373807778L;

    //产品ID
    private String                      skuId;
    //产品名称
    private String                      productName;
    //组合数量
    private int                         groupQty;

    //货道封装对象
    private List<VendingChnWrapperData> chnWrapperList;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getGroupQty() {
        return groupQty;
    }

    public void setGroupQty(int groupQty) {
        this.groupQty = groupQty;
    }

    public List<VendingChnWrapperData> getChnWrapperList() {
        return chnWrapperList;
    }

    public void setChnWrapperList(List<VendingChnWrapperData> chnWrapperList) {
        this.chnWrapperList = chnWrapperList;
    }

}
