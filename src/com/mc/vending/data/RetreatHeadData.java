package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Table: <strong>RetreatHead 退货单主表</strong>
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
 * <td>rt1Id</td>
 * <td>{@link String}</td>
 * <td>rt1_id</td>
 * <td>varchar</td>
 * <td>退货单主表ID</td>
 * </tr>
 * <tr>
 * <td>rt1M02Id</td>
 * <td>{@link String}</td>
 * <td>rt1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>rt1Rtcode</td>
 * <td>{@link String}</td>
 * <td>rt1_rtcode</td>
 * <td>varchar</td>
 * <td>退货单号</td>
 * </tr>
 * <tr>
 * <td>rt1Type</td>
 * <td>{@link String}</td>
 * <td>rt1_type</td>
 * <td>varchar</td>
 * <td>退货单类型</td>
 * </tr>
 * <tr>
 * <td>rt1Cu1Id</td>
 * <td>{@link String}</td>
 * <td>rt1_cu1_id</td>
 * <td>varchar</td>
 * <td>客户ID</td>
 * </tr>
 * <tr>
 * <td>rt1Vd1Id</td>
 * <td>{@link String}</td>
 * <td>rt1_vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>rt1Ce1Id</td>
 * <td>{@link String}</td>
 * <td>rt1_ce1_id</td>
 * <td>varchar</td>
 * <td>退货员</td>
 * </tr>
 * <tr>
 * <td>rt1Status</td>
 * <td>{@link String}</td>
 * <td>rt1_status</td>
 * <td>varchar</td>
 * <td>订单状态</td>
 * </tr>
 * <tr>
 * <td>rt1CreateUser</td>
 * <td>{@link String}</td>
 * <td>rt1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>rt1CreateTime</td>
 * <td>{@link String}</td>
 * <td>rt1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>rt1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>rt1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>rt1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>rt1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>rt1RowVersion</td>
 * <td>{@link String}</td>
 * <td>rt1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */

public class RetreatHeadData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long  serialVersionUID      = -7560204188382045659L;

    //定义退化状态
    public static final String RETREAT_STATUS_CREATE = "0";                  //创建
    public static final String RETREAT_STATUS_FINISH = "1";                  //完成

    //定义正向、反向退货
    public static final String RETREAT_TYPE_POSITIVE = "0";                  //正向退货
    public static final String RETREAT_TYPE_REVERSE  = "1";                  //反向退货

    private String             rt1Id;

   
    public String getRt1Id() {
        return this.rt1Id;
    }

    public void setRt1Id(String value) {
        this.rt1Id = value;
    }

    private String rt1M02Id;

    public String getRt1M02Id() {
        return this.rt1M02Id;
    }

    public void setRt1M02Id(String value) {
        this.rt1M02Id = value;
    }

    private String rt1Rtcode;

    public String getRt1Rtcode() {
        return this.rt1Rtcode;
    }

    public void setRt1Rtcode(String value) {
        this.rt1Rtcode = value;
    }

    private String rt1Type;

    public String getRt1Type() {
        return this.rt1Type;
    }

    public void setRt1Type(String value) {
        this.rt1Type = value;
    }

    private String rt1Cu1Id;

    public String getRt1Cu1Id() {
        return this.rt1Cu1Id;
    }

    public void setRt1Cu1Id(String value) {
        this.rt1Cu1Id = value;
    }

    private String rt1Vd1Id;

    public String getRt1Vd1Id() {
        return this.rt1Vd1Id;
    }

    public void setRt1Vd1Id(String value) {
        this.rt1Vd1Id = value;
    }

    private String rt1Ce1Id;

    public String getRt1Ce1Id() {
        return rt1Ce1Id;
    }

    public void setRt1Ce1Id(String rt1Ce1Id) {
        this.rt1Ce1Id = rt1Ce1Id;
    }

    private String rt1Status;

    public String getRt1Status() {
        return this.rt1Status;
    }

    public void setRt1Status(String value) {
        this.rt1Status = value;
    }

    private List<RetreatDetailData> retreatDetailDatas;

    public List<RetreatDetailData> getRetreatDetailDatas() {
        return retreatDetailDatas;
    }

    public void setRetreatDetailDatas(List<RetreatDetailData> retreatDetailDatas) {
        this.retreatDetailDatas = retreatDetailDatas;
    }

    @Override
    public String toString() {
        return "RetreatHeadData [rt1Id=" + rt1Id + ", rt1M02Id=" + rt1M02Id + ", rt1Rtcode=" + rt1Rtcode
                + ", rt1Type=" + rt1Type + ", rt1Cu1Id=" + rt1Cu1Id + ", rt1Vd1Id=" + rt1Vd1Id
                + ", rt1Ce1Id=" + rt1Ce1Id + ", rt1Status=" + rt1Status + "]";
    }

}