package com.mc.vending.data;

import java.io.Serializable;

/**
 * ID
 * <p>
 * Table: <strong>CustomerEmpLink 客户员工</strong>
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
 * <td>ce1Id</td>
 * <td>{@link String}</td>
 * <td>ce1_id</td>
 * <td>varchar</td>
 * <td>客户员工ID</td>
 * </tr>
 * <tr>
 * <td>ce1M02Id</td>
 * <td>{@link String}</td>
 * <td>ce1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>ce1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>ce1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>ce1Code</td>
 * <td>{@link String}</td>
 * <td>ce1_code</td>
 * <td>varchar</td>
 * <td>工号</td>
 * </tr>
 * <tr>
 * <td>ce1Name</td>
 * <td>{@link String}</td>
 * <td>ce1_name</td>
 * <td>varchar</td>
 * <td>姓名</td>
 * </tr>
 * <tr>
 * <td>ce1EnglishName</td>
 * <td>{@link String}</td>
 * <td>ce1_englishName</td>
 * <td>varchar</td>
 * <td>英文名</td>
 * </tr>
 * <tr>
 * <td>ce1Sex</td>
 * <td>{@link String}</td>
 * <td>ce1_sex</td>
 * <td>varchar</td>
 * <td>性别</td>
 * </tr>
 * <tr>
 * <td>ce1Dp1Id</td>
 * <td>{@link String}</td>
 * <td>ce1_dp1_id</td>
 * <td>varchar</td>
 * <td>部门ID</td>
 * </tr>
 * <tr>
 * <td>ce1DicIdJob</td>
 * <td>{@link String}</td>
 * <td>ce1_dic_id_job</td>
 * <td>varchar</td>
 * <td>职务ID:数据字典，类型='职务'</td>
 * </tr>
 * <tr>
 * <td>ce1Phone</td>
 * <td>{@link String}</td>
 * <td>ce1_phone</td>
 * <td>varchar</td>
 * <td>电话</td>
 * </tr>
 * <tr>
 * <td>ce1Status</td>
 * <td>{@link String}</td>
 * <td>ce1_status</td>
 * <td>varchar</td>
 * <td>状态:启用；停用</td>
 * </tr>
 * <tr>
 * <td>ce1Remark</td>
 * <td>{@link String}</td>
 * <td>ce1_remark</td>
 * <td>varchar</td>
 * <td>备注</td>
 * </tr>
 * <tr>
 * <td>ce1CreateUser</td>
 * <td>{@link String}</td>
 * <td>ce1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>ce1CreateTime</td>
 * <td>{@link String}</td>
 * <td>ce1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>ce1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>ce1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>ce1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>ce1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>ce1RowVersion</td>
 * <td>{@link String}</td>
 * <td>ce1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class CustomerEmpLinkData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID       = 8737298947585844776L;

    // 定义客户员工状态
    public static final String CUS_EMP_STATUS_ENABLE  = "1";                 // 启用
    public static final String CUS_EMP_STATUS_DISABLE = "2";                 // 停用

    private String             ce1Id;

    public String getCe1Id() {
        return this.ce1Id;
    }

    public void setCe1Id(String value) {
        this.ce1Id = value;
    }

    private String ce1M02Id;

    public String getCe1M02Id() {
        return this.ce1M02Id;
    }

    public void setCe1M02Id(String value) {
        this.ce1M02Id = value;
    }

    private String ce1Cu1Id;

    public String getCe1Cu1Id() {
        return this.ce1Cu1Id;
    }

    public void setCe1Cu1Id(String value) {
        this.ce1Cu1Id = value;
    }

    private String ce1Code;

    public String getCe1Code() {
        return this.ce1Code;
    }

    public void setCe1Code(String value) {
        this.ce1Code = value;
    }

    private String ce1Name;

    public String getCe1Name() {
        return this.ce1Name;
    }

    public void setCe1Name(String value) {
        this.ce1Name = value;
    }

    private String ce1EnglishName;

    public String getCe1EnglishName() {
        return this.ce1EnglishName;
    }

    public void setCe1EnglishName(String value) {
        this.ce1EnglishName = value;
    }

    private String ce1Sex;

    public String getCe1Sex() {
        return this.ce1Sex;
    }

    public void setCe1Sex(String value) {
        this.ce1Sex = value;
    }

    private String ce1Dp1Id;

    public String getCe1Dp1Id() {
        return this.ce1Dp1Id;
    }

    public void setCe1Dp1Id(String value) {
        this.ce1Dp1Id = value;
    }

    private String ce1DicIdJob;

    public String getCe1DicIdJob() {
        return this.ce1DicIdJob;
    }

    public void setCe1DicIdJob(String value) {
        this.ce1DicIdJob = value;
    }

    private String ce1Phone;

    public String getCe1Phone() {
        return this.ce1Phone;
    }

    public void setCe1Phone(String value) {
        this.ce1Phone = value;
    }

    private String ce1Status;

    public String getCe1Status() {
        return this.ce1Status;
    }

    public void setCe1Status(String value) {
        this.ce1Status = value;
    }

    private String ce1Remark;

    public String getCe1Remark() {
        return this.ce1Remark;
    }

    public void setCe1Remark(String value) {
        this.ce1Remark = value;
    }

    private String ce1CreateUser;

    public String getCe1CreateUser() {
        return this.ce1CreateUser;
    }

    public void setCe1CreateUser(String value) {
        this.ce1CreateUser = value;
    }

    private String ce1CreateTime;

    public String getCe1CreateTime() {
        return this.ce1CreateTime;
    }

    public void setCe1CreateTime(String value) {
        this.ce1CreateTime = value;
    }

    private String ce1ModifyUser;

    public String getCe1ModifyUser() {
        return this.ce1ModifyUser;
    }

    public void setCe1ModifyUser(String value) {
        this.ce1ModifyUser = value;
    }

    private String ce1ModifyTime;

    public String getCe1ModifyTime() {
        return this.ce1ModifyTime;
    }

    public void setCe1ModifyTime(String value) {
        this.ce1ModifyTime = value;
    }

    private String ce1RowVersion;

    public String getCe1RowVersion() {
        return this.ce1RowVersion;
    }

    public void setCe1RowVersion(String value) {
        this.ce1RowVersion = value;
    }

    @Override
    public String toString() {
        return "CustomerEmpLinkData [ce1Id=" + ce1Id + ", ce1M02Id=" + ce1M02Id + ", ce1Cu1Id=" + ce1Cu1Id
                + ", ce1Code=" + ce1Code + ", ce1Name=" + ce1Name + ", ce1EnglishName=" + ce1EnglishName
                + ", ce1Sex=" + ce1Sex + ", ce1Dp1Id=" + ce1Dp1Id + ", ce1DicIdJob=" + ce1DicIdJob
                + ", ce1Phone=" + ce1Phone + ", ce1Status=" + ce1Status + ", ce1Remark=" + ce1Remark
                + ", ce1CreateUser=" + ce1CreateUser + ", ce1CreateTime=" + ce1CreateTime
                + ", ce1ModifyUser=" + ce1ModifyUser + ", ce1ModifyTime=" + ce1ModifyTime
                + ", ce1RowVersion=" + ce1RowVersion + "]";
    }

}