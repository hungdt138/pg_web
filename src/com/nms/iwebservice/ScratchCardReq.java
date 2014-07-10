/**
 * 
 */
package com.nms.iwebservice;

/**
 * @author hungdt
 * 
 */
public class ScratchCardReq extends ServiceRequest {
	private String secretCode;
	private String serial;

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
	
}
