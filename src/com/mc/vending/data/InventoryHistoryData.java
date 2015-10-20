package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>InventoryHistory 盘点记录表</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>ih3Id</td><td>{@link String}</td><td>ih3_id</td><td>varchar</td><td>盘点记录表ID</td></tr>
 *   <tr><td>ih3M02Id</td><td>{@link String}</td><td>ih3_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>ih3IHcode</td><td>{@link String}</td><td>ih3_iHcode</td><td>varchar</td><td>盘点单号</td></tr>
 *   <tr><td>ih3ActualDate</td><td>{@link String}</td><td>ih3_actualDate</td><td>varchar</td><td>实际盘点日期</td></tr>
 *   <tr><td>ih3Cu1Id</td><td>{@link String}</td><td>ih3_cu1_id</td><td>varchar</td><td>客户ID</td></tr>
 *   <tr><td>ih3InventoryPeople</td><td>{@link String}</td><td>ih3_inventoryPeople</td><td>varchar</td><td>盘点人</td></tr>
 *   <tr><td>ih3Vd1Id</td><td>{@link String}</td><td>ih3_vd1_id</td><td>varchar</td><td>售货机ID</td></tr>
 *   <tr><td>ih3Vc1Code</td><td>{@link String}</td><td>ih3_vc1_code</td><td>varchar</td><td>售货机货道编号</td></tr>
 *   <tr><td>ih3Pd1Id</td><td>{@link String}</td><td>ih3_pd1_id</td><td>varchar</td><td>SKUID</td></tr>
 *   <tr><td>ih3Quantity</td><td>{@link Integer}</td><td>ih3_quantity</td><td>int</td><td>库存数量</td></tr>
 *   <tr><td>ih3InventoryQty</td><td>{@link Integer}</td><td>ih3_inventoryQty</td><td>int</td><td>实盘数量</td></tr>
 *   <tr><td>ih3DifferentiaQty</td><td>{@link Integer}</td><td>ih3_differentiaQty</td><td>int</td><td>盘点差异</td></tr>
 *   <tr><td>ih3UploadStatus</td><td>{@link Integer}</td><td>ih3_uploadStatus</td><td>int</td><td>上传状态</td></tr>
 *   <tr><td>ih3CreateUser</td><td>{@link String}</td><td>ih3_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>ih3CreateTime</td><td>{@link String}</td><td>ih3_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>ih3ModifyUser</td><td>{@link String}</td><td>ih3_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>ih3ModifyTime</td><td>{@link String}</td><td>ih3_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>ih3RowVersion</td><td>{@link String}</td><td>ih3_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class InventoryHistoryData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */

    //定义上传状态
    public static final String UPLOAD_UNLOAD    = "0";                 //未上传
    public static final String UPLOAD_LOAD      = "1";                 //已上传

    private static final long  serialVersionUID = 5194870158930913281L;

    private String             ih3Id;

    public String getIh3Id() {
        return this.ih3Id;
    }

    public void setIh3Id(String value) {
        this.ih3Id = value;
    }

    private String ih3M02Id;

    public String getIh3M02Id() {
        return this.ih3M02Id;
    }

    public void setIh3M02Id(String value) {
        this.ih3M02Id = value;
    }

    private String ih3IHcode;

    public String getIh3IHcode() {
        return this.ih3IHcode;
    }

    public void setIh3IHcode(String value) {
        this.ih3IHcode = value;
    }

    private String ih3ActualDate;

    public String getIh3ActualDate() {
        return ih3ActualDate;
    }

    public void setIh3ActualDate(String ih3ActualDate) {
        this.ih3ActualDate = ih3ActualDate;
    }

    private String ih3Cu1Id;

    public String getIh3Cu1Id() {
        return this.ih3Cu1Id;
    }

    public void setIh3Cu1Id(String value) {
        this.ih3Cu1Id = value;
    }

    private String ih3InventoryPeople;

    public String getIh3InventoryPeople() {
        return this.ih3InventoryPeople;
    }

    public void setIh3InventoryPeople(String value) {
        this.ih3InventoryPeople = value;
    }

    private String ih3Vd1Id;

    public String getIh3Vd1Id() {
        return this.ih3Vd1Id;
    }

    public void setIh3Vd1Id(String value) {
        this.ih3Vd1Id = value;
    }

    private String ih3Vc1Code;

    public String getIh3Vc1Code() {
        return this.ih3Vc1Code;
    }

    public void setIh3Vc1Code(String value) {
        this.ih3Vc1Code = value;
    }

    private String ih3Pd1Id;

    public String getIh3Pd1Id() {
        return this.ih3Pd1Id;
    }

    public void setIh3Pd1Id(String value) {
        this.ih3Pd1Id = value;
    }

    private Integer ih3Quantity;

    public Integer getIh3Quantity() {
        return this.ih3Quantity;
    }

    public void setIh3Quantity(Integer value) {
        this.ih3Quantity = value;
    }

    private Integer ih3InventoryQty;

    public Integer getIh3InventoryQty() {
        return this.ih3InventoryQty;
    }

    public void setIh3InventoryQty(Integer value) {
        this.ih3InventoryQty = value;
    }

    private Integer ih3DifferentiaQty;

    public Integer getIh3DifferentiaQty() {
        return this.ih3DifferentiaQty;
    }

    public void setIh3DifferentiaQty(Integer value) {
        this.ih3DifferentiaQty = value;
    }

    private String ih3UploadStatus;

    public String getIh3UploadStatus() {
        return ih3UploadStatus;
    }

    public void setIh3UploadStatus(String ih3UploadStatus) {
        this.ih3UploadStatus = ih3UploadStatus;
    }

    private String ih3CreateUser;

    public String getIh3CreateUser() {
        return this.ih3CreateUser;
    }

    public void setIh3CreateUser(String value) {
        this.ih3CreateUser = value;
    }

    private String ih3CreateTime;

    public String getIh3CreateTime() {
        return this.ih3CreateTime;
    }

    public void setIh3CreateTime(String value) {
        this.ih3CreateTime = value;
    }

    private String ih3ModifyUser;

    public String getIh3ModifyUser() {
        return this.ih3ModifyUser;
    }

    public void setIh3ModifyUser(String value) {
        this.ih3ModifyUser = value;
    }

    private String ih3ModifyTime;

    public String getIh3ModifyTime() {
        return this.ih3ModifyTime;
    }

    public void setIh3ModifyTime(String value) {
        this.ih3ModifyTime = value;
    }

    private String ih3RowVersion;

    public String getIh3RowVersion() {
        return this.ih3RowVersion;
    }

    public void setIh3RowVersion(String value) {
        this.ih3RowVersion = value;
    }

}