package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Table: <strong>ProductGroupHead 产品组合主表</strong>
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
 * <td>pg1Id</td>
 * <td>{@link String}</td>
 * <td>pg1_id</td>
 * <td>varchar</td>
 * <td>产品组合主表ID</td>
 * </tr>
 * <tr>
 * <td>pg1M02Id</td>
 * <td>{@link String}</td>
 * <td>pg1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>pg1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>pg1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>pg1Code</td>
 * <td>{@link String}</td>
 * <td>pg1_code</td>
 * <td>varchar</td>
 * <td>产品组合编号</td>
 * </tr>
 * <tr>
 * <td>pg1Name</td>
 * <td>{@link String}</td>
 * <td>pg1_name</td>
 * <td>varchar</td>
 * <td>产品组合名称</td>
 * </tr>
 * <tr>
 * <td>pg1CreateUser</td>
 * <td>{@link String}</td>
 * <td>pg1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>pg1CreateTime</td>
 * <td>{@link String}</td>
 * <td>pg1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>pg1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>pg1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>pg1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>pg1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>pg1RowVersion</td>
 * <td>{@link String}</td>
 * <td>pg1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ProductGroupHeadData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2797875315912200009L;

    private String            pg1Id;

    public String getPg1Id() {
        return this.pg1Id;
    }

    public void setPg1Id(String value) {
        this.pg1Id = value;
    }

    private String pg1M02Id;

    public String getPg1M02Id() {
        return this.pg1M02Id;
    }

    public void setPg1M02Id(String value) {
        this.pg1M02Id = value;
    }

    private String pg1Cu1Id;

    public String getPg1Cu1Id() {
        return this.pg1Cu1Id;
    }

    public void setPg1Cu1Id(String value) {
        this.pg1Cu1Id = value;
    }

    private String pg1Code;

    public String getPg1Code() {
        return this.pg1Code;
    }

    public void setPg1Code(String value) {
        this.pg1Code = value;
    }

    private String pg1Name;

    public String getPg1Name() {
        return this.pg1Name;
    }

    public void setPg1Name(String value) {
        this.pg1Name = value;
    }

    private String pg1CreateUser;

    public String getPg1CreateUser() {
        return this.pg1CreateUser;
    }

    public void setPg1CreateUser(String value) {
        this.pg1CreateUser = value;
    }

    private String pg1CreateTime;

    public String getPg1CreateTime() {
        return this.pg1CreateTime;
    }

    public void setPg1CreateTime(String value) {
        this.pg1CreateTime = value;
    }

    private String pg1ModifyUser;

    public String getPg1ModifyUser() {
        return this.pg1ModifyUser;
    }

    public void setPg1ModifyUser(String value) {
        this.pg1ModifyUser = value;
    }

    private String pg1ModifyTime;

    public String getPg1ModifyTime() {
        return this.pg1ModifyTime;
    }

    public void setPg1ModifyTime(String value) {
        this.pg1ModifyTime = value;
    }

    private String pg1RowVersion;

    public String getPg1RowVersion() {
        return this.pg1RowVersion;
    }

    public void setPg1RowVersion(String value) {
        this.pg1RowVersion = value;
    }

    private List<ProductGroupDetailData> children;

    public List<ProductGroupDetailData> getChildren() {
        return children;
    }

    public void setChildren(List<ProductGroupDetailData> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ProductGroupHeadData [pg1Id=" + pg1Id + ", pg1M02Id=" + pg1M02Id + ", pg1Cu1Id=" + pg1Cu1Id
                + ", pg1Code=" + pg1Code + ", pg1Name=" + pg1Name + ", pg1CreateUser=" + pg1CreateUser
                + ", pg1CreateTime=" + pg1CreateTime + ", pg1ModifyUser=" + pg1ModifyUser
                + ", pg1ModifyTime=" + pg1ModifyTime + ", pg1RowVersion=" + pg1RowVersion + ", children="
                + children + "]";
    }

}