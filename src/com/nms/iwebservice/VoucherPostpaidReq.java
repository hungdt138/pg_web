/**
 * 
 */
package com.nms.iwebservice;

/**
 * @author hungdt
 * 
 */
public class VoucherPostpaidReq extends ServiceRequest {
	private String secretCode;
	private String serial;
	private String isdn;

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getIsdn() {
		return isdn;
	}

	public void setIsdn(String isdn) {
		this.isdn = isdn;
	}

}
