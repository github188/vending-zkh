package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Customer　客户</strong>
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
 * <td>cu1Id</td>
 * <td>{@link String}</td>
 * <td>cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>cu1M02Id</td>
 * <td>{@link String}</td>
 * <td>cu1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>cu1Code</td>
 * <td>{@link String}</td>
 * <td>cu1_code</td>
 * <td>varchar</td>
 * <td>客户编号</td>
 * </tr>
 * <tr>
 * <td>cu1Name</td>
 * <td>{@link String}</td>
 * <td>cu1_name</td>
 * <td>varchar</td>
 * <td>客户名称</td>
 * </tr>
 * <tr>
 * <td>cu1Relation</td>
 * <td>{@link String}</td>
 * <td>cu1_relation</td>
 * <td>varchar</td>
 * <td>联系人</td>
 * </tr>
 * <tr>
 * <td>cu1RelationPhone</td>
 * <td>{@link String}</td>
 * <td>cu1_relationPhone</td>
 * <td>varchar</td>
 * <td>联系人电话</td>
 * </tr>
 * <tr>
 * <td>cu1Saler</td>
 * <td>{@link String}</td>
 * <td>cu1_saler</td>
 * <td>varchar</td>
 * <td>销售员</td>
 * </tr>
 * <tr>
 * <td>cu1SalerPhone</td>
 * <td>{@link String}</td>
 * <td>cu1_salerPhone</td>
 * <td>varchar</td>
 * <td>销售员电话</td>
 * </tr>
 * <tr>
 * <td>cu1Country</td>
 * <td>{@link String}</td>
 * <td>cu1_country</td>
 * <td>varchar</td>
 * <td>省</td>
 * </tr>
 * <tr>
 * <td>cu1City</td>
 * <td>{@link String}</td>
 * <td>cu1_city</td>
 * <td>varchar</td>
 * <td>市</td>
 * </tr>
 * <tr>
 * <td>cu1Area</td>
 * <td>{@link String}</td>
 * <td>cu1_area</td>
 * <td>varchar</td>
 * <td>区/县</td>
 * </tr>
 * <tr>
 * <td>cu1Address</td>
 * <td>{@link String}</td>
 * <td>cu1_address</td>
 * <td>varchar</td>
 * <td>地址</td>
 * </tr>
 * <tr>
 * <td>cu1LastImportTime</td>
 * <td>{@link String}</td>
 * <td>cu1_lastImportTime</td>
 * <td>varchar</td>
 * <td>最近导入时间</td>
 * </tr>
 * <tr>
 * <td>cu1CodeFather</td>
 * <td>{@link String}</td>
 * <td>cu1_code_father</td>
 * <td>varchar</td>
 * <td>上级客户</td>
 * </tr>
 * <tr>
 * <td>cu1CreateUser</td>
 * <td>{@link String}</td>
 * <td>cu1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>cu1CreateTime</td>
 * <td>{@link String}</td>
 * <td>cu1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>cu1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>cu1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>cu1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>cu1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>cu1RowVersion</td>
 * <td>{@link String}</td>
 * <td>cu1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class CustomerData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6793590734045578599L;

    private String            cu1Id;

    public String getCu1Id() {
        return this.cu1Id;
    }

    public void setCu1Id(String value) {
        this.cu1Id = value;
    }

    private String cu1M02Id;

    public String getCu1M02Id() {
        return this.cu1M02Id;
    }

    public void setCu1M02Id(String value) {
        this.cu1M02Id = value;
    }

    private String cu1Code;

    public String getCu1Code() {
        return this.cu1Code;
    }

    public void setCu1Code(String value) {
        this.cu1Code = value;
    }

    private String cu1Name;

    public String getCu1Name() {
        return this.cu1Name;
    }

    public void setCu1Name(String value) {
        this.cu1Name = value;
    }

    private String cu1Relation;

    public String getCu1Relation() {
        return this.cu1Relation;
    }

    public void setCu1Relation(String value) {
        this.cu1Relation = value;
    }

    private String cu1RelationPhone;

    public String getCu1RelationPhone() {
        return this.cu1RelationPhone;
    }

    public void setCu1RelationPhone(String value) {
        this.cu1RelationPhone = value;
    }

    private String cu1Saler;

    public String getCu1Saler() {
        return this.cu1Saler;
    }

    public void setCu1Saler(String value) {
        this.cu1Saler = value;
    }

    private String cu1SalerPhone;

    public String getCu1SalerPhone() {
        return this.cu1SalerPhone;
    }

    public void setCu1SalerPhone(String value) {
        this.cu1SalerPhone = value;
    }

    private String cu1Country;

    public String getCu1Country() {
        return this.cu1Country;
    }

    public void setCu1Country(String value) {
        this.cu1Country = value;
    }

    private String cu1City;

    public String getCu1City() {
        return this.cu1City;
    }

    public void setCu1City(String value) {
        this.cu1City = value;
    }

    private String cu1Area;

    public String getCu1Area() {
        return this.cu1Area;
    }

    public void setCu1Area(String value) {
        this.cu1Area = value;
    }

    private String cu1Address;

    public String getCu1Address() {
        return this.cu1Address;
    }

    public void setCu1Address(String value) {
        this.cu1Address = value;
    }

    private String cu1LastImportTime;

    public String getCu1LastImportTime() {
        return this.cu1LastImportTime;
    }

    public void setCu1LastImportTime(String value) {
        this.cu1LastImportTime = value;
    }

    private String cu1CodeFather;

    public String getCu1CodeFather() {
        return cu1CodeFather;
    }

    public void setCu1CodeFather(String cu1CodeFather) {
        this.cu1CodeFather = cu1CodeFather;
    }

    private String cu1CreateUser;

    public String getCu1CreateUser() {
        return this.cu1CreateUser;
    }

    public void setCu1CreateUser(String value) {
        this.cu1CreateUser = value;
    }

    private String cu1CreateTime;

    public String getCu1CreateTime() {
        return this.cu1CreateTime;
    }

    public void setCu1CreateTime(String value) {
        this.cu1CreateTime = value;
    }

    private String cu1ModifyUser;

    public String getCu1ModifyUser() {
        return this.cu1ModifyUser;
    }

    public void setCu1ModifyUser(String value) {
        this.cu1ModifyUser = value;
    }

    private String cu1ModifyTime;

    public String getCu1ModifyTime() {
        return this.cu1ModifyTime;
    }

    public void setCu1ModifyTime(String value) {
        this.cu1ModifyTime = value;
    }

    private String cu1RowVersion;

    public String getCu1RowVersion() {
        return this.cu1RowVersion;
    }

    public void setCu1RowVersion(String value) {
        this.cu1RowVersion = value;
    }

    @Override
    public String toString() {
        return "CustomerData [cu1Id=" + cu1Id + ", cu1M02Id=" + cu1M02Id + ", cu1Code=" + cu1Code
                + ", cu1Name=" + cu1Name + ", cu1Relation=" + cu1Relation + ", cu1RelationPhone="
                + cu1RelationPhone + ", cu1Saler=" + cu1Saler + ", cu1SalerPhone=" + cu1SalerPhone
                + ", cu1Country=" + cu1Country + ", cu1City=" + cu1City + ", cu1Area=" + cu1Area
                + ", cu1Address=" + cu1Address + ", cu1LastImportTime=" + cu1LastImportTime
                + ", cu1CodeFather=" + cu1CodeFather + ", cu1CreateUser=" + cu1CreateUser
                + ", cu1CreateTime=" + cu1CreateTime + ", cu1ModifyUser=" + cu1ModifyUser
                + ", cu1ModifyTime=" + cu1ModifyTime + ", cu1RowVersion=" + cu1RowVersion + "]";
    }

}