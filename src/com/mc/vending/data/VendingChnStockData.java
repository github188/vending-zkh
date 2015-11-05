package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>VendingChnStock 售货机货道库存</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>vs1Id</td><td>{@link String}</td><td>vs1_id</td><td>varchar</td><td>售货机货道库存ID</td></tr>
 *   <tr><td>vs1M02Id</td><td>{@link String}</td><td>vs1_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>vs1Vd1Id</td><td>{@link String}</td><td>vs1_vd1_id</td><td>varchar</td><td>售货机ID</td></tr>
 *   <tr><td>vs1Vc1Code</td><td>{@link String}</td><td>vs1_vc1_code</td><td>varchar</td><td>售货机货道编号</td></tr>
 *   <tr><td>vs1Pd1Id</td><td>{@link String}</td><td>vs1_pd1_id</td><td>varchar</td><td>SKUID</td></tr>
 *   <tr><td>vs1Quantity</td><td>{@link Integer}</td><td>vs1_quantity</td><td>int</td><td>库存数量</td></tr>
 *   <tr><td>vs1CreateUser</td><td>{@link String}</td><td>vs1_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>vs1CreateTime</td><td>{@link String}</td><td>vs1_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>vs1ModifyUser</td><td>{@link String}</td><td>vs1_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>vs1ModifyTime</td><td>{@link String}</td><td>vs1_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>vs1RowVersion</td><td>{@link String}</td><td>vs1_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingChnStockData extends BaseParseData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2632596560892226177L;

    private String            vs1Id;

    public String getVs1Id() {
        return this.vs1Id;
    }

    public void setVs1Id(String value) {
        this.vs1Id = value;
    }

    private String vs1M02Id;

    public String getVs1M02Id() {
        return this.vs1M02Id;
    }

    public void setVs1M02Id(String value) {
        this.vs1M02Id = value;
    }

    private String vs1Vd1Id;

    public String getVs1Vd1Id() {
        return this.vs1Vd1Id;
    }

    public void setVs1Vd1Id(String value) {
        this.vs1Vd1Id = value;
    }

    private String vs1Vc1Code;

    public String getVs1Vc1Code() {
        return this.vs1Vc1Code;
    }

    public void setVs1Vc1Code(String value) {
        this.vs1Vc1Code = value;
    }

    private String vs1Pd1Id;

    public String getVs1Pd1Id() {
        return this.vs1Pd1Id;
    }

    public void setVs1Pd1Id(String value) {
        this.vs1Pd1Id = value;
    }

    private Integer vs1Quantity;

    public Integer getVs1Quantity() {
        return this.vs1Quantity;
    }

    public void setVs1Quantity(Integer value) {
        this.vs1Quantity = value;
    }

    private String vs1CreateUser;

    public String getVs1CreateUser() {
        return this.vs1CreateUser;
    }

    public void setVs1CreateUser(String value) {
        this.vs1CreateUser = value;
    }

    private String vs1CreateTime;

    public String getVs1CreateTime() {
        return this.vs1CreateTime;
    }

    public void setVs1CreateTime(String value) {
        this.vs1CreateTime = value;
    }

    private String vs1ModifyUser;

    public String getVs1ModifyUser() {
        return this.vs1ModifyUser;
    }

    public void setVs1ModifyUser(String value) {
        this.vs1ModifyUser = value;
    }

    private String vs1ModifyTime;

    public String getVs1ModifyTime() {
        return this.vs1ModifyTime;
    }

    public void setVs1ModifyTime(String value) {
        this.vs1ModifyTime = value;
    }

    private String vs1RowVersion;

    public String getVs1RowVersion() {
        return this.vs1RowVersion;
    }

    public void setVs1RowVersion(String value) {
        this.vs1RowVersion = value;
    }

    @Override
    public String toString() {
        return "VendingChnStockData [vs1Id=" + vs1Id + ", vs1M02Id=" + vs1M02Id + ", vs1Vd1Id="
               + vs1Vd1Id + ", vs1Vc1Code=" + vs1Vc1Code + ", vs1Pd1Id=" + vs1Pd1Id
               + ", vs1Quantity=" + vs1Quantity + ", vs1CreateUser=" + vs1CreateUser
               + ", vs1CreateTime=" + vs1CreateTime + ", vs1ModifyUser=" + vs1ModifyUser
               + ", vs1ModifyTime=" + vs1ModifyTime + ", vs1RowVersion=" + vs1RowVersion + "]";
    }

}