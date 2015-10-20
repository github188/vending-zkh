package com.mc.vending.data;

import java.io.Serializable;

public class ChnStockWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3040431937652936635L;

    //货道对象
    private VendingChnData    vendingChn;
    //售货机货道库存.库存数量
    private int               quantity;

    public VendingChnData getVendingChn() {
        return vendingChn;
    }

    public void setVendingChn(VendingChnData vendingChn) {
        this.vendingChn = vendingChn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
