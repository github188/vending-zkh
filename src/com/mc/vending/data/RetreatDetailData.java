package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>RetreatDetail　退货单从表</strong>
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
 * <td>rt2Id</td>
 * <td>{@link String}</td>
 * <td>rt2_id</td>
 * <td>varchar</td>
 * <td>退货单从表ID</td>
 * </tr>
 * <tr>
 * <td>rt2M02Id</td>
 * <td>{@link String}</td>
 * <td>rt2_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>rt2Rt1Id</td>
 * <td>{@link String}</td>
 * <td>rt2_rt1_id</td>
 * <td>varchar</td>
 * <td>退货单主表ID</td>
 * </tr>
 * <tr>
 * <td>rt2Vc1Code</td>
 * <td>{@link String}</td>
 * <td>rt2_vc1_code</td>
 * <td>varchar</td>
 * <td>售货机货道编号</td>
 * </tr>
 * <tr>
 * <td>rt2Pd1Id</td>
 * <td>{@link String}</td>
 * <td>rt2_pd1_id</td>
 * <td>varchar</td>
 * <td>SKUID</td>
 * </tr>
 * <tr>
 * <td>rt2SaleType</td>
 * <td>{@link String}</td>
 * <td>rt2_saleType</td>
 * <td>varchar</td>
 * <td>销售类型</td>
 * </tr>
 * <tr>
 * <td>rt2Sp1Id</td>
 * <td>{@link String}</td>
 * <td>rt2_sp1_id</td>
 * <td>varchar</td>
 * <td>货主ID</td>
 * </tr>
 * <tr>
 * <td>rt2PlanQty</td>
 * <td>{@link Integer}</td>
 * <td>rt2_planQty</td>
 * <td>int</td>
 * <td>计划退货数</td>
 * </tr>
 * <tr>
 * <td>rt2ActualQty</td>
 * <td>{@link Integer}</td>
 * <td>rt2_actualQty</td>
 * <td>int</td>
 * <td>实际退货数</td>
 * </tr>
 * <tr>
 * <td>rt2CreateUser</td>
 * <td>{@link String}</td>
 * <td>rt2_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>rt2CreateTime</td>
 * <td>{@link String}</td>
 * <td>rt2_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>rt2ModifyUser</td>
 * <td>{@link String}</td>
 * <td>rt2_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>rt2ModifyTime</td>
 * <td>{@link String}</td>
 * <td>rt2_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>rt2RowVersion</td>
 * <td>{@link String}</td>
 * <td>rt2_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class RetreatDetailData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2864876118133865574L;

    private String            rt2Id;

    public String getRt2Id() {
        return this.rt2Id;
    }

    public void setRt2Id(String value) {
        this.rt2Id = value;
    }

    private String rt2M02Id;

    public String getRt2M02Id() {
        return this.rt2M02Id;
    }

    public void setRt2M02Id(String value) {
        this.rt2M02Id = value;
    }

    private String rt2Rt1Id;

    public String getRt2Rt1Id() {
        return this.rt2Rt1Id;
    }

    public void setRt2Rt1Id(String value) {
        this.rt2Rt1Id = value;
    }

    private String rt2Vc1Code;

    public String getRt2Vc1Code() {
        return this.rt2Vc1Code;
    }

    public void setRt2Vc1Code(String value) {
        this.rt2Vc1Code = value;
    }

    private String rt2Pd1Id;

    public String getRt2Pd1Id() {
        return this.rt2Pd1Id;
    }

    public void setRt2Pd1Id(String value) {
        this.rt2Pd1Id = value;
    }

    private String rt2SaleType;

    public String getRt2SaleType() {
        return this.rt2SaleType;
    }

    public void setRt2SaleType(String value) {
        this.rt2SaleType = value;
    }

    private String rt2Sp1Id;

    public String getRt2Sp1Id() {
        return this.rt2Sp1Id;
    }

    public void setRt2Sp1Id(String value) {
        this.rt2Sp1Id = value;
    }

    private Integer rt2PlanQty;

    public Integer getRt2PlanQty() {
        return this.rt2PlanQty;
    }

    public void setRt2PlanQty(Integer value) {
        this.rt2PlanQty = value;
    }

    private Integer rt2ActualQty;

    public Integer getRt2ActualQty() {
        return this.rt2ActualQty;
    }

    public void setRt2ActualQty(Integer value) {
        this.rt2ActualQty = value;
    }

    @Override
    public String toString() {
        return "RetreatDetailData [rt2Id=" + rt2Id + ", rt2M02Id=" + rt2M02Id + ", rt2Rt1Id=" + rt2Rt1Id
                + ", rt2Vc1Code=" + rt2Vc1Code + ", rt2Pd1Id=" + rt2Pd1Id + ", rt2SaleType=" + rt2SaleType
                + ", rt2Sp1Id=" + rt2Sp1Id + ", rt2PlanQty=" + rt2PlanQty + ", rt2ActualQty=" + rt2ActualQty
                + "]";
    }

}