package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>InventoryHead 盘点单主表</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>ih1Id</td><td>{@link String}</td><td>ih1_id</td><td>varchar</td><td>盘点单主表ID</td></tr>
 *   <tr><td>ih1M02Id</td><td>{@link String}</td><td>ih1_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>ih1IHcode</td><td>{@link String}</td><td>ih1_iHcode</td><td>varchar</td><td>盘点单号</td></tr>
 *   <tr><td>ih1Type</td><td>{@link String}</td><td>ih1_type</td><td>varchar</td><td>盘点单类型</td></tr>
 *   <tr><td>ih1PlanDate</td><td>{@link String}</td><td>ih1_planDate</td><td>varchar</td><td>计划盘点日期</td></tr>
 *   <tr><td>ih1Ce1Id</td><td>{@link String}</td><td>ih1_ce1_id</td><td>varchar</td><td>盘点人</td></tr>
 *   <tr><td>ih1CreateUser</td><td>{@link String}</td><td>ih1_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>ih1CreateTime</td><td>{@link String}</td><td>ih1_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>ih1ModifyUser</td><td>{@link String}</td><td>ih1_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>ih1ModifyTime</td><td>{@link String}</td><td>ih1_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>ih1RowVersion</td><td>{@link String}</td><td>ih1_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class InventoryHeadData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2208124900220315884L;

    private String            ih1Id;

    public String getIh1Id() {
        return this.ih1Id;
    }

    public void setIh1Id(String value) {
        this.ih1Id = value;
    }

    private String ih1M02Id;

    public String getIh1M02Id() {
        return this.ih1M02Id;
    }

    public void setIh1M02Id(String value) {
        this.ih1M02Id = value;
    }

    private String ih1IHcode;

    public String getIh1IHcode() {
        return this.ih1IHcode;
    }

    public void setIh1IHcode(String value) {
        this.ih1IHcode = value;
    }

    private String ih1Type;

    public String getIh1Type() {
        return this.ih1Type;
    }

    public void setIh1Type(String value) {
        this.ih1Type = value;
    }

    private String ih1PlanDate;

    public String getIh1PlanDate() {
        return this.ih1PlanDate;
    }

    public void setIh1PlanDate(String value) {
        this.ih1PlanDate = value;
    }

    private String ih1Ce1Id;

    public String getIh1Ce1Id() {
        return ih1Ce1Id;
    }

    public void setIh1Ce1Id(String ih1Ce1Id) {
        this.ih1Ce1Id = ih1Ce1Id;
    }

    private String ih1CreateUser;

    public String getIh1CreateUser() {
        return this.ih1CreateUser;
    }

    public void setIh1CreateUser(String value) {
        this.ih1CreateUser = value;
    }

    private String ih1CreateTime;

    public String getIh1CreateTime() {
        return this.ih1CreateTime;
    }

    public void setIh1CreateTime(String value) {
        this.ih1CreateTime = value;
    }

    private String ih1ModifyUser;

    public String getIh1ModifyUser() {
        return this.ih1ModifyUser;
    }

    public void setIh1ModifyUser(String value) {
        this.ih1ModifyUser = value;
    }

    private String ih1ModifyTime;

    public String getIh1ModifyTime() {
        return this.ih1ModifyTime;
    }

    public void setIh1ModifyTime(String value) {
        this.ih1ModifyTime = value;
    }

    private String ih1RowVersion;

    public String getIh1RowVersion() {
        return this.ih1RowVersion;
    }

    public void setIh1RowVersion(String value) {
        this.ih1RowVersion = value;
    }

}