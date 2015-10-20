package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>StockTransaction　库存交易记录</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>ts1Id</td><td>{@link String}</td><td>ts1_id</td><td>varchar</td><td>库存交易记录ID</td></tr>
 *   <tr><td>ts1M02Id</td><td>{@link String}</td><td>ts1_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>ts1BillType</td><td>{@link String}</td><td>ts1_billType</td><td>varchar</td><td>单据类型</td></tr>
 *   <tr><td>ts1BillCode</td><td>{@link String}</td><td>ts1_billCode</td><td>varchar</td><td>单据号</td></tr>
 *   <tr><td>ts1Cd1Id</td><td>{@link String}</td><td>ts1_cd1_id</td><td>varchar</td><td>卡ID</td></tr>
 *   <tr><td>ts1Vd1Id</td><td>{@link String}</td><td>ts1_vd1_id</td><td>varchar</td><td>售货机ID</td></tr>
 *   <tr><td>ts1Pd1Id</td><td>{@link String}</td><td>ts1_pd1_id</td><td>varchar</td><td>SKUID</td></tr>
 *   <tr><td>ts1Vc1Code</td><td>{@link String}</td><td>ts1_vc1_code</td><td>varchar</td><td>售货机货道编号</td></tr>
 *   <tr><td>ts1TransQty</td><td>{@link Integer}</td><td>ts1_transQty</td><td>int</td><td>交易数量</td></tr>
 *   <tr><td>ts1TransType</td><td>{@link String}</td><td>ts1_transType</td><td>varchar</td><td>销售类型</td></tr>
 *   <tr><td>ts1Sp1Code</td><td>{@link String}</td><td>ts1_sp1_code</td><td>varchar</td><td>货主编号</td></tr>
 *   <tr><td>ts1Sp1Name</td><td>{@link String}</td><td>ts1_sp1_Name</td><td>varchar</td><td>货主姓名</td></tr>
 *   <tr><td>ts1UploadStatus</td><td>{@link String}</td><td>ts1_uploadStatus</td><td>varchar</td><td>上传状态</td></tr>
 *   <tr><td>ts1CreateUser</td><td>{@link String}</td><td>ts1_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>ts1CreateTime</td><td>{@link String}</td><td>ts1_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>ts1ModifyUser</td><td>{@link String}</td><td>ts1_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>ts1ModifyTime</td><td>{@link String}</td><td>ts1_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>ts1RowVersion</td><td>{@link String}</td><td>ts1_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class StockTransactionData implements Serializable {

    //定义上传状态
    public static final String UPLOAD_UNLOAD              = "0";                 //未上传
    public static final String UPLOAD_LOAD                = "1";                 //已上传
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID           = 4542416370363666200L;

    //权限设置常量
    public static final String STOCKTRANSACTION_POWER_YES = "0";                 //有权限
    public static final String STOCKTRANSACTION_POWER_NO  = "1";                 //无权限

    //定义单据类型常量
    public static final String BILL_TYPE_PLAN             = "0";                 //计划补货
    public static final String BILL_TYPE_DIFF             = "1";                 //补货差异
    public static final String BILL_TYPE_EMERGENCY        = "2";                 //紧急补货
    public static final String BILL_TYPE_PANDIAN          = "3";                 //盘点
    public static final String BILL_TYPE_GET              = "4";                 //领料
    public static final String BILL_TYPE_RETURN           = "5";                 //退货
    public static final String BILL_TYPE_BORROW           = "6";                 //借还

    private String             ts1Id;

    public String getTs1Id() {
        return this.ts1Id;
    }

    public void setTs1Id(String value) {
        this.ts1Id = value;
    }

    private String ts1M02Id;

    public String getTs1M02Id() {
        return this.ts1M02Id;
    }

    public void setTs1M02Id(String value) {
        this.ts1M02Id = value;
    }

    private String ts1BillType;

    public String getTs1BillType() {
        return this.ts1BillType;
    }

    public void setTs1BillType(String value) {
        this.ts1BillType = value;
    }

    private String ts1BillCode;

    public String getTs1BillCode() {
        return this.ts1BillCode;
    }

    public void setTs1BillCode(String value) {
        this.ts1BillCode = value;
    }

    private String ts1Cd1Id;

    public String getTs1Cd1Id() {
        return this.ts1Cd1Id;
    }

    public void setTs1Cd1Id(String value) {
        this.ts1Cd1Id = value;
    }

    private String ts1Vd1Id;

    public String getTs1Vd1Id() {
        return this.ts1Vd1Id;
    }

    public void setTs1Vd1Id(String value) {
        this.ts1Vd1Id = value;
    }

    private String ts1Pd1Id;

    public String getTs1Pd1Id() {
        return this.ts1Pd1Id;
    }

    public void setTs1Pd1Id(String value) {
        this.ts1Pd1Id = value;
    }

    private String ts1Vc1Code;

    public String getTs1Vc1Code() {
        return this.ts1Vc1Code;
    }

    public void setTs1Vc1Code(String value) {
        this.ts1Vc1Code = value;
    }

    private Integer ts1TransQty;

    public Integer getTs1TransQty() {
        return this.ts1TransQty;
    }

    public void setTs1TransQty(Integer value) {
        this.ts1TransQty = value;
    }

    private String ts1TransType;

    public String getTs1TransType() {
        return this.ts1TransType;
    }

    public void setTs1TransType(String value) {
        this.ts1TransType = value;
    }

    private String ts1Sp1Code;

    public String getTs1Sp1Code() {
        return this.ts1Sp1Code;
    }

    public void setTs1Sp1Code(String value) {
        this.ts1Sp1Code = value;
    }

    private String ts1Sp1Name;

    public String getTs1Sp1Name() {
        return this.ts1Sp1Name;
    }

    public void setTs1Sp1Name(String value) {
        this.ts1Sp1Name = value;
    }

    private String ts1UploadStatus;

    public String getTs1UploadStatus() {
        return ts1UploadStatus;
    }

    public void setTs1UploadStatus(String ts1UploadStatus) {
        this.ts1UploadStatus = ts1UploadStatus;
    }

    private String ts1CreateUser;

    public String getTs1CreateUser() {
        return this.ts1CreateUser;
    }

    public void setTs1CreateUser(String value) {
        this.ts1CreateUser = value;
    }

    private String ts1CreateTime;

    public String getTs1CreateTime() {
        return this.ts1CreateTime;
    }

    public void setTs1CreateTime(String value) {
        this.ts1CreateTime = value;
    }

    private String ts1ModifyUser;

    public String getTs1ModifyUser() {
        return this.ts1ModifyUser;
    }

    public void setTs1ModifyUser(String value) {
        this.ts1ModifyUser = value;
    }

    private String ts1ModifyTime;

    public String getTs1ModifyTime() {
        return this.ts1ModifyTime;
    }

    public void setTs1ModifyTime(String value) {
        this.ts1ModifyTime = value;
    }

    private String ts1RowVersion;

    public String getTs1RowVersion() {
        return this.ts1RowVersion;
    }

    public void setTs1RowVersion(String value) {
        this.ts1RowVersion = value;
    }

}