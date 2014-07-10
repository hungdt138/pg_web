/**
 * 
 */
package com.nms.iwebservice;


/**
 * @author hungdt
 *
 */
public interface PaymentServices {
	public abstract ScratchCardResp cardCharging(ScratchCardReq req) throws Exception;
	public abstract TopupResp topupOnline(TopupReq req) throws Exception;
	public abstract VoucherPostpaidResp voucherPostpaid(VoucherPostpaidReq req) throws Exception;
}
