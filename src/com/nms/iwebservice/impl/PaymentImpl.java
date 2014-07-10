/**
 * 
 */
package com.nms.iwebservice.impl;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.xfire.transport.http.XFireServletController;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.message.CommandMessage;
import com.nms.iwebservice.ErrorCode;
import com.nms.iwebservice.PaymentWebserviceBase;
import com.nms.iwebservice.ScratchCardReq;
import com.nms.iwebservice.ScratchCardResp;
import com.nms.iwebservice.TopupReq;
import com.nms.iwebservice.TopupResp;
import com.nms.iwebservice.VoucherPostpaidReq;
import com.nms.iwebservice.VoucherPostpaidResp;

/**
 * @author hungdt
 * 
 */
public class PaymentImpl extends PaymentWebserviceBase {
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public ScratchCardResp cardCharging(ScratchCardReq req) throws Exception {
		ScratchCardResp resp = new ScratchCardResp();
		String service = "cardCharging";

		String sessionId = getSessionId(true);
		log.info("[cardCharging][REQ]: sessionId = " + sessionId + " | "
				+ req.toString());
		try {
			HttpServletRequest request = XFireServletController.getRequest();
			authenticate(req, service, resp, request.getRemoteAddr());
			if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
				return resp;
			}

			String command = getCommand(service);
			String shortCode = getShortCode(service);

			CommandMessage message = new CommandMessage();
			message.setCorrelationID(sessionId);
			message.setChannel(Constants.CHANNEL_WEB);
			message.setMerchantId(req.getPartnerId());
			message.setOrderDate(df.parse(req.getrequestDate()));
			message.setKeyword(command);
			message.setServiceAddress(shortCode);
			message.setAgentId(req.getAgentId());
			message.setSecretCode(req.getSecretCode());
			message.setSerial(req.getSerial());
			message.setRequestId(req.getrequestId());
			CommandMessage result = sendOrder(message, req);

			log.info(result.toString());

			String error = ErrorCode.getErrorByCause(result.getCause());
			//neu ma loi do CCWS tra ve thi tra luon ma loi cho partner
			if(!result.getDescription().equals("") && error.equals(Constants.ERROR_CARD))
			{
				resp.setResult(result.getDescription());
			}
			else
			{
				resp.setResult(error);
			}
			resp.setResultDescription(ErrorCode.getErrorDetail(error));
			resp.setAmout((int) result.getAmount());

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("[cardCharging][RESP]: sessionId = " + sessionId + " | "
					+ resp.toString());
		}

		return resp;
	}

	
	public TopupResp topupOnline(TopupReq req) throws Exception {
		TopupResp resp = new TopupResp();
		String service = "topupOnline";

		String sessionId = getSessionId(true);
		log.info("[topupOnline][REQ]: sessionId = " + sessionId + " | "
				+ req.toString());
		try {
			log.info("get ipaddr");
			HttpServletRequest request = XFireServletController.getRequest();

			log.info("IP addr: " + request.getRemoteAddr());
			authenticate(req, service, resp, request.getRemoteAddr());
			if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
				return resp;
			}

			if (req.getAmount() <= 0) {
				resp.setAmout((int) req.getAmount());
				resp.setResult(ErrorCode.SVC_INVALID_AMOUNT);
				resp.setResultDescription(ErrorCode
						.getErrorDetail(ErrorCode.SVC_INVALID_AMOUNT));
				return resp;
			}

			String command = getCommand(service);
			String shortCode = getShortCode(service);

			CommandMessage message = new CommandMessage();
			message.setCorrelationID(sessionId);
			message.setChannel(Constants.CHANNEL_WEB);
			message.setMerchantId(req.getPartnerId());
			message.setOrderDate(df.parse(req.getrequestDate()));
			message.setKeyword(command);
			message.setServiceAddress(shortCode);
			message.setAgentId(req.getAgentId());
			message.setAmount(req.getAmount());
			message.setIsdn(req.getIsdn());
			message.setRequestId(req.getrequestId());

			CommandMessage result = sendOrder(message, req);

			log.info(result.toString());

			String error = ErrorCode.getErrorByCause(result.getCause());
			//neu ma loi do CCWS tra ve thi tra luon ma loi cho partner
			if(!result.getDescription().equals("") && error.equals(Constants.ERROR_CARD))
			{
				resp.setResult(result.getDescription());
			}
			else{
				resp.setResult(error);
			}
			resp.setResultDescription(ErrorCode.getErrorDetail(error));
			resp.setAmout((int) result.getAmount());

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("[topupOnline][RESP]: sessionId = " + sessionId + " | "
					+ resp.toString());
		}

		return resp;
	}

	
	public VoucherPostpaidResp voucherPostpaid(VoucherPostpaidReq req)
			throws Exception {
		VoucherPostpaidResp resp = new VoucherPostpaidResp();
		String service = "voucherPostpaid";

		String sessionId = getSessionId(true);
		log.info("[voucherPostpaid][REQ]: sessionId = " + sessionId + " | "
				+ req.toString());
		try {
			HttpServletRequest request = XFireServletController.getRequest();
			authenticate(req, service, resp, request.getRemoteAddr());
			if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
				return resp;
			}

			String command = getCommand(service);
			String shortCode = getShortCode(service);

			CommandMessage message = new CommandMessage();
			message.setCorrelationID(sessionId);
			message.setChannel(Constants.CHANNEL_WEB);
			message.setMerchantId(req.getPartnerId());
			message.setOrderDate(df.parse(req.getrequestDate()));
			message.setKeyword(command);
			message.setServiceAddress(shortCode);
			message.setAgentId(req.getAgentId());
			message.setIsdn(req.getIsdn());
			message.setSecretCode(req.getSecretCode());
			message.setSerial(req.getSerial());
			message.setRequestId(req.getrequestId());

			CommandMessage result = sendOrder(message, req);

			log.info(result.toString());

			String error = ErrorCode.getErrorByCause(result.getCause());
			resp.setResult(error);
			resp.setResultDescription(ErrorCode.getErrorDetail(error));
			resp.setAmout((int) result.getAmount());

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("[voucherPostpaid][RESP]: sessionId = " + sessionId
					+ " | " + resp.toString());
		}
		return resp;
	}

}
