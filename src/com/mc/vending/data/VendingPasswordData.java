package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>VendingPassword 售货机强制密码</strong>
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
 * <td>vp3Id</td>
 * <td>{@link String}</td>
 * <td>VP3_ID</td>
 * <td>varchar</td>
 * <td>售货机强制密码ID</td>
 * </tr>
 * <tr>
 * <td>vp3M02Id</td>
 * <td>{@link String}</td>
 * <td>VP3_M02_ID</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vp3Password</td>
 * <td>{@link String}</td>
 * <td>VP3_Password</td>
 * <td>varchar</td>
 * <td>密码</td>
 * </tr>
 * <tr>
 * <td>vp3CreateUser</td>
 * <td>{@link String}</td>
 * <td>vp2_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vp3CreateTime</td>
 * <td>{@link String}</td>
 * <td>vp2_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vp3ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vp2_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vp3ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vp2_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vp3RowVersion</td>
 * <td>{@link String}</td>
 * <td>vp2_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 * 
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingPasswordData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -7731245339685646725L;
    /**
     * Comment for <code>serialVersionUID</code>
     */

    private String            vp3Id;
    private String            vp3M02Id;
    private String            vp3Password;
    private String            vp3CreateUser;
    private String            vp3CreateTime;
    private String            vp3ModifyUser;
    private String            vp3ModifyTime;
    private String            vp3RowVersion;

    public String getVp3Id() {
        return vp3Id;
    }

    public void setVp3Id(String vp3Id) {
        this.vp3Id = vp3Id;
    }

    public String getVp3M02Id() {
        return vp3M02Id;
    }

    public void setVp3M02Id(String vp3m02Id) {
        vp3M02Id = vp3m02Id;
    }

    public String getVp3Password() {
        return vp3Password;
    }

    public void setVp3Password(String vp3Password) {
        this.vp3Password = vp3Password;
    }

    public String getVp3CreateUser() {
        return vp3CreateUser;
    }

    public void setVp3CreateUser(String vp3CreateUser) {
        this.vp3CreateUser = vp3CreateUser;
    }

    public String getVp3CreateTime() {
        return vp3CreateTime;
    }

    public void setVp3CreateTime(String vp3CreateTime) {
        this.vp3CreateTime = vp3CreateTime;
    }

    public String getVp3ModifyUser() {
        return vp3ModifyUser;
    }

    public void setVp3ModifyUser(String vp3ModifyUser) {
        this.vp3ModifyUser = vp3ModifyUser;
    }

    public String getVp3ModifyTime() {
        return vp3ModifyTime;
    }

    public void setVp3ModifyTime(String vp3ModifyTime) {
        this.vp3ModifyTime = vp3ModifyTime;
    }

    public String getVp3RowVersion() {
        return vp3RowVersion;
    }

    public void setVp3RowVersion(String vp3RowVersion) {
        this.vp3RowVersion = vp3RowVersion;
    }

}
