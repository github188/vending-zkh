/**
 * 
 */
package com.mc.vending.data;

/**
 * @author Forever
 * @date 2015年7月17日
 * @email forever.wang@zillionstar.com
 */
public class BaseParseData {

    private String logVersion;
    private String CreateUser;
    private String CreateTime;
    private String ModifyUser;
    private String ModifyTime;
    private String RowVersion;

    public String getRowVersion() {
        return RowVersion;
    }

    public void setRowVersion(String rowVersion) {
        RowVersion = rowVersion;
    }

    public String getLogVersion() {
        return logVersion;
    }

    public void setLogVersion(String logVersion) {
        this.logVersion = logVersion;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getModifyUser() {
        return ModifyUser;
    }

    public void setModifyUser(String modifyUser) {
        ModifyUser = modifyUser;
    }

    public String getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        ModifyTime = modifyTime;
    }

}
