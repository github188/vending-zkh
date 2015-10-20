package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>ProductMaterialPower　产品领料权限</strong>
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
 * <td>pm1Id</td>
 * <td>{@link String}</td>
 * <td>pm1_id</td>
 * <td>varchar</td>
 * <td>产品领料权限ID</td>
 * </tr>
 * <tr>
 * <td>pm1M02Id</td>
 * <td>{@link String}</td>
 * <td>pm1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>pm1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>pm1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>pm1Vc2Id</td>
 * <td>{@link String}</td>
 * <td>pm1_vc2_id</td>
 * <td>varchar</td>
 * <td>售货机卡/密码权限ID</td>
 * </tr>
 * <tr>
 * <td>pm1Vp1Id</td>
 * <td>{@link String}</td>
 * <td>pm1_vp1_id</td>
 * <td>varchar</td>
 * <td>售货机产品ID</td>
 * </tr>
 * <tr>
 * <td>pm1Power</td>
 * <td>{@link String}</td>
 * <td>pm1_power</td>
 * <td>varchar</td>
 * <td>权限</td>
 * </tr>
 * <tr>
 * <td>pm1OnceQty</td>
 * <td>{@link Integer}</td>
 * <td>pm1_onceQty</td>
 * <td>int</td>
 * <td>一次领料数</td>
 * </tr>
 * <tr>
 * <td>pm1Period</td>
 * <td>{@link String}</td>
 * <td>pm1_period</td>
 * <td>varchar</td>
 * <td>期间设置</td>
 * </tr>
 * <tr>
 * <td>pm1IntervalStart</td>
 * <td>{@link String}</td>
 * <td>pm1_intervalStart</td>
 * <td>varchar</td>
 * <td>间隔开始</td>
 * </tr>
 * <tr>
 * <td>pm1IntervalFinish</td>
 * <td>{@link String}</td>
 * <td>pm1_intervalFinish</td>
 * <td>varchar</td>
 * <td>间隔结束</td>
 * </tr>
 * <tr>
 * <td>pm1StartDate</td>
 * <td>{@link String}</td>
 * <td>pm1_start</td>
 * <td>varchar</td>
 * <td>起始时间</td>
 * </tr>
 * <tr>
 * <td>pm1PeriodQty</td>
 * <td>{@link Integer}</td>
 * <td>pm1_periodQty</td>
 * <td>int</td>
 * <td>期间领料数</td>
 * </tr>
 * <tr>
 * <td>pm1CreateUser</td>
 * <td>{@link String}</td>
 * <td>pm1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>pm1CreateTime</td>
 * <td>{@link String}</td>
 * <td>pm1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>pm1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>pm1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>pm1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>pm1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>pm1RowVersion</td>
 * <td>{@link String}</td>
 * <td>pm1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ProductMaterialPowerData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID   = 6916042653966560600L;

    // 定义权限常量
    public static final String MATERIAL_POWER_YES = "0";                 // 有权限
    public static final String MATERIAL_POWER_NO  = "1";                 // 无权限

    private String             pm1Id;

    public String getPm1Id() {
        return this.pm1Id;
    }

    public void setPm1Id(String value) {
        this.pm1Id = value;
    }

    private String pm1M02Id;

    public String getPm1M02Id() {
        return this.pm1M02Id;
    }

    public void setPm1M02Id(String value) {
        this.pm1M02Id = value;
    }

    private String pm1Cu1Id;

    public String getPm1Cu1Id() {
        return this.pm1Cu1Id;
    }

    public void setPm1Cu1Id(String value) {
        this.pm1Cu1Id = value;
    }

    private String pm1Vc2Id;

    public String getPm1Vc2Id() {
        return this.pm1Vc2Id;
    }

    public void setPm1Vc2Id(String value) {
        this.pm1Vc2Id = value;
    }

    private String pm1Vp1Id;

    public String getPm1Vp1Id() {
        return this.pm1Vp1Id;
    }

    public void setPm1Vp1Id(String value) {
        this.pm1Vp1Id = value;
    }

    private String pm1Power;

    public String getPm1Power() {
        return this.pm1Power;
    }

    public void setPm1Power(String value) {
        this.pm1Power = value;
    }

    private Integer pm1OnceQty;

    public Integer getPm1OnceQty() {
        return this.pm1OnceQty;
    }

    public void setPm1OnceQty(Integer value) {
        this.pm1OnceQty = value;
    }

    private String pm1Period;

    public String getPm1Period() {
        return this.pm1Period;
    }

    public void setPm1Period(String value) {
        this.pm1Period = value;
    }

    private String pm1IntervalStart;

    public String getPm1IntervalStart() {
        return this.pm1IntervalStart;
    }

    public void setPm1IntervalStart(String value) {
        this.pm1IntervalStart = value;
    }

    private String pm1IntervalFinish;

    public String getPm1IntervalFinish() {
        return this.pm1IntervalFinish;
    }

    public void setPm1IntervalFinish(String value) {
        this.pm1IntervalFinish = value;
    }

    private String pm1StartDate;

    public String getPm1StartDate() {
        return this.pm1StartDate;
    }

    public void setPm1StartDate(String value) {
        this.pm1StartDate = value;
    }

    private Integer pm1PeriodQty;

    public Integer getPm1PeriodQty() {
        return this.pm1PeriodQty;
    }

    public void setPm1PeriodQty(Integer value) {
        this.pm1PeriodQty = value;
    }

    private String pm1CreateUser;

    public String getPm1CreateUser() {
        return this.pm1CreateUser;
    }

    public void setPm1CreateUser(String value) {
        this.pm1CreateUser = value;
    }

    private String pm1CreateTime;

    public String getPm1CreateTime() {
        return this.pm1CreateTime;
    }

    public void setPm1CreateTime(String value) {
        this.pm1CreateTime = value;
    }

    private String pm1ModifyUser;

    public String getPm1ModifyUser() {
        return this.pm1ModifyUser;
    }

    public void setPm1ModifyUser(String value) {
        this.pm1ModifyUser = value;
    }

    private String pm1ModifyTime;

    public String getPm1ModifyTime() {
        return this.pm1ModifyTime;
    }

    public void setPm1ModifyTime(String value) {
        this.pm1ModifyTime = value;
    }

    private String pm1RowVersion;

    public String getPm1RowVersion() {
        return this.pm1RowVersion;
    }

    public void setPm1RowVersion(String value) {
        this.pm1RowVersion = value;
    }

    @Override
    public String toString() {
        return "ProductMaterialPowerData [pm1Id=" + pm1Id + ", pm1M02Id=" + pm1M02Id + ", pm1Cu1Id="
                + pm1Cu1Id + ", pm1Vc2Id=" + pm1Vc2Id + ", pm1Vp1Id=" + pm1Vp1Id + ", pm1Power=" + pm1Power
                + ", pm1OnceQty=" + pm1OnceQty + ", pm1Period=" + pm1Period + ", pm1IntervalStart="
                + pm1IntervalStart + ", pm1IntervalFinish=" + pm1IntervalFinish + ", pm1StartDate="
                + pm1StartDate + ", pm1PeriodQty=" + pm1PeriodQty + ", pm1CreateUser=" + pm1CreateUser
                + ", pm1CreateTime=" + pm1CreateTime + ", pm1ModifyUser=" + pm1ModifyUser
                + ", pm1ModifyTime=" + pm1ModifyTime + ", pm1RowVersion=" + pm1RowVersion + "]";
    }

}