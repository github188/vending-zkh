package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class ProductGroupHeadWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long             serialVersionUID = 46121774452384773L;
    private String                        pg1Id;
    private List<ProductGroupWrapperData> wrapperList;

    public String getPg1Id() {
        return pg1Id;
    }

    public void setPg1Id(String pg1Id) {
        this.pg1Id = pg1Id;
    }

    public List<ProductGroupWrapperData> getWrapperList() {
        return wrapperList;
    }

    public void setWrapperList(List<ProductGroupWrapperData> wrapperList) {
        this.wrapperList = wrapperList;
    }

}
