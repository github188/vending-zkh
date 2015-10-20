package com.mc.vending.data;

import java.io.Serializable;

public class VendingCardPowerWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long    serialVersionUID = -3924255313116862126L;
    private VendingCardPowerData vendingCardPowerData;
    private String               cusEmpId;
    private String               cardPuductPowerType;

    public String getCardPuductPowerType() {
        return cardPuductPowerType;
    }

    public void setCardPuductPowerType(String cardPuductPowerType) {
        this.cardPuductPowerType = cardPuductPowerType;
    }

    public VendingCardPowerData getVendingCardPowerData() {
        return vendingCardPowerData;
    }

    public void setVendingCardPowerData(VendingCardPowerData vendingCardPowerData) {
        this.vendingCardPowerData = vendingCardPowerData;
    }

    public String getCusEmpId() {
        return cusEmpId;
    }

    public void setCusEmpId(String cusEmpId) {
        this.cusEmpId = cusEmpId;
    }

}
