package com.mc.vending.data;

import java.io.Serializable;

public class ReplenishmentDetailWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long       serialVersionUID = -6446590272795987752L;
    private ReplenishmentDetailData ReplenishmentDetail;
    private String                  productName;

    public ReplenishmentDetailData getReplenishmentDetail() {
        return ReplenishmentDetail;
    }

    public void setReplenishmentDetail(ReplenishmentDetailData replenishmentDetail) {
        ReplenishmentDetail = replenishmentDetail;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
