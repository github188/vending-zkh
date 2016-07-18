package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Card 卡/密码</strong>
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
 * <td>cd1Id</td>
 * <td>{@link String}</td>
 * <td>cd1_id</td>
 * <td>varchar</td>
 * <td>卡/密码ID</td>
 * </tr>
 * <tr>
 * <td>cd1M02Id</td>
 * <td>{@link String}</td>
 * <td>cd1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>cd1SerialNo</td>
 * <td>{@link String}</td>
 * <td>cd1_serialNo</td>
 * <td>varchar</td>
 * <td>卡序列号</td>
 * </tr>
 * <tr>
 * <td>cd1Code</td>
 * <td>{@link String}</td>
 * <td>cd1_code</td>
 * <td>varchar</td>
 * <td>卡号</td>
 * </tr>
 * <tr>
 * <td>cd1Type</td>
 * <td>{@link String}</td>
 * <td>cd1_type</td>
 * <td>varchar</td>
 * <td>卡类型:1 震坤行内部卡号；2 客户卡号</td>
 * </tr>
 * <tr>
 * <td>cd1Password</td>
 * <td>{@link String}</td>
 * <td>cd1_password</td>
 * <td>varchar</td>
 * <td>密码</td>
 * </tr>
 * <tr>
 * <td>cd1Purpose</td>
 * <td>{@link String}</td>
 * <td>cd1_purpose</td>
 * <td>varchar</td>
 * <td>用途</td>
 * </tr>
 * <tr>
 * <td>cd1Status</td>
 * <td>{@link String}</td>
 * <td>cd1_status</td>
 * <td>varchar</td>
 * <td>震坤行状态:1 可用、0 不可用</td>
 * </tr>
 * <tr>
 * <td>cd1CustomerStatus</td>
 * <td>{@link String}</td>
 * <td>cd1_customerStatus</td>
 * <td>varchar</td>
 * <td>客户状态:1 可用、0 不可用</td>
 * </tr>
 * <tr>
 * <td>cd1CreateUser</td>
 * <td>{@link String}</td>
 * <td>cd1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>cd1CreateTime</td>
 * <td>{@link String}</td>
 * <td>cd1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>cd1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>cd1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>cd1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>cd1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>cd1RowVersion</td>
 * <td>{@link String}</td>
 * <td>cd1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class CardData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID          = 4971250362917376782L;

    // 传参常量定义
    public static final String CARD_PASSWORD_PARAM       = "password";          // 传入参数为密码
    public static final String CARD_SERIALNO_PARAM       = "serialNo";          // 传入参数为卡序列号

    // 定义卡/密码表的卡类型
    public static final String CARD_TYPE_ZKH             = "0";                 // 震坤行内部卡号；
    public static final String CARD_TYPE_CUS             = "1";                 // 客户卡号

    // 定义卡/密码表的产品权限类型
    public static final String CARD_PRODUCTPOWER_VENDING = "0";                 // 卡-售货机权限；
    public static final String CARD_PRODUCTPOWER_PRODUCT = "1";                 // 卡-产品权限

    // 定义卡/密码表的震坤行状态
    public static final String CARD_STATUS_YES           = "0";                 // 可用
    public static final String CARD_STATUS_NO            = "1";                 // 不可用

    // 定义卡/密码表的客户状态
    public static final String CARD_CUS_STATUS_YES       = "0";                 // 可用
    public static final String CARD_CUS_STATUS_NO        = "1";                 // 不可用

    private String             cd1Id;

    public String getCd1Id() {
        return this.cd1Id;
    }

    public void setCd1Id(String value) {
        this.cd1Id = value;
    }

    private String cd1M02Id;

    public String getCd1M02Id() {
        return this.cd1M02Id;
    }

    public void setCd1M02Id(String value) {
        this.cd1M02Id = value;
    }

    private String cd1SerialNo;

    public String getCd1SerialNo() {
        return this.cd1SerialNo;
    }

    public void setCd1SerialNo(String value) {
        this.cd1SerialNo = value;
    }

    private String cd1Code;

    public String getCd1Code() {
        return this.cd1Code;
    }

    public void setCd1Code(String value) {
        this.cd1Code = value;
    }

    private String cd1Type;

    public String getCd1Type() {
        return this.cd1Type;
    }

    public void setCd1Type(String value) {
        this.cd1Type = value;
    }

    private String cd1Password;

    public String getCd1Password() {
        return this.cd1Password;
    }

    public void setCd1Password(String value) {
        this.cd1Password = value;
    }

    private String cd1Purpose;

    public String getCd1Purpose() {
        return this.cd1Purpose;
    }

    public void setCd1Purpose(String value) {
        this.cd1Purpose = value;
    }

    private String cd1Status;

    public String getCd1Status() {
        return this.cd1Status;
    }

    public void setCd1Status(String value) {
        this.cd1Status = value;
    }

    private String cd1CustomerStatus;

    public String getCd1CustomerStatus() {
        return this.cd1CustomerStatus;
    }

    public void setCd1CustomerStatus(String value) {
        this.cd1CustomerStatus = value;
    }

    private String cd1ProductPower;

    public String getCd1ProductPower() {
        return cd1ProductPower;
    }

    public void setCd1ProductPower(String cd1ProductPower) {
        this.cd1ProductPower = cd1ProductPower;
    }

    private String cd1CreateUser;

    public String getCd1CreateUser() {
        return this.cd1CreateUser;
    }

    public void setCd1CreateUser(String value) {
        this.cd1CreateUser = value;
    }

    private String cd1CreateTime;

    public String getCd1CreateTime() {
        return this.cd1CreateTime;
    }

    public void setCd1CreateTime(String value) {
        this.cd1CreateTime = value;
    }

    private String cd1ModifyUser;

    public String getCd1ModifyUser() {
        return this.cd1ModifyUser;
    }

    public void setCd1ModifyUser(String value) {
        this.cd1ModifyUser = value;
    }

    private String cd1ModifyTime;

    public String getCd1ModifyTime() {
        return this.cd1ModifyTime;
    }

    public void setCd1ModifyTime(String value) {
        this.cd1ModifyTime = value;
    }

    private String cd1RowVersion;

    public String getCd1RowVersion() {
        return this.cd1RowVersion;
    }

    public void setCd1RowVersion(String value) {
        this.cd1RowVersion = value;
    }


	@Override
    public String toString() {
        return "CardData [cd1Id=" + cd1Id + ", cd1M02Id=" + cd1M02Id + ", cd1SerialNo=" + cd1SerialNo
                + ", cd1Code=" + cd1Code + ", cd1Type=" + cd1Type + ", cd1Password=" + cd1Password
                + ", cd1Purpose=" + cd1Purpose + ", cd1Status=" + cd1Status + ", cd1CustomerStatus="
                + cd1CustomerStatus + ", cd1ProductPower=" + cd1ProductPower + ", cd1CreateUser="
                + cd1CreateUser + ", cd1CreateTime=" + cd1CreateTime + ", cd1ModifyUser=" + cd1ModifyUser
                + ", cd1ModifyTime=" + cd1ModifyTime + ", cd1RowVersion=" + cd1RowVersion + "]";
    }

}