package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Product 产品</strong>
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
 * <td>pd1Id</td>
 * <td>{@link String}</td>
 * <td>pd1_id</td>
 * <td>varchar</td>
 * <td>产品ID</td>
 * </tr>
 * <tr>
 * <td>pd1M02Id</td>
 * <td>{@link String}</td>
 * <td>pd1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>pd1Code</td>
 * <td>{@link String}</td>
 * <td>pd1_code</td>
 * <td>varchar</td>
 * <td>SKU编号</td>
 * </tr>
 * <tr>
 * <td>pd1Name</td>
 * <td>{@link String}</td>
 * <td>pd1_name</td>
 * <td>varchar</td>
 * <td>产品名称</td>
 * </tr>
 * <tr>
 * <td>pd1Description</td>
 * <td>{@link String}</td>
 * <td>pd1_description</td>
 * <td>varchar</td>
 * <td>物料描述</td>
 * </tr>
 * <tr>
 * <td>pd1ManufactureModel</td>
 * <td>{@link String}</td>
 * <td>pd1_manufactureModel</td>
 * <td>varchar</td>
 * <td>制造商型号</td>
 * </tr>
 * <tr>
 * <td>pd1Size</td>
 * <td>{@link String}</td>
 * <td>pd1_size</td>
 * <td>varchar</td>
 * <td>核心规格</td>
 * </tr>
 * <tr>
 * <td>pd1Brand</td>
 * <td>{@link String}</td>
 * <td>pd1_brand</td>
 * <td>varchar</td>
 * <td>品牌</td>
 * </tr>
 * <tr>
 * <td>pd1Package</td>
 * <td>{@link String}</td>
 * <td>pd1_package</td>
 * <td>varchar</td>
 * <td>包装</td>
 * </tr>
 * <tr>
 * <td>pd1Unit</td>
 * <td>{@link String}</td>
 * <td>pd1_unit</td>
 * <td>varchar</td>
 * <td>计量单位</td>
 * </tr>
 * <tr>
 * <td>pd1LastImportTime</td>
 * <td>{@link String}</td>
 * <td>pd1_lastImportTime</td>
 * <td>varchar</td>
 * <td>最近导入时间</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class ProductData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID    = -2974800842806130086L;
    public static final int   ProductData_status1 = 1;                    // 状态1=sdfs；

    private String            pd1Id;

    public String getPd1Id() {
        return this.pd1Id;
    }

    public void setPd1Id(String value) {
        this.pd1Id = value;
    }

    private String pd1M02Id;

    public String getPd1M02Id() {
        return this.pd1M02Id;
    }

    public void setPd1M02Id(String value) {
        this.pd1M02Id = value;
    }

    private String pd1Code;

    public String getPd1Code() {
        return this.pd1Code;
    }

    public void setPd1Code(String value) {
        this.pd1Code = value;
    }

    private String pd1Name;

    public String getPd1Name() {
        return this.pd1Name;
    }

    public void setPd1Name(String value) {
        this.pd1Name = value;
    }

    private String pd1Description;

    public String getPd1Description() {
        return this.pd1Description;
    }

    public void setPd1Description(String value) {
        this.pd1Description = value;
    }

    private String pd1ManufactureModel;

    public String getPd1ManufactureModel() {
        return this.pd1ManufactureModel;
    }

    public void setPd1ManufactureModel(String value) {
        this.pd1ManufactureModel = value;
    }

    private String pd1Size;

    public String getPd1Size() {
        return this.pd1Size;
    }

    public void setPd1Size(String value) {
        this.pd1Size = value;
    }

    private String pd1Brand;

    public String getPd1Brand() {
        return this.pd1Brand;
    }

    public void setPd1Brand(String value) {
        this.pd1Brand = value;
    }

    private String pd1Package;

    public String getPd1Package() {
        return this.pd1Package;
    }

    public void setPd1Package(String value) {
        this.pd1Package = value;
    }

    private String pd1Unit;

    public String getPd1Unit() {
        return this.pd1Unit;
    }

    public void setPd1Unit(String value) {
        this.pd1Unit = value;
    }

    private String pd1LastImportTime;

    public String getPd1LastImportTime() {
        return this.pd1LastImportTime;
    }

    public void setPd1LastImportTime(String value) {
        this.pd1LastImportTime = value;
    }

    @Override
    public String toString() {
        return "ProductData [pd1Id=" + pd1Id + ", pd1M02Id=" + pd1M02Id + ", pd1Code=" + pd1Code
                + ", pd1Name=" + pd1Name + ", pd1Description=" + pd1Description + ", pd1ManufactureModel="
                + pd1ManufactureModel + ", pd1Size=" + pd1Size + ", pd1Brand=" + pd1Brand + ", pd1Package="
                + pd1Package + ", pd1Unit=" + pd1Unit + ", pd1LastImportTime=" + pd1LastImportTime + "]";
    }

}