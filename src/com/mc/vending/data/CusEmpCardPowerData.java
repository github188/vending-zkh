package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>CusEmpCardPower 客户员工卡/密码权限</strong>
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
 * <td>ce2Id</td>
 * <td>{@link String}</td>
 * <td>ce2_id</td>
 * <td>varchar</td>
 * <td>客户员工卡/密码权限记录ID</td>
 * </tr>
 * <tr>
 * <td>ce2M02Id</td>
 * <td>{@link String}</td>
 * <td>ce2_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>ce2Ce1Id</td>
 * <td>{@link String}</td>
 * <td>ce2_ce1_id</td>
 * <td>varchar</td>
 * <td>客户员工ID</td>
 * </tr>
 * <tr>
 * <td>ce2Cd1Id</td>
 * <td>{@link String}</td>
 * <td>ce2_cd1_id</td>
 * <td>varchar</td>
 * <td>卡ID</td>
 * </tr>
 * <tr>
 * <td>ce2CreateUser</td>
 * <td>{@link String}</td>
 * <td>ce2_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>ce2CreateTime</td>
 * <td>{@link String}</td>
 * <td>ce2_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>ce2ModifyUser</td>
 * <td>{@link String}</td>
 * <td>ce2_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>ce2ModifyTime</td>
 * <td>{@link String}</td>
 * <td>ce2_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>ce2RowVersion</td>
 * <td>{@link String}</td>
 * <td>ce2_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class CusEmpCardPowerData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3710661701687481442L;

    private String            ce2Id;

    public String getCe2Id() {
        return this.ce2Id;
    }

    public void setCe2Id(String value) {
        this.ce2Id = value;
    }

    private String ce2M02Id;

    public String getCe2M02Id() {
        return this.ce2M02Id;
    }

    public void setCe2M02Id(String value) {
        this.ce2M02Id = value;
    }

    private String ce2Ce1Id;

    public String getCe2Ce1Id() {
        return this.ce2Ce1Id;
    }

    public void setCe2Ce1Id(String value) {
        this.ce2Ce1Id = value;
    }

    private String ce2Cd1Id;

    public String getCe2Cd1Id() {
        return this.ce2Cd1Id;
    }

    public void setCe2Cd1Id(String value) {
        this.ce2Cd1Id = value;
    }

    private String ce2CreateUser;

    public String getCe2CreateUser() {
        return this.ce2CreateUser;
    }

    public void setCe2CreateUser(String value) {
        this.ce2CreateUser = value;
    }

    private String ce2CreateTime;

    public String getCe2CreateTime() {
        return this.ce2CreateTime;
    }

    public void setCe2CreateTime(String value) {
        this.ce2CreateTime = value;
    }

    private String ce2ModifyUser;

    public String getCe2ModifyUser() {
        return this.ce2ModifyUser;
    }

    public void setCe2ModifyUser(String value) {
        this.ce2ModifyUser = value;
    }

    private String ce2ModifyTime;

    public String getCe2ModifyTime() {
        return this.ce2ModifyTime;
    }

    public void setCe2ModifyTime(String value) {
        this.ce2ModifyTime = value;
    }

    private String ce2RowVersion;

    public String getCe2RowVersion() {
        return this.ce2RowVersion;
    }

    public void setCe2RowVersion(String value) {
        this.ce2RowVersion = value;
    }

    @Override
    public String toString() {
        return "CusEmpCardPowerData [ce2Id=" + ce2Id + ", ce2M02Id=" + ce2M02Id + ", ce2Ce1Id=" + ce2Ce1Id
                + ", ce2Cd1Id=" + ce2Cd1Id + ", ce2CreateUser=" + ce2CreateUser + ", ce2CreateTime="
                + ce2CreateTime + ", ce2ModifyUser=" + ce2ModifyUser + ", ce2ModifyTime=" + ce2ModifyTime
                + ", ce2RowVersion=" + ce2RowVersion + "]";
    }

}