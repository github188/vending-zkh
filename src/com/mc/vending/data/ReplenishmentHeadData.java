package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Table: <strong>ReplenishmentHead 补货单主表</strong>
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
 * <td>rh1Id</td>
 * <td>{@link String}</td>
 * <td>rh1_id</td>
 * <td>varchar</td>
 * <td>补货单主表ID</td>
 * </tr>
 * <tr>
 * <td>rh1M02Id</td>
 * <td>{@link String}</td>
 * <td>rh1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>rh1Rhcode</td>
 * <td>{@link String}</td>
 * <td>rh1_rhcode</td>
 * <td>varchar</td>
 * <td>补货单号</td>
 * </tr>
 * <tr>
 * <td>rh1RhType</td>
 * <td>{@link String}</td>
 * <td>rh1_rhType</td>
 * <td>varchar</td>
 * <td>补货单类型</td>
 * </tr>
 * <tr>
 * <td>rh1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>rh1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>rh1Vd1Id</td>
 * <td>{@link String}</td>
 * <td>rh1_vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>rh1Wh1Id</td>
 * <td>{@link String}</td>
 * <td>rh1_wh1_id</td>
 * <td>varchar</td>
 * <td>仓库ID</td>
 * </tr>
 * <tr>
 * <td>rh1Ce1IdPh</td>
 * <td>{@link String}</td>
 * <td>rh1_ce1_id_ph</td>
 * <td>varchar</td>
 * <td>配货员ID</td>
 * </tr>
 * <tr>
 * <td>rh1DistributionRemark</td>
 * <td>{@link String}</td>
 * <td>rh1_distributionRemark</td>
 * <td>varchar</td>
 * <td>配货备注</td>
 * </tr>
 * <tr>
 * <td>rh1St1Id</td>
 * <td>{@link String}</td>
 * <td>rh1_st1_id</td>
 * <td>varchar</td>
 * <td>站点ID</td>
 * </tr>
 * <tr>
 * <td>rh1Ce1IdBh</td>
 * <td>{@link String}</td>
 * <td>rh1_ce1_id_bh</td>
 * <td>varchar</td>
 * <td>补货员ID</td>
 * </tr>
 * <tr>
 * <td>rh1ReplenishRemark</td>
 * <td>{@link String}</td>
 * <td>rh1_replenishRemark</td>
 * <td>varchar</td>
 * <td>补货备注</td>
 * </tr>
 * <tr>
 * <td>rh1ReplenishReason</td>
 * <td>{@link String}</td>
 * <td>rh1_replenishReason</td>
 * <td>varchar</td>
 * <td>补货差异原因</td>
 * </tr>
 * <tr>
 * <td>rh1OrderStatus</td>
 * <td>{@link String}</td>
 * <td>rh1_orderStatus</td>
 * <td>varchar</td>
 * <td>订单状态</td>
 * </tr>
 * <tr>
 * <td>rh1DownloadStatus</td>
 * <td>{@link String}</td>
 * <td>rh1_downloadStatus</td>
 * <td>varchar</td>
 * <td>下载状态</td>
 * </tr>
 * <tr>
 * <td>rh1UploadStatus</td>
 * <td>{@link String}</td>
 * <td>rh1_uploadStatus</td>
 * <td>varchar</td>
 * <td>上传状态</td>
 * </tr>
 * <tr>
 * <td>rh1CreateUser</td>
 * <td>{@link String}</td>
 * <td>rh1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>rh1CreateTime</td>
 * <td>{@link String}</td>
 * <td>rh1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>rh1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>rh1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>rh1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>rh1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>rh1RowVersion</td>
 * <td>{@link String}</td>
 * <td>rh1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ReplenishmentHeadData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2701126889875907490L;

    // 定义订单状态
    /** //创建 */
    public static final String ORDERSTATUS_CREATED = "0";
    /** 
     * // 已完成
     */
    public static final String ORDERSTATUS_FINISHED = "1";
    /**
     * // 关闭
     */
    public static final String ORDERSTATUS_CLOSED = "2"; 

    // 定义下载状态
    /**
     * // 未下载
     */
    public static final String DOWNLOAD_UNDOWN = "0"; 
    /**
     * // 已下载
     */
    public static final String DOWNLOAD_DOWN = "1"; 

    // 定义上传状态
    /**
     * // 未上传
     */
    public static final String UPLOAD_UNLOAD = "0"; 
    /**
     * // 已上传
     */
    public static final String UPLOAD_LOAD = "1"; 

    // 定义表单类型
    /** 计划补货 */
    public static final String RH_TYPE_PLAN = "0";
    /** 紧急补货 */
    public static final String RH_TYPE_EMERGENCY = "1";
    /** 一键补满 */
    public static final String RH_TYPE_All = "3";

    private String rh1Id;

    public String getRh1Id() {
        return this.rh1Id;
    }

    public void setRh1Id(String value) {
        this.rh1Id = value;
    }

    private String rh1M02Id;

    public String getRh1M02Id() {
        return this.rh1M02Id;
    }

    public void setRh1M02Id(String value) {
        this.rh1M02Id = value;
    }

    private String rh1Rhcode;

    public String getRh1Rhcode() {
        return this.rh1Rhcode;
    }

    public void setRh1Rhcode(String value) {
        this.rh1Rhcode = value;
    }

    private String rh1RhType;

    public String getRh1RhType() {
        return this.rh1RhType;
    }

    public void setRh1RhType(String value) {
        this.rh1RhType = value;
    }

    private String rh1Cu1Id;

    public String getRh1Cu1Id() {
        return this.rh1Cu1Id;
    }

    public void setRh1Cu1Id(String value) {
        this.rh1Cu1Id = value;
    }

    private String rh1Vd1Id;

    public String getRh1Vd1Id() {
        return this.rh1Vd1Id;
    }

    public void setRh1Vd1Id(String value) {
        this.rh1Vd1Id = value;
    }

    private String rh1Wh1Id;

    public String getRh1Wh1Id() {
        return this.rh1Wh1Id;
    }

    public void setRh1Wh1Id(String value) {
        this.rh1Wh1Id = value;
    }

    private String rh1Ce1IdPh;

    public String getRh1Ce1IdPh() {
        return rh1Ce1IdPh;
    }

    public void setRh1Ce1IdPh(String rh1Ce1IdPh) {
        this.rh1Ce1IdPh = rh1Ce1IdPh;
    }

    private String rh1DistributionRemark;

    public String getRh1DistributionRemark() {
        return this.rh1DistributionRemark;
    }

    public void setRh1DistributionRemark(String value) {
        this.rh1DistributionRemark = value;
    }

    private String rh1St1Id;

    public String getRh1St1Id() {
        return this.rh1St1Id;
    }

    public void setRh1St1Id(String value) {
        this.rh1St1Id = value;
    }

    private String rh1Ce1IdBh;

    public String getRh1Ce1IdBh() {
        return rh1Ce1IdBh;
    }

    public void setRh1Ce1IdBh(String rh1Ce1IdBh) {
        this.rh1Ce1IdBh = rh1Ce1IdBh;
    }

    private String rh1ReplenishRemark;

    public String getRh1ReplenishRemark() {
        return this.rh1ReplenishRemark;
    }

    public void setRh1ReplenishRemark(String value) {
        this.rh1ReplenishRemark = value;
    }

    private String rh1ReplenishReason;

    public String getRh1ReplenishReason() {
        return this.rh1ReplenishReason;
    }

    public void setRh1ReplenishReason(String value) {
        this.rh1ReplenishReason = value;
    }

    private String rh1OrderStatus;

    public String getRh1OrderStatus() {
        return this.rh1OrderStatus;
    }

    public void setRh1OrderStatus(String value) {
        this.rh1OrderStatus = value;
    }

    private String rh1DownloadStatus;

    public String getRh1DownloadStatus() {
        return this.rh1DownloadStatus;
    }

    public void setRh1DownloadStatus(String value) {
        this.rh1DownloadStatus = value;
    }

    private String rh1UploadStatus;

    public String getRh1UploadStatus() {
        return rh1UploadStatus;
    }

    public void setRh1UploadStatus(String rh1UploadStatus) {
        this.rh1UploadStatus = rh1UploadStatus;
    }

    private String rh1CreateUser;

    public String getRh1CreateUser() {
        return this.rh1CreateUser;
    }

    public void setRh1CreateUser(String value) {
        this.rh1CreateUser = value;
    }

    private String rh1CreateTime;

    public String getRh1CreateTime() {
        return this.rh1CreateTime;
    }

    public void setRh1CreateTime(String value) {
        this.rh1CreateTime = value;
    }

    private String rh1ModifyUser;

    public String getRh1ModifyUser() {
        return this.rh1ModifyUser;
    }

    public void setRh1ModifyUser(String value) {
        this.rh1ModifyUser = value;
    }

    private String rh1ModifyTime;

    public String getRh1ModifyTime() {
        return this.rh1ModifyTime;
    }

    public void setRh1ModifyTime(String value) {
        this.rh1ModifyTime = value;
    }

    private String rh1RowVersion;

    public String getRh1RowVersion() {
        return this.rh1RowVersion;
    }

    public void setRh1RowVersion(String value) {
        this.rh1RowVersion = value;
    }

    private List<ReplenishmentDetailData> children;

    public List<ReplenishmentDetailData> getChildren() {
        return children;
    }

    public void setChildren(List<ReplenishmentDetailData> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ReplenishmentHeadData [rh1Id=" + rh1Id + ", rh1M02Id=" + rh1M02Id + ", rh1Rhcode="
                + rh1Rhcode + ", rh1RhType=" + rh1RhType + ", rh1Cu1Id=" + rh1Cu1Id + ", rh1Vd1Id="
                + rh1Vd1Id + ", rh1Wh1Id=" + rh1Wh1Id + ", rh1Ce1IdPh=" + rh1Ce1IdPh
                + ", rh1DistributionRemark=" + rh1DistributionRemark + ", rh1St1Id=" + rh1St1Id
                + ", rh1Ce1IdBh=" + rh1Ce1IdBh + ", rh1ReplenishRemark=" + rh1ReplenishRemark
                + ", rh1ReplenishReason=" + rh1ReplenishReason + ", rh1OrderStatus=" + rh1OrderStatus
                + ", rh1DownloadStatus=" + rh1DownloadStatus + ", rh1UploadStatus=" + rh1UploadStatus
                + ", rh1CreateUser=" + rh1CreateUser + ", rh1CreateTime=" + rh1CreateTime
                + ", rh1ModifyUser=" + rh1ModifyUser + ", rh1ModifyTime=" + rh1ModifyTime
                + ", rh1RowVersion=" + rh1RowVersion + ", children=" + children + "]";
    }

}