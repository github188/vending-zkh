package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>ProductGroupPower 产品组合权限表</strong>
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
 * <td>pp1Id</td>
 * <td>{@link String}</td>
 * <td>pp1_id</td>
 * <td>varchar</td>
 * <td>产品组合权限表ID</td>
 * </tr>
 * <tr>
 * <td>pp1M02Id</td>
 * <td>{@link String}</td>
 * <td>pp1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>pp1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>pp1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>pp1Pg1Id</td>
 * <td>{@link String}</td>
 * <td>pp1_pg1_id</td>
 * <td>varchar</td>
 * <td>产品组合主表ID</td>
 * </tr>
 * <tr>
 * <td>pp1Cd1Id</td>
 * <td>{@link String}</td>
 * <td>pp1_cd1_id</td>
 * <td>varchar</td>
 * <td>卡ID</td>
 * </tr>
 * <tr>
 * <td>pp1Power</td>
 * <td>{@link String}</td>
 * <td>pp1_power</td>
 * <td>varchar</td>
 * <td>权限</td>
 * </tr>
 * <tr>
 * <td>pp1Period</td>
 * <td>{@link String}</td>
 * <td>pp1_period</td>
 * <td>varchar</td>
 * <td>期间设置</td>
 * </tr>
 * <tr>
 * <td>pp1IntervalStart</td>
 * <td>{@link String}</td>
 * <td>pp1_intervalStart</td>
 * <td>varchar</td>
 * <td>间隔开始</td>
 * </tr>
 * <tr>
 * <td>pp1IntervalFinish</td>
 * <td>{@link String}</td>
 * <td>pp1_intervalFinish</td>
 * <td>varchar</td>
 * <td>间隔结束</td>
 * </tr>
 * <tr>
 * <td>pp1StartDate</td>
 * <td>{@link String}</td>
 * <td>pp1_startDate</td>
 * <td>varchar</td>
 * <td>起始时间</td>
 * </tr>
 * <tr>
 * <td>pp1PeriodNum</td>
 * <td>{@link Integer}</td>
 * <td>pp1_periodNum</td>
 * <td>int</td>
 * <td>期间领料数</td>
 * </tr>
 * <tr>
 * <td>pp1CreateUser</td>
 * <td>{@link String}</td>
 * <td>pp1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>pp1CreateTime</td>
 * <td>{@link String}</td>
 * <td>pp1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>pp1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>pp1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>pp1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>pp1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>pp1RowVersion</td>
 * <td>{@link String}</td>
 * <td>pp1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ProductGroupPowerData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID = -7560204188382045659L;

    // 定义权限常量
    public static final String GROUP_POWER_YES  = "0";                  // 有权限
    public static final String GROUP_POWER_NO   = "1";                  // 无权限

    private String             pp1M02Id;

    private String             pp1Cu1Id;

    private String             pp1Id;

    private String             pp1Pg1Id;

    private String             pp1Cd1Id;

    private String             pp1Power;

    private String             pp1Period;

    private String             pp1IntervalStart;

    private String             pp1IntervalFinish;

    private String             pp1StartDate;

    private Integer            pp1PeriodNum;

    private String             pp1CreateUser;

    private String             pp1CreateTime;

    private String             pp1ModifyUser;

    private String             pp1ModifyTime;

    private String             pp1RowVersion;

    public String getPp1M02Id() {
        return pp1M02Id;
    }

    public void setPp1M02Id(String pp1m02Id) {
        pp1M02Id = pp1m02Id;
    }

    public String getPp1Cu1Id() {
        return pp1Cu1Id;
    }

    public void setPp1Cu1Id(String pp1Cu1Id) {
        this.pp1Cu1Id = pp1Cu1Id;
    }

    public String getPp1Id() {
        return pp1Id;
    }

    public void setPp1Id(String pp1Id) {
        this.pp1Id = pp1Id;
    }

    public String getPp1Pg1Id() {
        return pp1Pg1Id;
    }

    public void setPp1Pg1Id(String pp1Pg1Id) {
        this.pp1Pg1Id = pp1Pg1Id;
    }

    public String getPp1Cd1Id() {
        return pp1Cd1Id;
    }

    public void setPp1Cd1Id(String pp1Cd1Id) {
        this.pp1Cd1Id = pp1Cd1Id;
    }

    public String getPp1Power() {
        return pp1Power;
    }

    public void setPp1Power(String pp1Power) {
        this.pp1Power = pp1Power;
    }

    public String getPp1Period() {
        return pp1Period;
    }

    public void setPp1Period(String pp1Period) {
        this.pp1Period = pp1Period;
    }

    public String getPp1IntervalStart() {
        return pp1IntervalStart;
    }

    public void setPp1IntervalStart(String pp1IntervalStart) {
        this.pp1IntervalStart = pp1IntervalStart;
    }

    public String getPp1IntervalFinish() {
        return pp1IntervalFinish;
    }

    public void setPp1IntervalFinish(String pp1IntervalFinish) {
        this.pp1IntervalFinish = pp1IntervalFinish;
    }

    public String getPp1StartDate() {
        return pp1StartDate;
    }

    public void setPp1StartDate(String pp1StartDate) {
        this.pp1StartDate = pp1StartDate;
    }

    public Integer getPp1PeriodNum() {
        return pp1PeriodNum;
    }

    public void setPp1PeriodNum(Integer pp1PeriodNum) {
        this.pp1PeriodNum = pp1PeriodNum;
    }

    public String getPp1CreateUser() {
        return pp1CreateUser;
    }

    public void setPp1CreateUser(String pp1CreateUser) {
        this.pp1CreateUser = pp1CreateUser;
    }

    public String getPp1CreateTime() {
        return pp1CreateTime;
    }

    public void setPp1CreateTime(String pp1CreateTime) {
        this.pp1CreateTime = pp1CreateTime;
    }

    public String getPp1ModifyUser() {
        return pp1ModifyUser;
    }

    public void setPp1ModifyUser(String pp1ModifyUser) {
        this.pp1ModifyUser = pp1ModifyUser;
    }

    public String getPp1ModifyTime() {
        return pp1ModifyTime;
    }

    public void setPp1ModifyTime(String pp1ModifyTime) {
        this.pp1ModifyTime = pp1ModifyTime;
    }

    public String getPp1RowVersion() {
        return pp1RowVersion;
    }

    public void setPp1RowVersion(String pp1RowVersion) {
        this.pp1RowVersion = pp1RowVersion;
    }

    @Override
    public String toString() {
        return "ProductGroupPowerData [pp1M02Id=" + pp1M02Id + ", pp1Cu1Id=" + pp1Cu1Id + ", pp1Id=" + pp1Id
                + ", pp1Pg1Id=" + pp1Pg1Id + ", pp1Cd1Id=" + pp1Cd1Id + ", pp1Power=" + pp1Power
                + ", pp1Period=" + pp1Period + ", pp1IntervalStart=" + pp1IntervalStart
                + ", pp1IntervalFinish=" + pp1IntervalFinish + ", pp1StartDate=" + pp1StartDate
                + ", pp1PeriodNum=" + pp1PeriodNum + ", pp1CreateUser=" + pp1CreateUser + ", pp1CreateTime="
                + pp1CreateTime + ", pp1ModifyUser=" + pp1ModifyUser + ", pp1ModifyTime=" + pp1ModifyTime
                + ", pp1RowVersion=" + pp1RowVersion + "]";
    }

}