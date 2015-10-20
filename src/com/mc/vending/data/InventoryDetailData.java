package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>Table: <strong>InventoryDetail　盘点单从表</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 *   <tr style="background-color:#ddd;Text-align:Left;">
 *     <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 *   </tr>
 *   <tr><td>ih2Id</td><td>{@link String}</td><td>ih2_m02_id</td><td>varchar</td><td>盘点单从表ID</td></tr>
 *   <tr><td>ih2M02Id</td><td>{@link String}</td><td>ih2_m02_id</td><td>varchar</td><td>事业体ID</td></tr>
 *   <tr><td>ih2Ih1Id</td><td>{@link String}</td><td>ih2_ih1_id</td><td>varchar</td><td>盘点单主表ID</td></tr>
 *   <tr><td>ih2St1Id</td><td>{@link String}</td><td>ih2_st1_id</td><td>varchar</td><td>站点ID</td></tr>
 *   <tr><td>ih2St1StationMaster</td><td>{@link String}</td><td>ih2_st1_stationMaster</td><td>varchar</td><td>站长编号</td></tr>
 *   <tr><td>ih2Cu1Id</td><td>{@link String}</td><td>ih2_cu1_id</td><td>varchar</td><td>客户ID</td></tr>
 *   <tr><td>ih2Vd1Id</td><td>{@link String}</td><td>ih2_vd1_id</td><td>varchar</td><td>售货机ID</td></tr>
 *   <tr><td>ih2Status</td><td>{@link String}</td><td>ih2_status</td><td>varchar</td><td>盘点状态：未盘点；已盘点</td></tr>
 *   <tr><td>ih2CreateUser</td><td>{@link String}</td><td>ih2_createUser</td><td>varchar</td><td>创建人</td></tr>
 *   <tr><td>ih2CreateTime</td><td>{@link String}</td><td>ih2_createTime</td><td>varchar</td><td>创建时间</td></tr>
 *   <tr><td>ih2ModifyUser</td><td>{@link String}</td><td>ih2_modifyUser</td><td>varchar</td><td>最后修改人</td></tr>
 *   <tr><td>ih2ModifyTime</td><td>{@link String}</td><td>ih2_modifyTime</td><td>varchar</td><td>最后修改时间</td></tr>
 *   <tr><td>ih2RowVersion</td><td>{@link String}</td><td>ih2_rowVersion</td><td>varchar</td><td>时间戳</td></tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class InventoryDetailData implements Serializable {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long   serialVersionUID = -1671821675652345706L;

    //定义盘点状态
    private static final String IH2_STATUS_NO    = "0";                  //未盘点
    private static final String IH2_STATUS_YES   = "1";                  //已盘点

    private String              ih2Id;

    public String getIh2Id() {
        return this.ih2Id;
    }

    public void setIh2Id(String value) {
        this.ih2Id = value;
    }

    private String ih2M02Id;

    public String getIh2M02Id() {
        return this.ih2M02Id;
    }

    public void setIh2M02Id(String value) {
        this.ih2M02Id = value;
    }

    private String ih2Ih1Id;

    public String getIh2Ih1Id() {
        return this.ih2Ih1Id;
    }

    public void setIh2Ih1Id(String value) {
        this.ih2Ih1Id = value;
    }

    private String ih2St1Id;

    public String getIh2St1Id() {
        return this.ih2St1Id;
    }

    public void setIh2St1Id(String value) {
        this.ih2St1Id = value;
    }

    private String ih2St1StationMaster;

    public String getIh2St1StationMaster() {
        return this.ih2St1StationMaster;
    }

    public void setIh2St1StationMaster(String value) {
        this.ih2St1StationMaster = value;
    }

    private String ih2Cu1Id;

    public String getIh2Cu1Id() {
        return this.ih2Cu1Id;
    }

    public void setIh2Cu1Id(String value) {
        this.ih2Cu1Id = value;
    }

    private String ih2Vd1Id;

    public String getIh2Vd1Id() {
        return this.ih2Vd1Id;
    }

    public void setIh2Vd1Id(String value) {
        this.ih2Vd1Id = value;
    }

    private String ih2Status;

    public String getIh2Status() {
        return this.ih2Status;
    }

    public void setIh2Status(String value) {
        this.ih2Status = value;
    }

    private String ih2CreateUser;

    public String getIh2CreateUser() {
        return this.ih2CreateUser;
    }

    public void setIh2CreateUser(String value) {
        this.ih2CreateUser = value;
    }

    private String ih2CreateTime;

    public String getIh2CreateTime() {
        return this.ih2CreateTime;
    }

    public void setIh2CreateTime(String value) {
        this.ih2CreateTime = value;
    }

    private String ih2ModifyUser;

    public String getIh2ModifyUser() {
        return this.ih2ModifyUser;
    }

    public void setIh2ModifyUser(String value) {
        this.ih2ModifyUser = value;
    }

    private String ih2ModifyTime;

    public String getIh2ModifyTime() {
        return this.ih2ModifyTime;
    }

    public void setIh2ModifyTime(String value) {
        this.ih2ModifyTime = value;
    }

    private String ih2RowVersion;

    public String getIh2RowVersion() {
        return this.ih2RowVersion;
    }

    public void setIh2RowVersion(String value) {
        this.ih2RowVersion = value;
    }

}