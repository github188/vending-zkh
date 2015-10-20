package com.mc.vending.mock;

public class MC_DifferenceReplenishmentMock {

	private String channleNumber;
	public String getChannleNumber() {
		return channleNumber;
	}
	public void setChannleNumber(String channleNumber) {
		this.channleNumber = channleNumber;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getReplenishmentNumber() {
		return replenishmentNumber;
	}
	public void setReplenishmentNumber(String replenishmentNumber) {
		this.replenishmentNumber = replenishmentNumber;
	}
	public String getDifferenceNumber() {
		return differenceNumber;
	}
	public void setDifferenceNumber(String differenceNumber) {
		this.differenceNumber = differenceNumber;
	}
	private String skuName;
	private String replenishmentNumber;
	private String differenceNumber;
}
