package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnWrapperData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long   serialVersionUID = -6703331592768913133L;

    private ChnStockWrapperData chnStock;                                //货道库存封装对象
    private int                 qty;                                     //领料数量

    public ChnStockWrapperData getChnStock() {
        return chnStock;
    }

    public void setChnStock(ChnStockWrapperData chnStock) {
        this.chnStock = chnStock;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
