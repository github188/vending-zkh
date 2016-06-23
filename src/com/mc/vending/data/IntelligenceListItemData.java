/**
 * 
 */
package com.mc.vending.data;

import android.widget.TextView;

/**
 * @author junjie.you
 *
 */
public class IntelligenceListItemData {
	private TextView stockNum;
	private TextView pdName;
	private TextView quantity;
	/**
	 * @author junjie.you
	 * @return the stockNum
	 */
	public TextView getStockNum() {
		return stockNum;
	}
	/**
	 * @author junjie.you
	 * @param stockNum the stockNum to set
	 */
	public void setStockNum(TextView stockNum) {
		this.stockNum = stockNum;
	}
	/**
	 * @author junjie.you
	 * @return the pdName
	 */
	public TextView getPdName() {
		return pdName;
	}
	/**
	 * @author junjie.you
	 * @param pdName the pdName to set
	 */
	public void setPdName(TextView pdName) {
		this.pdName = pdName;
	}
	/**
	 * @author junjie.you
	 * @return the quantity
	 */
	public TextView getQuantity() {
		return quantity;
	}
	/**
	 * @author junjie.you
	 * @param quantity the quantity to set
	 */
	public void setQuantity(TextView quantity) {
		this.quantity = quantity;
	}
}
