package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>VendingPicture 待机图片</strong>
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
 * <td>vp2Id</td>
 * <td>{@link String}</td>
 * <td>vp2_id</td>
 * <td>varchar</td>
 * <td>待机图片ID</td>
 * </tr>
 * <tr>
 * <td>vp2M02Id</td>
 * <td>{@link String}</td>
 * <td>vp2_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vp2Seq</td>
 * <td>{@link String}</td>
 * <td>vp2_seq</td>
 * <td>varchar</td>
 * <td>序号</td>
 * </tr>
 * <tr>
 * <td>vp2FilePath</td>
 * <td>{@link String}</td>
 * <td>vp2_filePath</td>
 * <td>varchar</td>
 * <td>产品图片路径</td>
 * </tr>
 * <tr>
 * <td>vp2RunTime</td>
 * <td>{@link Integer}</td>
 * <td>vp2_runTime</td>
 * <td>int</td>
 * <td>滚动时间</td>
 * </tr>
 * <tr>
 * <td>vp2Type</td>
 * <td>{@link String}</td>
 * <td>vp2_type</td>
 * <td>varchar</td>
 * <td>产品图片类型0:待机图片，1:默认图片</td>
 * </tr>
 * <tr>
 * <td>vp2CreateUser</td>
 * <td>{@link String}</td>
 * <td>vp2_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vp2CreateTime</td>
 * <td>{@link String}</td>
 * <td>vp2_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vp2ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vp2_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vp2ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vp2_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vp2RowVersion</td>
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
public class VendingPictureData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID        = 1459829408727533938L;

    // 定义联机状态
    public static final String VENDING_PICTURE_DEFAULT = "0";                 // 默认图片类型
    public static final String VENDING_PICTURE_DAIJI   = "1";                 // 待机图片类型

    private String             vp2Id;
    private String             vp2M02Id;
    private String             vp2Seq;
    private String             vp2FilePath;
    private int                vp2RunTime;
    private String             vp2Type;
    private String             vp2CreateUser;
    private String             vp2CreateTime;
    private String             vp2ModifyUser;
    private String             vp2ModifyTime;
    private String             vp2RowVersion;

    public String getVp2Id() {
        return vp2Id;
    }

    public void setVp2Id(String vp2Id) {
        this.vp2Id = vp2Id;
    }

    public String getVp2M02Id() {
        return vp2M02Id;
    }

    public void setVp2M02Id(String vp2m02Id) {
        vp2M02Id = vp2m02Id;
    }

    public String getVp2Seq() {
        return vp2Seq;
    }

    public void setVp2Seq(String vp2Seq) {
        this.vp2Seq = vp2Seq;
    }

    public String getVp2FilePath() {
        return vp2FilePath;
    }

    public void setVp2FilePath(String vp2FilePath) {
        this.vp2FilePath = vp2FilePath;
    }

    public int getVp2RunTime() {
        return vp2RunTime;
    }

    public void setVp2RunTime(int vp2RunTime) {
        this.vp2RunTime = vp2RunTime;
    }

    public String getVp2Type() {
        return vp2Type;
    }

    public void setVp2Type(String vp2Type) {
        this.vp2Type = vp2Type;
    }

    public String getVp2CreateUser() {
        return vp2CreateUser;
    }

    public void setVp2CreateUser(String vp2CreateUser) {
        this.vp2CreateUser = vp2CreateUser;
    }

    public String getVp2CreateTime() {
        return vp2CreateTime;
    }

    public void setVp2CreateTime(String vp2CreateTime) {
        this.vp2CreateTime = vp2CreateTime;
    }

    public String getVp2ModifyUser() {
        return vp2ModifyUser;
    }

    public void setVp2ModifyUser(String vp2ModifyUser) {
        this.vp2ModifyUser = vp2ModifyUser;
    }

    public String getVp2ModifyTime() {
        return vp2ModifyTime;
    }

    public void setVp2ModifyTime(String vp2ModifyTime) {
        this.vp2ModifyTime = vp2ModifyTime;
    }

    public String getVp2RowVersion() {
        return vp2RowVersion;
    }

    public void setVp2RowVersion(String vp2RowVersion) {
        this.vp2RowVersion = vp2RowVersion;
    }

    @Override
    public String toString() {
        return "VendingPictureData [vp2Id=" + vp2Id + ", vp2M02Id=" + vp2M02Id + ", vp2Seq=" + vp2Seq
                + ", vp2FilePath=" + vp2FilePath + ", vp2RunTime=" + vp2RunTime + ", vp2CreateUser="
                + vp2CreateUser + ", vp2CreateTime=" + vp2CreateTime + ", vp2ModifyUser=" + vp2ModifyUser
                + ", vp2ModifyTime=" + vp2ModifyTime + ", vp2RowVersion=" + vp2RowVersion + "]";
    }

}
