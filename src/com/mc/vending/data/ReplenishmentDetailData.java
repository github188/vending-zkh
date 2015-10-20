package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>ReplenishmentDetail　补货单从表</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>rh2Id</td><td>{@link String}</td><td>rh2_id</td><td>varchar</td><td>补货单从表ID</td></tr>
 *   <tr><td>rh2M02Id</td><td>{@link String}</td><td>rh2_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>rh2Rh1Id</td><td>{@link String}</td><td>rh2_rh1_id</td><td>varchar</td><td>补货单主表ID</td></tr>
 *   <tr><td>rh2Vc1Code</td><td>{@link String}</td><td>rh2_vc1_code</td><td>varchar</td><td>售货机货道编号</td></tr>
 *   <tr><td>rh2Pd1Id</td><td>{@link String}</td><td>rh2_pd1_id</td><td>varchar</td><td>SKUID</td></tr>
 *   <tr><td>rh2SaleType</td><td>{@link String}</td><td>rh2_saleType</td><td>varchar</td><td>销售类型</td></tr>
 *   <tr><td>rh2Sp1Id</td><td>{@link String}</td><td>rh2_sp1_id</td><td>varchar</td><td>货主ID</td></tr>
 *   <tr><td>rh2ActualQty</td><td>{@link Integer}</td><td>rh2_actualQty</td><td>int</td><td>实际补货数</td></tr>
 *   <tr><td>rh2DifferentiaQty</td><td>{@link Integer}</td><td>rh2_differentiaQty</td><td>int</td><td>补货差异</td></tr>
 *   <tr><td>rh2Rp1Id</td><td>{@link String}</td><td>rh2_rp1_id</td><td>varchar</td><td>补货计划ID</td></tr>
 *   <tr><td>rh2UploadStatus</td><td>{@link String}</td><td>rh2_UploadStatus</td><td>varchar</td><td>上传状态</td></tr>
 *   <tr><td>rh2CreateUser</td><td>{@link String}</td><td>rh2_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>rh2CreateTime</td><td>{@link String}</td><td>rh2_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>rh2ModifyUser</td><td>{@link String}</td><td>rh2_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>rh2ModifyTime</td><td>{@link String}</td><td>rh2_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>rh2RowVersion</td><td>{@link String}</td><td>rh2_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ReplenishmentDetailData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */

    //定义上传状态
    public static final String UPLOAD_UNLOAD    = "0";                  //未上传
    public static final String UPLOAD_LOAD      = "1";                  //已上传

    private static final long  serialVersionUID = -6106463384508774391L;

    private String             rh2Id;

    public String getRh2Id() {
        return this.rh2Id;
    }

    public void setRh2Id(String value) {
        this.rh2Id = value;
    }

    private String rh2M02Id;

    public String getRh2M02Id() {
        return this.rh2M02Id;
    }

    public void setRh2M02Id(String value) {
        this.rh2M02Id = value;
    }

    private String rh2Rh1Id;

    public String getRh2Rh1Id() {
        return this.rh2Rh1Id;
    }

    public void setRh2Rh1Id(String value) {
        this.rh2Rh1Id = value;
    }

    private String rh2Vc1Code;

    public String getRh2Vc1Code() {
        return this.rh2Vc1Code;
    }

    public void setRh2Vc1Code(String value) {
        this.rh2Vc1Code = value;
    }

    private String rh2Pd1Id;

    public String getRh2Pd1Id() {
        return this.rh2Pd1Id;
    }

    public void setRh2Pd1Id(String value) {
        this.rh2Pd1Id = value;
    }

    private String rh2SaleType;

    public String getRh2SaleType() {
        return this.rh2SaleType;
    }

    public void setRh2SaleType(String value) {
        this.rh2SaleType = value;
    }

    private String rh2Sp1Id;

    public String getRh2Sp1Id() {
        return this.rh2Sp1Id;
    }

    public void setRh2Sp1Id(String value) {
        this.rh2Sp1Id = value;
    }

    private Integer rh2ActualQty;

    public Integer getRh2ActualQty() {
        return this.rh2ActualQty;
    }

    public void setRh2ActualQty(Integer value) {
        this.rh2ActualQty = value;
    }

    private Integer rh2DifferentiaQty;

    public Integer getRh2DifferentiaQty() {
        return this.rh2DifferentiaQty;
    }

    public void setRh2DifferentiaQty(Integer value) {
        this.rh2DifferentiaQty = value;
    }

    private String rh2Rp1Id;

    public String getRh2Rp1Id() {
        return this.rh2Rp1Id;
    }

    public void setRh2Rp1Id(String value) {
        this.rh2Rp1Id = value;
    }

    private String rh2UploadStatus;

    public String getRh2UploadStatus() {
        return rh2UploadStatus;
    }

    public void setRh2UploadStatus(String rh2UploadStatus) {
        this.rh2UploadStatus = rh2UploadStatus;
    }

    private String rh2CreateUser;

    public String getRh2CreateUser() {
        return this.rh2CreateUser;
    }

    public void setRh2CreateUser(String value) {
        this.rh2CreateUser = value;
    }

    private String rh2CreateTime;

    public String getRh2CreateTime() {
        return this.rh2CreateTime;
    }

    public void setRh2CreateTime(String value) {
        this.rh2CreateTime = value;
    }

    private String rh2ModifyUser;

    public String getRh2ModifyUser() {
        return this.rh2ModifyUser;
    }

    public void setRh2ModifyUser(String value) {
        this.rh2ModifyUser = value;
    }

    private String rh2ModifyTime;

    public String getRh2ModifyTime() {
        return this.rh2ModifyTime;
    }

    public void setRh2ModifyTime(String value) {
        this.rh2ModifyTime = value;
    }

    private String rh2RowVersion;

    public String getRh2RowVersion() {
        return this.rh2RowVersion;
    }

    public void setRh2RowVersion(String value) {
        this.rh2RowVersion = value;
    }

    @Override
    public String toString() {
        return "ReplenishmentDetailData [rh2Id=" + rh2Id + ", rh2M02Id=" + rh2M02Id + ", rh2Rh1Id="
               + rh2Rh1Id + ", rh2Vc1Code=" + rh2Vc1Code + ", rh2Pd1Id=" + rh2Pd1Id
               + ", rh2SaleType=" + rh2SaleType + ", rh2Sp1Id=" + rh2Sp1Id + ", rh2ActualQty="
               + rh2ActualQty + ", rh2DifferentiaQty=" + rh2DifferentiaQty + ", rh2Rp1Id="
               + rh2Rp1Id + ", rh2UploadStatus=" + rh2UploadStatus + ", rh2CreateUser="
               + rh2CreateUser + ", rh2CreateTime=" + rh2CreateTime + ", rh2ModifyUser="
               + rh2ModifyUser + ", rh2ModifyTime=" + rh2ModifyTime + ", rh2RowVersion="
               + rh2RowVersion + "]";
    }

}