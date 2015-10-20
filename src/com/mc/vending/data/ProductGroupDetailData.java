package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>ProductGroupDetail　产品组合从表</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>pg2Id</td><td>{@link String}</td><td>pg2_id</td><td>varchar</td><td>产品组合从表ID</td></tr>
 *   <tr><td>pg2M02Id</td><td>{@link String}</td><td>pg2_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>pg2Pg1Id</td><td>{@link String}</td><td>pg2_pg1_id</td><td>varchar</td><td>产品组合主表ID</td></tr>
 *   <tr><td>pg2Pd1Id</td><td>{@link String}</td><td>pg2_pd1_id</td><td>varchar</td><td>SKUID</td></tr>
 *   <tr><td>pg2GroupQty</td><td>{@link Integer}</td><td>pg2_groupQty</td><td>int</td><td>组合数量</td></tr>
 *   <tr><td>pg2CreateUser</td><td>{@link String}</td><td>pg2_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>pg2CreateTime</td><td>{@link String}</td><td>pg2_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>pg2ModifyUser</td><td>{@link String}</td><td>pg2_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>pg2ModifyTime</td><td>{@link String}</td><td>pg2_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>pg2RowVersion</td><td>{@link String}</td><td>pg2_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ProductGroupDetailData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2932329074755520269L;

    private String            pg2Id;

    public String getPg2Id() {
        return this.pg2Id;
    }

    public void setPg2Id(String value) {
        this.pg2Id = value;
    }

    private String pg2M02Id;

    public String getPg2M02Id() {
        return this.pg2M02Id;
    }

    public void setPg2M02Id(String value) {
        this.pg2M02Id = value;
    }

    private String pg2Pg1Id;

    public String getPg2Pg1Id() {
        return this.pg2Pg1Id;
    }

    public void setPg2Pg1Id(String value) {
        this.pg2Pg1Id = value;
    }

    private String pg2Pd1Id;

    public String getPg2Pd1Id() {
        return this.pg2Pd1Id;
    }

    public void setPg2Pd1Id(String value) {
        this.pg2Pd1Id = value;
    }

    private Integer pg2GroupQty;

    public Integer getPg2GroupQty() {
        return this.pg2GroupQty;
    }

    public void setPg2GroupQty(Integer value) {
        this.pg2GroupQty = value;
    }

    private String pg2CreateUser;

    public String getPg2CreateUser() {
        return this.pg2CreateUser;
    }

    public void setPg2CreateUser(String value) {
        this.pg2CreateUser = value;
    }

    private String pg2CreateTime;

    public String getPg2CreateTime() {
        return this.pg2CreateTime;
    }

    public void setPg2CreateTime(String value) {
        this.pg2CreateTime = value;
    }

    private String pg2ModifyUser;

    public String getPg2ModifyUser() {
        return this.pg2ModifyUser;
    }

    public void setPg2ModifyUser(String value) {
        this.pg2ModifyUser = value;
    }

    private String pg2ModifyTime;

    public String getPg2ModifyTime() {
        return this.pg2ModifyTime;
    }

    public void setPg2ModifyTime(String value) {
        this.pg2ModifyTime = value;
    }

    private String pg2RowVersion;

    public String getPg2RowVersion() {
        return this.pg2RowVersion;
    }

    public void setPg2RowVersion(String value) {
        this.pg2RowVersion = value;
    }

    @Override
    public String toString() {
        return "ProductGroupDetailData [pg2Id=" + pg2Id + ", pg2M02Id=" + pg2M02Id + ", pg2Pg1Id="
               + pg2Pg1Id + ", pg2Pd1Id=" + pg2Pd1Id + ", pg2GroupQty=" + pg2GroupQty
               + ", pg2CreateUser=" + pg2CreateUser + ", pg2CreateTime=" + pg2CreateTime
               + ", pg2ModifyUser=" + pg2ModifyUser + ", pg2ModifyTime=" + pg2ModifyTime
               + ", pg2RowVersion=" + pg2RowVersion + "]";
    }

}