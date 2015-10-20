package com.mc.vending.data;

import java.io.Serializable;

public class TransactionWrapperData implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3580961585440586985L;
    private String            createUser;
    private String            createTime;
    private String            name;
    private String            phone;
    private int               transQty;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTransQty() {
        return transQty;
    }

    public void setTransQty(int transQty) {
        this.transQty = transQty;
    }

}
