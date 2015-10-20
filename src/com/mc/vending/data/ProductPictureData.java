package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>ProductPicture　产品图片</strong>
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
 * <td>产品图片ID</td>
 * </tr>
 * <tr>
 * <td>pp1M02Id</td>
 * <td>{@link String}</td>
 * <td>pp1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>pp1Pd1Id</td>
 * <td>{@link String}</td>
 * <td>pp1_pd1_id</td>
 * <td>varchar</td>
 * <td>SKUID</td>
 * </tr>
 * <tr>
 * <td>pp1FilePath</td>
 * <td>{@link String}</td>
 * <td>pp1_filePath</td>
 * <td>varchar</td>
 * <td>产品图片路径</td>
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
public class ProductPictureData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7136345374079474515L;

    private String            pp1Id;

    public String getPp1Id() {
        return this.pp1Id;
    }

    public void setPp1Id(String value) {
        this.pp1Id = value;
    }

    private String pp1M02Id;

    public String getPp1M02Id() {
        return this.pp1M02Id;
    }

    public void setPp1M02Id(String value) {
        this.pp1M02Id = value;
    }

    private String pp1Pd1Id;

    public String getPp1Pd1Id() {
        return this.pp1Pd1Id;
    }

    public void setPp1Pd1Id(String value) {
        this.pp1Pd1Id = value;
    }

    private String pp1FilePath;

    public String getPp1FilePath() {
        return this.pp1FilePath;
    }

    public void setPp1FilePath(String value) {
        this.pp1FilePath = value;
    }

    private String pp1CreateUser;

    public String getPp1CreateUser() {
        return this.pp1CreateUser;
    }

    public void setPp1CreateUser(String value) {
        this.pp1CreateUser = value;
    }

    private String pp1CreateTime;

    public String getPp1CreateTime() {
        return this.pp1CreateTime;
    }

    public void setPp1CreateTime(String value) {
        this.pp1CreateTime = value;
    }

    private String pp1ModifyUser;

    public String getPp1ModifyUser() {
        return this.pp1ModifyUser;
    }

    public void setPp1ModifyUser(String value) {
        this.pp1ModifyUser = value;
    }

    private String pp1ModifyTime;

    public String getPp1ModifyTime() {
        return this.pp1ModifyTime;
    }

    public void setPp1ModifyTime(String value) {
        this.pp1ModifyTime = value;
    }

    private String pp1RowVersion;

    public String getPp1RowVersion() {
        return this.pp1RowVersion;
    }

    public void setPp1RowVersion(String value) {
        this.pp1RowVersion = value;
    }

    @Override
    public String toString() {
        return "ProductPictureData [pp1Id=" + pp1Id + ", pp1M02Id=" + pp1M02Id + ", pp1Pd1Id=" + pp1Pd1Id
                + ", pp1FilePath=" + pp1FilePath + ", pp1CreateUser=" + pp1CreateUser + ", pp1CreateTime="
                + pp1CreateTime + ", pp1ModifyUser=" + pp1ModifyUser + ", pp1ModifyTime=" + pp1ModifyTime
                + ", pp1RowVersion=" + pp1RowVersion + "]";
    }

}