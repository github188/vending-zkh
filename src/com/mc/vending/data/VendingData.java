package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Vending 售货机</strong>
 * <p>
 * <table class="er-mapping" cellspacing=0 cellpadding=0 style=
 * "border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th> <th nowrap>属性类型</th> <th nowrap>字段名</th> <th nowrap>字段类型
 * </th> <th nowrap>说明</th>
 * </tr>
 * <tr>
 * <td>vd1Id</td>
 * <td>{@link String}</td>
 * <td>vd1_id</td>
 * <td>varchar</td>
 * <td>售货机ID</td>
 * </tr>
 * <tr>
 * <td>vd1M02Id</td>
 * <td>{@link String}</td>
 * <td>vd1_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>vd1Code</td>
 * <td>{@link String}</td>
 * <td>vd1_code</td>
 * <td>varchar</td>
 * <td>售货机编号</td>
 * </tr>
 * <tr>
 * <td>vd1Manufacturer</td>
 * <td>{@link String}</td>
 * <td>vd1_manufacturer</td>
 * <td>varchar</td>
 * <td>制造商</td>
 * </tr>
 * <tr>
 * <td>vd1Vm1Id</td>
 * <td>{@link String}</td>
 * <td>vd1_vm1_id</td>
 * <td>varchar</td>
 * <td>售货机型号ID</td>
 * </tr>
 * <tr>
 * <td>vd1LastVersion</td>
 * <td>{@link String}</td>
 * <td>vd1_lastVersion</td>
 * <td>varchar</td>
 * <td>最新版本号</td>
 * </tr>
 * <tr>
 * <td>vd1LwhSize</td>
 * <td>{@link String}</td>
 * <td>vd1_lwhSize</td>
 * <td>varchar</td>
 * <td>尺寸长宽高</td>
 * </tr>
 * <tr>
 * <td>vd1Color</td>
 * <td>{@link String}</td>
 * <td>vd1_color</td>
 * <td>varchar</td>
 * <td>颜色</td>
 * </tr>
 * <tr>
 * <td>vd1InstallAddress</td>
 * <td>{@link String}</td>
 * <td>vd1_installAddress</td>
 * <td>varchar</td>
 * <td>安装地址</td>
 * </tr>
 * <tr>
 * <td>vd1Coordinate</td>
 * <td>{@link String}</td>
 * <td>vd1_coordinate</td>
 * <td>varchar</td>
 * <td>地图经纬度</td>
 * </tr>
 * <tr>
 * <td>vd1St1Id</td>
 * <td>{@link String}</td>
 * <td>vd1_st1_id</td>
 * <td>varchar</td>
 * <td>站点ID</td>
 * </tr>
 * <tr>
 * <td>vd1EmergencyRel</td>
 * <td>{@link String}</td>
 * <td>vd1_emergencyRel</td>
 * <td>varchar</td>
 * <td>紧急联系人</td>
 * </tr>
 * <tr>
 * <td>vd1EmergencyRelPhone</td>
 * <td>{@link String}</td>
 * <td>vd1_emergencyRelPhone</td>
 * <td>varchar</td>
 * <td>紧急联系人电话</td>
 * </tr>
 * <tr>
 * <td>vd1OnlineStatus</td>
 * <td>{@link String}</td>
 * <td>vd1_onlineStatus</td>
 * <td>varchar</td>
 * <td>联机状态:联机、未联机(未联机时超过次数发手机短信给紧急联系人)</td>
 * </tr>
 * <tr>
 * <td>vd1Status</td>
 * <td>{@link String}</td>
 * <td>vd1_status</td>
 * <td>varchar</td>
 * <td>状态:可用、不可用、删除</td>
 * </tr>
 * <tr>
 * <td>vd1LockerStatus</td>
 * <td>{@link String}</td>
 * <td>vd1_lockerstatus</td>
 * <td>varchar</td>
 * <td>售货机货道锁状态</td>
 * </tr>
 * <tr>
 * <td>vd1CreateUser</td>
 * <td>{@link String}</td>
 * <td>vd1_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>vd1CreateTime</td>
 * <td>{@link String}</td>
 * <td>vd1_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>vd1ModifyUser</td>
 * <td>{@link String}</td>
 * <td>vd1_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>vd1ModifyTime</td>
 * <td>{@link String}</td>
 * <td>vd1_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>vd1RowVersion</td>
 * <td>{@link String}</td>
 * <td>vd1_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class VendingData extends BaseParseData implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6990282001803903087L;

	// 定义特殊字符串
	public static final String VENDING_TELE_400 = "4006809696"; // 400电话

	// 定义联机状态
	public static final String VENDING_ONLINESTATUS_ON = "0"; // 联机
	public static final String VENDING_ONLINESTATUS_OFF = "1"; // 未联机（未联机时超过次数发手机短信给紧急联系人）

	// 定义售货机状态
	public static final String VENDING_STATUS_YES = "0"; // 可用
	public static final String VENDING_STATUS_NO = "1"; // 不可用
	public static final String VENDING_STATUS_DELETE = "2"; // 删除

	private String vd1Id;

	public String getVd1Id() {
		return this.vd1Id;
	}

	public void setVd1Id(String value) {
		this.vd1Id = value;
	}

	private String vd1M02Id;

	public String getVd1M02Id() {
		return this.vd1M02Id;
	}

	public void setVd1M02Id(String value) {
		this.vd1M02Id = value;
	}

	private String vd1Code;

	public String getVd1Code() {
		return this.vd1Code;
	}

	public void setVd1Code(String value) {
		this.vd1Code = value;
	}

	private String vd1Manufacturer;

	public String getVd1Manufacturer() {
		return this.vd1Manufacturer;
	}

	public void setVd1Manufacturer(String value) {
		this.vd1Manufacturer = value;
	}

	private String vd1Vm1Id;

	public String getVd1Vm1Id() {
		return this.vd1Vm1Id;
	}

	public void setVd1Vm1Id(String value) {
		this.vd1Vm1Id = value;
	}

	private String vd1LastVersion;

	public String getVd1LastVersion() {
		return this.vd1LastVersion;
	}

	public void setVd1LastVersion(String value) {
		this.vd1LastVersion = value;
	}

	private String vd1LwhSize;

	public String getVd1LwhSize() {
		return this.vd1LwhSize;
	}

	public void setVd1LwhSize(String value) {
		this.vd1LwhSize = value;
	}

	private String vd1Color;

	public String getVd1Color() {
		return this.vd1Color;
	}

	public void setVd1Color(String value) {
		this.vd1Color = value;
	}

	private String vd1InstallAddress;

	public String getVd1InstallAddress() {
		return this.vd1InstallAddress;
	}

	public void setVd1InstallAddress(String value) {
		this.vd1InstallAddress = value;
	}

	private String vd1Coordinate;

	public String getVd1Coordinate() {
		return this.vd1Coordinate;
	}

	public void setVd1Coordinate(String value) {
		this.vd1Coordinate = value;
	}

	private String vd1St1Id;

	public String getVd1St1Id() {
		return this.vd1St1Id;
	}

	public void setVd1St1Id(String value) {
		this.vd1St1Id = value;
	}

	private String vd1EmergencyRel;

	public String getVd1EmergencyRel() {
		return this.vd1EmergencyRel;
	}

	public void setVd1EmergencyRel(String value) {
		this.vd1EmergencyRel = value;
	}

	private String vd1EmergencyRelPhone;

	public String getVd1EmergencyRelPhone() {
		return this.vd1EmergencyRelPhone;
	}

	public void setVd1EmergencyRelPhone(String value) {
		this.vd1EmergencyRelPhone = value;
	}

	private String vd1OnlineStatus;

	public String getVd1OnlineStatus() {
		return this.vd1OnlineStatus;
	}

	public void setVd1OnlineStatus(String value) {
		this.vd1OnlineStatus = value;
	}

	private String vd1Status;

	public String getVd1Status() {
		return this.vd1Status;
	}

	public void setVd1Status(String value) {
		this.vd1Status = value;
	}

	private String vd1LockerStatus;

	/**
	 * @author junjie.you
	 * @return the vd1LockerStatus
	 */
	public String getVd1LockerStatus() {
		return vd1LockerStatus == null ? "0" : vd1LockerStatus;
	}

	/**
	 * @author junjie.you
	 * @param vd1LockerStatus
	 *            the vd1LockerStatus to set
	 */
	public void setVd1LockerStatus(String vd1LockerStatus) {
		this.vd1LockerStatus = vd1LockerStatus;
	}

	private String vd1CreateUser;

	public String getVd1CreateUser() {
		return this.vd1CreateUser;
	}

	public void setVd1CreateUser(String value) {
		this.vd1CreateUser = value;
	}

	private String vd1CreateTime;

	public String getVd1CreateTime() {
		return this.vd1CreateTime;
	}

	public void setVd1CreateTime(String value) {
		this.vd1CreateTime = value;
	}

	private String vd1ModifyUser;

	public String getVd1ModifyUser() {
		return this.vd1ModifyUser;
	}

	public void setVd1ModifyUser(String value) {
		this.vd1ModifyUser = value;
	}

	private String vd1ModifyTime;

	public String getVd1ModifyTime() {
		return this.vd1ModifyTime;
	}

	public void setVd1ModifyTime(String value) {
		this.vd1ModifyTime = value;
	}

	private String vd1RowVersion;

	public String getVd1RowVersion() {
		return this.vd1RowVersion;
	}

	public void setVd1RowVersion(String value) {
		this.vd1RowVersion = value;
	}

	private String vd1CardType;

	public String getVd1CardType() {
		return vd1CardType;
	}

	public void setVd1CardType(String vd1CardType) {
		this.vd1CardType = vd1CardType;
	}

	@Override
	public String toString() {
		return "VendingData [vd1Id=" + vd1Id + ", vd1M02Id=" + vd1M02Id + ", vd1Code=" + vd1Code + ", vd1Manufacturer="
				+ vd1Manufacturer + ", vd1Vm1Id=" + vd1Vm1Id + ", vd1LastVersion=" + vd1LastVersion + ", vd1LwhSize="
				+ vd1LwhSize + ", vd1Color=" + vd1Color + ", vd1InstallAddress=" + vd1InstallAddress
				+ ", vd1Coordinate=" + vd1Coordinate + ", vd1St1Id=" + vd1St1Id + ", vd1EmergencyRel=" + vd1EmergencyRel
				+ ", vd1EmergencyRelPhone=" + vd1EmergencyRelPhone + ", vd1OnlineStatus=" + vd1OnlineStatus
				+ ", vd1Status=" + vd1Status + ", vd1LockerStatus=" + vd1LockerStatus + ", vd1CreateUser="
				+ vd1CreateUser + ", vd1CreateTime=" + vd1CreateTime + ", vd1ModifyUser=" + vd1ModifyUser
				+ ", vd1ModifyTime=" + vd1ModifyTime + ", vd1RowVersion=" + vd1RowVersion + ", vd1CardType="
				+ vd1CardType + "]";
	}

}