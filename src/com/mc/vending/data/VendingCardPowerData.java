package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>VendingCardPower 售货机卡/密码权限</strong>
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
 * <td>vc2Id</td>
 * <td>{@link String}</td>
 * <td>vc2_id</td>
 * <td>varchar</td>
 * <td>售货机卡/密码权限ID</td>
 * </tr>
 * <tr>
 * <td>vc2M02Id</td>
 * <td>{@link String}</td>
 * <td>vc2_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vc2Cu1Id</td>
 * <td>{@link String}</td>
 * <td>vc2_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>vc2Vd1Id</td>
 * <td>{@link String}</td>
 * <td>vc2_vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>vc2Cd1Id</td>
 * <td>{@link String}</td>
 * <td>vc2_cd1_id</td>
 * <td>varchar</td>
 * <td>卡ID</td>
 * </tr>
 * <tr>
 * <td>vc2CreateUser</td>
 * <td>{@link String}</td>
 * <td>vc2_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vc2CreateTime</td>
 * <td>{@link String}</td>
 * <td>vc2_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vc2ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vc2_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vc2ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vc2_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vc2RowVersion</td>
 * <td>{@link String}</td>
 * <td>vc2_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingCardPowerData extends BaseParseData implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -581326770697470250L;

    private String            vc2Id;

    public String getVc2Id() {
        return this.vc2Id;
    }

    public void setVc2Id(String value) {
        this.vc2Id = value;
    }

    private String vc2M02Id;

    public String getVc2M02Id() {
        return this.vc2M02Id;
    }

    public void setVc2M02Id(String value) {
        this.vc2M02Id = value;
    }

    private String vc2Cu1Id;

    public String getVc2Cu1Id() {
        return this.vc2Cu1Id;
    }

    public void setVc2Cu1Id(String value) {
        this.vc2Cu1Id = value;
    }

    private String vc2Vd1Id;

    public String getVc2Vd1Id() {
        return this.vc2Vd1Id;
    }

    public void setVc2Vd1Id(String value) {
        this.vc2Vd1Id = value;
    }

    private String vc2Cd1Id;

    public String getVc2Cd1Id() {
        return this.vc2Cd1Id;
    }

    public void setVc2Cd1Id(String value) {
        this.vc2Cd1Id = value;
    }

    private String vc2CreateUser;

    public String getVc2CreateUser() {
        return this.vc2CreateUser;
    }

    public void setVc2CreateUser(String value) {
        this.vc2CreateUser = value;
    }

    private String vc2CreateTime;

    public String getVc2CreateTime() {
        return this.vc2CreateTime;
    }

    public void setVc2CreateTime(String value) {
        this.vc2CreateTime = value;
    }

    private String vc2ModifyUser;

    public String getVc2ModifyUser() {
        return this.vc2ModifyUser;
    }

    public void setVc2ModifyUser(String value) {
        this.vc2ModifyUser = value;
    }

    private String vc2ModifyTime;

    public String getVc2ModifyTime() {
        return this.vc2ModifyTime;
    }

    public void setVc2ModifyTime(String value) {
        this.vc2ModifyTime = value;
    }

    private String vc2RowVersion;

    public String getVc2RowVersion() {
        return this.vc2RowVersion;
    }

    public void setVc2RowVersion(String value) {
        this.vc2RowVersion = value;
    }

    @Override
    public String toString() {
        return "VendingCardPowerData [vc2Id=" + vc2Id + ", vc2M02Id=" + vc2M02Id + ", vc2Cu1Id=" + vc2Cu1Id
                + ", vc2Vd1Id=" + vc2Vd1Id + ", vc2Cd1Id=" + vc2Cd1Id + ", vc2CreateUser=" + vc2CreateUser
                + ", vc2CreateTime=" + vc2CreateTime + ", vc2ModifyUser=" + vc2ModifyUser
                + ", vc2ModifyTime=" + vc2ModifyTime + ", vc2RowVersion=" + vc2RowVersion + "]";
    }

}