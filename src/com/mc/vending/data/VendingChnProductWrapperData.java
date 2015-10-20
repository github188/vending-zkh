package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnProductWrapperData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -6195552913543872304L;
    private VendingChnData    vendingChn;                              //售货机货道对象
    private String            productName;                             //产品名称
    private int               actQty;                                  //数量
    private int               stock;                                   //库存

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public VendingChnData getVendingChn() {
        return vendingChn;
    }

    public void setVendingChn(VendingChnData vendingChn) {
        this.vendingChn = vendingChn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getActQty() {
        return actQty;
    }

    public void setActQty(int actQty) {
        this.actQty = actQty;
    }

}
