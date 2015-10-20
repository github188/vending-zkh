/**
 * 
 */
package com.mc.vending.data;

import java.io.Serializable;

/**
 * @author junjie.you
 * @date 2015年10月12日
 * @email junjie.you@zkh360.com
 */
public class ConversionData extends BaseParseData implements Serializable {

	private static final long serialVersionUID = -7443975919101786265L;

	private String cn1Id;
	private String cn1Upid;
	private String cn1Cpid;
	private String cn1Proportion;
	/** 
	 * 表ID
	 * 
	 * @author junjie.you
	 * @return the cn1Id
	 */
	public String getCn1Id() {
		return cn1Id;
	}
	/**
	 * 表ID
	 * @author junjie.you
	 * @param cn1Id the cn1Id to set
	 */
	public void setCn1Id(String cn1Id) {
		this.cn1Id = cn1Id;
	}
	/**
	 * 基础产品ID
	 * 
	 * @author junjie.you
	 * @return the cn1Upid
	 */
	public String getCn1Upid() {
		return cn1Upid;
	}
	/**
	 * 基础产品ID
	 * 
	 * @author junjie.you
	 * @param cn1Upid the cn1Upid to set
	 */
	public void setCn1Upid(String cn1Upid) {
		this.cn1Upid = cn1Upid;
	}
	/**
	 * 关联产品ID
	 *
	 * @author junjie.you
	 * @return the cn1Cpid
	 */
	public String getCn1Cpid() {
		return cn1Cpid;
	}
	/**
	 * 关联产品ID
	 *
	 * @author junjie.you
	 * @param cn1Cpid the cn1Cpid to set
	 */
	public void setCn1Cpid(String cn1Cpid) {
		this.cn1Cpid = cn1Cpid;
	}
	/**
	 * 换算比例
	 * 
	 * @author junjie.you
	 * @return the cn1Proportion
	 */
	public String getCn1Proportion() {
		return cn1Proportion;
	}
	/**
	 * 换算比例
	 * 
	 * @author junjie.you
	 * @param cn1Proportion the cn1Proportion to set
	 */
	public void setCn1Proportion(String cn1Proportion) {
		this.cn1Proportion = cn1Proportion;
	}
	/**
	 * 操作方式
	 * 
	 * @author junjie.you
	 * @return the cn1Operation
	 */
	public String getCn1Operation() {
		return cn1Operation;
	}
	/**
	 * 操作方式
	 * 
	 * @author junjie.you
	 * @param cn1Operation the cn1Operation to set
	 */
	public void setCn1Operation(String cn1Operation) {
		this.cn1Operation = cn1Operation;
	}
	/**
	 * 创建人
	 * 
	 * @author junjie.you
	 * @return the cn1CreateUser
	 */
	public String getCn1CreateUser() {
		return cn1CreateUser;
	}
	/**
	 * 创建人
	 * 
	 * @author junjie.you
	 * @param cn1CreateUser the cn1CreateUser to set
	 */
	public void setCn1CreateUser(String cn1CreateUser) {
		this.cn1CreateUser = cn1CreateUser;
	}
	/**
	 * 创建时间
	 * 
	 * @author junjie.you
	 * @return the cn1CreateTime
	 */
	public String getCn1CreateTime() {
		return cn1CreateTime;
	}
	/**
	 * 创建时间
	 * 
	 * @author junjie.you
	 * @param cn1CreateTime the cn1CreateTime to set
	 */
	public void setCn1CreateTime(String cn1CreateTime) {
		this.cn1CreateTime = cn1CreateTime;
	}
	/**
	 * 最后修改人
	 * 
	 * @author junjie.you
	 * @return the cn1ModifyUser
	 */
	public String getCn1ModifyUser() {
		return cn1ModifyUser;
	}
	/**
	 * 最后修改人
	 * 
	 * @author junjie.you
	 * @param cn1ModifyUser the cn1ModifyUser to set
	 */
	public void setCn1ModifyUser(String cn1ModifyUser) {
		this.cn1ModifyUser = cn1ModifyUser;
	}
	/**
	 * 最后修改时间
	 * 
	 * @author junjie.you
	 * @return the cn1ModifyTime
	 */
	public String getCn1ModifyTime() {
		return cn1ModifyTime;
	}
	/**
	 * 最后修改时间
	 * 
	 * @author junjie.you
	 * @param cn1ModifyTime the cn1ModifyTime to set
	 */
	public void setCn1ModifyTime(String cn1ModifyTime) {
		this.cn1ModifyTime = cn1ModifyTime;
	}
	/**
	 * 时间戳
	 * 
	 * @author junjie.you
	 * @return the cn1RowVersion
	 */
	public String getCn1RowVersion() {
		return cn1RowVersion;
	}
	/**
	 * 时间戳
	 * 
	 * @author junjie.you
	 * @param cn1RowVersion the cn1RowVersion to set
	 */
	public void setCn1RowVersion(String cn1RowVersion) {
		this.cn1RowVersion = cn1RowVersion;
	}
	private String cn1Operation;
	private String cn1CreateUser;
	private String cn1CreateTime;
	private String cn1ModifyUser;
	private String cn1ModifyTime;
	private String cn1RowVersion;
	
	@Override
    public String toString() {
        return "Conversion [cn1Id=" + cn1Id+ ", cn1Upid=" + cn1Upid+ ", cn1Proportion=" + cn1Proportion
                + ", cn1Operation=" + cn1Operation+ ", cn1CreateUser=" + cn1CreateUser+ ", cn1CreateTime=" + cn1CreateTime
                + ", cn1ModifyUser=" + cn1ModifyUser+ ", cn1ModifyTime=" + cn1ModifyTime
                + ", cn1RowVersion=" + cn1RowVersion+ "]";
    }
}
