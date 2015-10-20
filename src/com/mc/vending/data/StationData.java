package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Station 站点</strong>
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
 * <td>st1Id</td>
 * <td>{@link String}</td>
 * <td>st1_id</td>
 * <td>varchar</td>
 * <td>站点ID</td>
 * </tr>
 * <tr>
 * <td>st1M02Id</td>
 * <td>{@link String}</td>
 * <td>st1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>st1Code</td>
 * <td>{@link String}</td>
 * <td>st1_code</td>
 * <td>varchar</td>
 * <td>站点编号</td>
 * </tr>
 * <tr>
 * <td>st1Name</td>
 * <td>{@link String}</td>
 * <td>st1_name</td>
 * <td>varchar</td>
 * <td>站点名称</td>
 * </tr>
 * <tr>
 * <td>st1Ce1Id</td>
 * <td>{@link String}</td>
 * <td>st1_ce1_id</td>
 * <td>varchar</td>
 * <td>站长ID</td>
 * </tr>
 * <tr>
 * <td>st1Wh1Id</td>
 * <td>{@link String}</td>
 * <td>st1_wh1_id</td>
 * <td>varchar</td>
 * <td>仓库ID</td>
 * </tr>
 * <tr>
 * <td>st1Coordinate</td>
 * <td>{@link String}</td>
 * <td>st1_coordinate</td>
 * <td>varchar</td>
 * <td>地图经纬度</td>
 * </tr>
 * <tr>
 * <td>st1Address</td>
 * <td>{@link String}</td>
 * <td>st1_address</td>
 * <td>varchar</td>
 * <td>地址</td>
 * </tr>
 * <tr>
 * <td>st1Status</td>
 * <td>{@link String}</td>
 * <td>st1_status</td>
 * <td>varchar</td>
 * <td>状态</td>
 * </tr>
 * <tr>
 * <td>st1CreateUser</td>
 * <td>{@link String}</td>
 * <td>st1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>st1CreateTime</td>
 * <td>{@link String}</td>
 * <td>st1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>st1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>st1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>st1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>st1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>st1RowVersion</td>
 * <td>{@link String}</td>
 * <td>st1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class StationData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3151758757494795240L;

    private String            st1Id;

    public String getSt1Id() {
        return this.st1Id;
    }

    public void setSt1Id(String value) {
        this.st1Id = value;
    }

    private String st1M02Id;

    public String getSt1M02Id() {
        return this.st1M02Id;
    }

    public void setSt1M02Id(String value) {
        this.st1M02Id = value;
    }

    private String st1Code;

    public String getSt1Code() {
        return this.st1Code;
    }

    public void setSt1Code(String value) {
        this.st1Code = value;
    }

    private String st1Name;

    public String getSt1Name() {
        return this.st1Name;
    }

    public void setSt1Name(String value) {
        this.st1Name = value;
    }

    private String st1Ce1Id;

    public String getSt1Ce1Id() {
        return st1Ce1Id;
    }

    public void setSt1Ce1Id(String st1Ce1Id) {
        this.st1Ce1Id = st1Ce1Id;
    }

    private String st1Wh1Id;

    public String getSt1Wh1Id() {
        return this.st1Wh1Id;
    }

    public void setSt1Wh1Id(String value) {
        this.st1Wh1Id = value;
    }

    private String st1Coordinate;

    public String getSt1Coordinate() {
        return this.st1Coordinate;
    }

    public void setSt1Coordinate(String value) {
        this.st1Coordinate = value;
    }

    private String st1Address;

    public String getSt1Address() {
        return this.st1Address;
    }

    public void setSt1Address(String value) {
        this.st1Address = value;
    }

    private String st1Status;

    public String getSt1Status() {
        return this.st1Status;
    }

    public void setSt1Status(String value) {
        this.st1Status = value;
    }

    private String st1CreateUser;

    public String getSt1CreateUser() {
        return this.st1CreateUser;
    }

    public void setSt1CreateUser(String value) {
        this.st1CreateUser = value;
    }

    private String st1CreateTime;

    public String getSt1CreateTime() {
        return this.st1CreateTime;
    }

    public void setSt1CreateTime(String value) {
        this.st1CreateTime = value;
    }

    private String st1ModifyUser;

    public String getSt1ModifyUser() {
        return this.st1ModifyUser;
    }

    public void setSt1ModifyUser(String value) {
        this.st1ModifyUser = value;
    }

    private String st1ModifyTime;

    public String getSt1ModifyTime() {
        return this.st1ModifyTime;
    }

    public void setSt1ModifyTime(String value) {
        this.st1ModifyTime = value;
    }

    private String st1RowVersion;

    public String getSt1RowVersion() {
        return this.st1RowVersion;
    }

    public void setSt1RowVersion(String value) {
        this.st1RowVersion = value;
    }

}