package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>VendingProLink　售货机产品</strong>
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
 * <td>vp1Id</td>
 * <td>{@link String}</td>
 * <td>vp1_id</td>
 * <td>varchar</td>
 * <td>售货机产品ID</td>
 * </tr>
 * <tr>
 * <td>vp1M02Id</td>
 * <td>{@link String}</td>
 * <td>vp1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vp1Vd1Id</td>
 * <td>{@link String}</td>
 * <td>vp1_vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>vp1Pd1Id</td>
 * <td>{@link String}</td>
 * <td>vp1_pd1_id</td>
 * <td>varchar</td>
 * <td>SKUID</td>
 * </tr>
 * <tr>
 * <td>vp1PromptValue</td>
 * <td>{@link Integer}</td>
 * <td>vp1_promptValue</td>
 * <td>int</td>
 * <td>产品补货提示点</td>
 * </tr>
 * <tr>
 * <td>vp1WarningValue</td>
 * <td>{@link Integer}</td>
 * <td>vp1_warningValue</td>
 * <td>int</td>
 * <td>产品库存警戒值</td>
 * </tr>
 * <tr>
 * <td>vp1CreateUser</td>
 * <td>{@link String}</td>
 * <td>vp1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vp1CreateTime</td>
 * <td>{@link String}</td>
 * <td>vp1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vp1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vp1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vp1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vp1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vp1RowVersion</td>
 * <td>{@link String}</td>
 * <td>vp1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingProLinkData extends BaseParseData implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2869075970367560196L;

    private String            vp1Id;

    public String getVp1Id() {
        return this.vp1Id;
    }

    public void setVp1Id(String value) {
        this.vp1Id = value;
    }

    private String vp1M02Id;

    public String getVp1M02Id() {
        return this.vp1M02Id;
    }

    public void setVp1M02Id(String value) {
        this.vp1M02Id = value;
    }

    private String vp1Vd1Id;

    public String getVp1Vd1Id() {
        return this.vp1Vd1Id;
    }

    public void setVp1Vd1Id(String value) {
        this.vp1Vd1Id = value;
    }

    private String vp1Pd1Id;

    public String getVp1Pd1Id() {
        return this.vp1Pd1Id;
    }

    public void setVp1Pd1Id(String value) {
        this.vp1Pd1Id = value;
    }

    private Integer vp1PromptValue;

    public Integer getVp1PromptValue() {
        return this.vp1PromptValue;
    }

    public void setVp1PromptValue(Integer value) {
        this.vp1PromptValue = value;
    }

    private Integer vp1WarningValue;

    public Integer getVp1WarningValue() {
        return this.vp1WarningValue;
    }

    public void setVp1WarningValue(Integer value) {
        this.vp1WarningValue = value;
    }

    private String vp1CreateUser;

    public String getVp1CreateUser() {
        return this.vp1CreateUser;
    }

    public void setVp1CreateUser(String value) {
        this.vp1CreateUser = value;
    }

    private String vp1CreateTime;

    public String getVp1CreateTime() {
        return this.vp1CreateTime;
    }

    public void setVp1CreateTime(String value) {
        this.vp1CreateTime = value;
    }

    private String vp1ModifyUser;

    public String getVp1ModifyUser() {
        return this.vp1ModifyUser;
    }

    public void setVp1ModifyUser(String value) {
        this.vp1ModifyUser = value;
    }

    private String vp1ModifyTime;

    public String getVp1ModifyTime() {
        return this.vp1ModifyTime;
    }

    public void setVp1ModifyTime(String value) {
        this.vp1ModifyTime = value;
    }

    private String vp1RowVersion;

    public String getVp1RowVersion() {
        return this.vp1RowVersion;
    }

    public void setVp1RowVersion(String value) {
        this.vp1RowVersion = value;
    }

    @Override
    public String toString() {
        return "VendingProLinkData [vp1Id=" + vp1Id + ", vp1M02Id=" + vp1M02Id + ", vp1Vd1Id=" + vp1Vd1Id
                + ", vp1Pd1Id=" + vp1Pd1Id + ", vp1PromptValue=" + vp1PromptValue + ", vp1WarningValue="
                + vp1WarningValue + ", vp1CreateUser=" + vp1CreateUser + ", vp1CreateTime=" + vp1CreateTime
                + ", vp1ModifyUser=" + vp1ModifyUser + ", vp1ModifyTime=" + vp1ModifyTime
                + ", vp1RowVersion=" + vp1RowVersion + "]";
    }

}