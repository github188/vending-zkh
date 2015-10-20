package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Supplier 货主</strong>
 * <p>
 * <table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th>
 * <th nowrap>属性类型</th>
 * <th nowrap>字段名</th>
 * <th nowrap>字段类型</th>
 * <th nowrap>说明</th>
 * </tr>
 * <tr>
 * <td>sp1Id</td>
 * <td>{@link String}</td>
 * <td>sp1_id</td>
 * <td>varchar</td>
 * <td>货主ID</td>
 * </tr>
 * <tr>
 * <td>sp1M02Id</td>
 * <td>{@link String}</td>
 * <td>sp1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>sp1Code</td>
 * <td>{@link String}</td>
 * <td>sp1_code</td>
 * <td>varchar</td>
 * <td>货主编号</td>
 * </tr>
 * <tr>
 * <td>sp1Name</td>
 * <td>{@link String}</td>
 * <td>sp1_name</td>
 * <td>varchar</td>
 * <td>货主名称</td>
 * </tr>
 * <tr>
 * <td>sp1CreateUser</td>
 * <td>{@link String}</td>
 * <td>sp1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>sp1CreateTime</td>
 * <td>{@link String}</td>
 * <td>sp1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>sp1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>sp1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>sp1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>sp1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>sp1RowVersion</td>
 * <td>{@link String}</td>
 * <td>sp1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class SupplierData extends BaseParseData implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5658447978045405122L;

    private String            sp1Id;

    public String getSp1Id() {
        return this.sp1Id;
    }

    public void setSp1Id(String value) {
        this.sp1Id = value;
    }

    private String sp1M02Id;

    public String getSp1M02Id() {
        return this.sp1M02Id;
    }

    public void setSp1M02Id(String value) {
        this.sp1M02Id = value;
    }

    private String sp1Code;

    public String getSp1Code() {
        return this.sp1Code;
    }

    public void setSp1Code(String value) {
        this.sp1Code = value;
    }

    private String sp1Name;

    public String getSp1Name() {
        return this.sp1Name;
    }

    public void setSp1Name(String value) {
        this.sp1Name = value;
    }

    private String sp1CreateUser;

    public String getSp1CreateUser() {
        return this.sp1CreateUser;
    }

    public void setSp1CreateUser(String value) {
        this.sp1CreateUser = value;
    }

    private String sp1CreateTime;

    public String getSp1CreateTime() {
        return this.sp1CreateTime;
    }

    public void setSp1CreateTime(String value) {
        this.sp1CreateTime = value;
    }

    private String sp1ModifyUser;

    public String getSp1ModifyUser() {
        return this.sp1ModifyUser;
    }

    public void setSp1ModifyUser(String value) {
        this.sp1ModifyUser = value;
    }

    private String sp1ModifyTime;

    public String getSp1ModifyTime() {
        return this.sp1ModifyTime;
    }

    public void setSp1ModifyTime(String value) {
        this.sp1ModifyTime = value;
    }

    private String sp1RowVersion;

    public String getSp1RowVersion() {
        return this.sp1RowVersion;
    }

    public void setSp1RowVersion(String value) {
        this.sp1RowVersion = value;
    }

    @Override
    public String toString() {
        return "SupplierData [sp1Id=" + sp1Id + ", sp1M02Id=" + sp1M02Id + ", sp1Code=" + sp1Code
                + ", sp1Name=" + sp1Name + ", sp1CreateUser=" + sp1CreateUser + ", sp1CreateTime="
                + sp1CreateTime + ", sp1ModifyUser=" + sp1ModifyUser + ", sp1ModifyTime=" + sp1ModifyTime
                + ", sp1RowVersion=" + sp1RowVersion + "]";
    }

}