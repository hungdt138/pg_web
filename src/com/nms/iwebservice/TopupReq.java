/**
 * 
 */
package com.nms.iwebservice;

/**
 * @author hungdt
 * 
 */
public class TopupReq extends ServiceRequest {
	private double amount;
	private String	isdn;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getIsdn() {
		return isdn;
	}

	public void setIsdn(String isdn) {
		this.isdn = isdn;
	}

	

}
