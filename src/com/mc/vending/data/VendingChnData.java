package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>VendingChn 售货机货道</strong>
 * <p>
 * <table class="er-mapping" cellspacing=0 cellpadding=0 style=
 * "border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th> <th nowrap>属性类型</th> <th nowrap>字段名</th> <th nowrap>字段类型
 * </th> <th nowrap>说明</th>
 * </tr>
 * <tr>
 * <td>vc1Id</td>
 * <td>{@link String}</td>
 * <td>vc1_id</td>
 * <td>varchar</td>
 * <td>售货机货道ID</td>
 * </tr>
 * <tr>
 * <td>vc1M02Id</td>
 * <td>{@link String}</td>
 * <td>vc1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vc1Vd1Id</td>
 * <td>{@link String}</td>
 * <td>vc1_vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>vc1Code</td>
 * <td>{@link String}</td>
 * <td>vc1_code</td>
 * <td>varchar</td>
 * <td>货道编号</td>
 * </tr>
 * <tr>
 * <td>vc1Type</td>
 * <td>{@link String}</td>
 * <td>vc1_type</td>
 * <td>varchar</td>
 * <td>货道类型</td>
 * </tr>
 * <tr>
 * <td>vc1Capacity</td>
 * <td>{@link Integer}</td>
 * <td>vc1_capacity</td>
 * <td>int</td>
 * <td>货道容量</td>
 * </tr>
 * <tr>
 * <td>vc1ThreadSize</td>
 * <td>{@link String}</td>
 * <td>vc1_threadSize</td>
 * <td>varchar</td>
 * <td>螺纹尺寸</td>
 * </tr>
 * <tr>
 * <td>vc1Pd1Id</td>
 * <td>{@link String}</td>
 * <td>vc1_pd1_id</td>
 * <td>varchar</td>
 * <td>SKUID</td>
 * </tr>
 * <tr>
 * <td>vc1SaleType</td>
 * <td>{@link String}</td>
 * <td>vc1_saleType</td>
 * <td>varchar</td>
 * <td>销售类型</td>
 * </tr>
 * <tr>
 * <td>vc1Sp1Id</td>
 * <td>{@link String}</td>
 * <td>vc1_sp1_id</td>
 * <td>varchar</td>
 * <td>货主ID</td>
 * </tr>
 * <tr>
 * <td>vc1BorrowStatus</td>
 * <td>{@link String}</td>
 * <td>vc1_borrowStatus</td>
 * <td>varchar</td>
 * <td>借还状态</td>
 * </tr>
 * <tr>
 * <td>vc1Status</td>
 * <td>{@link String}</td>
 * <td>vc1_status</td>
 * <td>varchar</td>
 * <td>状态</td>
 * </tr>
 * <tr>
 * <td>vc1LineNum</td>
 * <td>{@link String}</td>
 * <td>vc1_lineNum</td>
 * <td>varchar</td>
 * <td>售货机行/格子机型号</td>
 * </tr>
 * <tr>
 * <td>vc1ColumnNum</td>
 * <td>{@link String}</td>
 * <td>vc1_columnNum</td>
 * <td>varchar</td>
 * <td>售货机列/格子机编号</td>
 * </tr>
 * <tr>
 * <td>vc1Height</td>
 * <td>{@link String}</td>
 * <td>vc1_height</td>
 * <td>varchar</td>
 * <td>售货机高/格子机门号</td>
 * </tr>
 * <tr>
 * <td>vc1DistanceInitValue</td>
 * <td>{@link String}</td>
 * <td>vc1_distanceinitvalue</td>
 * <td>varchar</td>
 * <td>距离传感器初始值</td>
 * </tr>
 * <tr>
 * <td>vc1CreateUser</td>
 * <td>{@link String}</td>
 * <td>vc1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vc1CreateTime</td>
 * <td>{@link String}</td>
 * <td>vc1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vc1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vc1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vc1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vc1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vc1RowVersion</td>
 * <td>{@link String}</td>
 * <td>vc1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingChnData extends BaseParseData implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1591303617438650753L;

	// 定义售货机货疲道类型
	public static final String VENDINGCHN_TYPE_VENDING = "0"; // 售货机
	public static final String VENDINGCHN_TYPE_CELL = "1"; // 格子机

	// 定义售货机货道状态
	public static final String VENDINGCHN_STATUS_NORMAL = "0"; // 正常
	public static final String VENDINGCHN_STATUS_FROZENING = "1"; // 冻结中
	public static final String VENDINGCHN_STATUS_FROZEN_FINISH = "2"; // 已冻结

	// 定义售货机货道销售类型
	public static final String VENDINGCHN_SALETYPE_SELF = "0"; // 自营
	public static final String VENDINGCHN_SALETYPE_JISHOU = "1"; // 寄售
	public static final String VENDINGCHN_SALETYPE_BORROW = "2"; // 借/还

	// 定义借还状态
	public static final String VENDINGCHN_BORROWSTATUS_BORROW = "0"; // 借
	public static final String VENDINGCHN_BORROWSTATUS_RETURN = "1"; // 还

	// 定义售货机货疲道检查标识
	public static final String VENDINGCHN_METHOD_GENERAL = "1"; // 一般领料
	public static final String VENDINGCHN_METHOD_BORROW = "2"; // 借还领料

	// 定义售货机货疲道检查标识
	public static final String VENDINGCHN_COMMAND_BORROW = "1"; // 借指令
	public static final String VENDINGCHN_COMMAND_RETURN = "2"; // 还指令

	private String vc1Id;

	public String getVc1Id() {
		return this.vc1Id;
	}

	public void setVc1Id(String value) {
		this.vc1Id = value;
	}

	private String vc1M02Id;

	public String getVc1M02Id() {
		return this.vc1M02Id;
	}

	public void setVc1M02Id(String value) {
		this.vc1M02Id = value;
	}

	private String vc1Vd1Id;

	public String getVc1Vd1Id() {
		return this.vc1Vd1Id;
	}

	public void setVc1Vd1Id(String value) {
		this.vc1Vd1Id = value;
	}

	private String vc1Code;

	public String getVc1Code() {
		return this.vc1Code;
	}

	public void setVc1Code(String value) {
		this.vc1Code = value;
	}

	private String vc1Type;

	public String getVc1Type() {
		return this.vc1Type;
	}

	public void setVc1Type(String value) {
		this.vc1Type = value;
	}

	private Integer vc1Capacity;

	public Integer getVc1Capacity() {
		return this.vc1Capacity;
	}

	public void setVc1Capacity(Integer value) {
		this.vc1Capacity = value;
	}

	private String vc1ThreadSize;

	public String getVc1ThreadSize() {
		return this.vc1ThreadSize;
	}

	public void setVc1ThreadSize(String value) {
		this.vc1ThreadSize = value;
	}

	private String vc1Pd1Id;

	public String getVc1Pd1Id() {
		return this.vc1Pd1Id;
	}

	public void setVc1Pd1Id(String value) {
		this.vc1Pd1Id = value;
	}

	private String vc1SaleType;

	public String getVc1SaleType() {
		return this.vc1SaleType;
	}

	public void setVc1SaleType(String value) {
		this.vc1SaleType = value;
	}

	private String vc1Sp1Id;

	public String getVc1Sp1Id() {
		return this.vc1Sp1Id;
	}

	public void setVc1Sp1Id(String value) {
		this.vc1Sp1Id = value;
	}

	private String vc1BorrowStatus;

	public String getVc1BorrowStatus() {
		return this.vc1BorrowStatus;
	}

	public void setVc1BorrowStatus(String value) {
		this.vc1BorrowStatus = value;
	}

	private String vc1Status;

	public String getVc1Status() {
		return this.vc1Status;
	}

	public void setVc1Status(String value) {
		this.vc1Status = value;
	}

	private String vc1LineNum;

	public String getVc1LineNum() {
		return this.vc1LineNum;
	}

	public void setVc1LineNum(String value) {
		this.vc1LineNum = value;
	}

	private String vc1ColumnNum;

	public String getVc1ColumnNum() {
		return this.vc1ColumnNum;
	}

	public void setVc1ColumnNum(String value) {
		this.vc1ColumnNum = value;
	}

	private String vc1Height;

	public String getVc1Height() {
		return this.vc1Height;
	}

	public void setVc1Height(String value) {
		this.vc1Height = value;
	}

	private String vc1CreateUser;

	public String getVc1CreateUser() {
		return this.vc1CreateUser;
	}

	public void setVc1CreateUser(String value) {
		this.vc1CreateUser = value;
	}

	private String vc1CreateTime;

	public String getVc1CreateTime() {
		return this.vc1CreateTime;
	}

	public void setVc1CreateTime(String value) {
		this.vc1CreateTime = value;
	}

	private String vc1ModifyUser;

	public String getVc1ModifyUser() {
		return this.vc1ModifyUser;
	}

	public void setVc1ModifyUser(String value) {
		this.vc1ModifyUser = value;
	}

	private String vc1ModifyTime;

	public String getVc1ModifyTime() {
		return this.vc1ModifyTime;
	}

	public void setVc1ModifyTime(String value) {
		this.vc1ModifyTime = value;
	}

	private String vc1RowVersion;

	public String getVc1RowVersion() {
		return this.vc1RowVersion;
	}

	public void setVc1RowVersion(String value) {
		this.vc1RowVersion = value;
	}

	private int inputQty = 0;
	private int failureQty = 0;

	public int getFailureQty() {
		return failureQty;
	}

	public void setFailureQty(int failureQty) {
		this.failureQty = failureQty;
	}

	public int getInputQty() {
		return inputQty;
	}

	public void setInputQty(int inputQty) {
		this.inputQty = inputQty;
	}

	private String vc1DistanceInitValue;

	/**
	 * @author junjie.you
	 * @return the vc1DistanceInitValue
	 */
	public String getVc1DistanceInitValue() {
		return vc1DistanceInitValue == null ? "0" : vc1DistanceInitValue;
	}

	/**
	 * @author junjie.you
	 * @param vc1DistanceInitValue
	 *            the vc1DistanceInitValue to set
	 */
	public void setVc1DistanceInitValue(String vc1DistanceInitValue) {
		this.vc1DistanceInitValue = vc1DistanceInitValue;
	}

	@Override
	public String toString() {
		return "VendingChnData [vc1Id=" + vc1Id + ", vc1M02Id=" + vc1M02Id + ", vc1Vd1Id=" + vc1Vd1Id + ", vc1Code="
				+ vc1Code + ", vc1Type=" + vc1Type + ", vc1Capacity=" + vc1Capacity + ", vc1ThreadSize=" + vc1ThreadSize
				+ ", vc1Pd1Id=" + vc1Pd1Id + ", vc1SaleType=" + vc1SaleType + ", vc1Sp1Id=" + vc1Sp1Id
				+ ", vc1BorrowStatus=" + vc1BorrowStatus + ", vc1Status=" + vc1Status + ", vc1LineNum=" + vc1LineNum
				+ ", vc1ColumnNum=" + vc1ColumnNum + ", vc1Height=" + vc1Height + ", vc1DistanceInitValue="
				+ vc1DistanceInitValue + ", vc1CreateUser=" + vc1CreateUser + ", vc1CreateTime=" + vc1CreateTime
				+ ", vc1ModifyUser=" + vc1ModifyUser + ", vc1ModifyTime=" + vc1ModifyTime + ", vc1RowVersion="
				+ vc1RowVersion + "]";
	}

}