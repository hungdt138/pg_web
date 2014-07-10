/**
 * 
 */
package com.nms.iwebservice;

import com.crm.kernel.message.Constants;
import com.nms.cp.ServicesImpl;

/**
 * @author Hung
 * 
 */
public class ErrorCode {

	public static final String SVC_SUCCESS = "SVC0000";

	// system error
	public static final String SVC_ERROR = "SVC0001";

	// Invalid input value
	public static final String SVC_INVALID_INPUT_VALUE = "SVC0002";

	// not has permission
	public static final String SVC_NOT_HAS_PERMISSION = "SVC0003";

	// IP address not allowed
	public static final String SVC_IP_REJECT = "SVC0004";

	// The function of sending a message to a group is not supported
	public static final String SVC_INVALID_AMOUNT = "SVC0005";

	// Transaction duplicated
	public static final String SVC_TRANSACTION_DUPLICATED = "SVC0006";

	// The concurrent connection limit
	public static final String SVC_CONNECTION_LIMIT = "SVC0007";

	// TPS limit
	public static final String SVC_TPS_LIMIT = "SVC0008";
	
	public static final String SVC_INVALID_REQUESTID = "SVC0009";

	// Connection timeout
	public static final String SVC_TIMEOUT = "SVC0010";

	//Error Spam
	public static final String SVC_SPAM = "SVC0011";

	//Error Card
	public static final String SVC_INVALID_CARD = "SVC0012";

	// The transaction does not exist
	public static final String SVC_TRANSACTION_NOT_EXIST = "SVC0013";

	// Subscriber does not exist
	public static final String SVC_SUBSCRIBER_NOT_EXITS = "SVC0014";

	// Invalid syntax
	public static final String SVC_INVALID_SYNTAX = "SVC0015";

	// Denied subscriber type
	public static final String SVC_DENIED_SUBSCRIBER_TYPE = "SVC0016";

	// Access authentication or authorization error
	public static final String SVC_ACCESS_AUTHENTICATION_ERROR = "SVC0017";


	public static String getErrorDetail(String errorcode) throws Exception {

		if (errorcode.equals(SVC_SUCCESS)) {
			return "success";
		} else if (errorcode.equals(SVC_ERROR)) {
			return "error";
		} else if (errorcode.equals(SVC_INVALID_INPUT_VALUE)) {
			return "Invalid input value";
		}  else if (errorcode.equals(SVC_ACCESS_AUTHENTICATION_ERROR)) {
			return "Access authentication or authorization error";
		}  else if (errorcode.equals(SVC_TIMEOUT)) {
			return "Connection timeout";
		}  else if (errorcode.equals(SVC_TPS_LIMIT)) {
			return "TPS limit";
		} else if (errorcode.equals(SVC_INVALID_AMOUNT)) {
			return "Invalid amount";
		}  else if (errorcode.equals(SVC_IP_REJECT)) {
			return "IP address not allowed";
		} else if (errorcode.equals(SVC_CONNECTION_LIMIT)) {
			return "The concurrent connection limit";
		} else if (errorcode.equals(SVC_TRANSACTION_NOT_EXIST)) {
			return "The transaction does not exist";
		} else if (errorcode.equals(SVC_SUBSCRIBER_NOT_EXITS)) {
			return "Subscriber does not exist";
		}  else if (errorcode.equals(SVC_TRANSACTION_DUPLICATED)) {
			return "Transaction duplicated";
		} else if (errorcode.equals(SVC_INVALID_SYNTAX)) {
			return "Invalid syntax";
		} else if (errorcode.equals(SVC_DENIED_SUBSCRIBER_TYPE)) {
			return "Denied subscriber type";
		}  else if (errorcode.equals(SVC_NOT_HAS_PERMISSION)) {
			return "User not has permisstion";
		}  else if (errorcode.equals(SVC_SPAM)) {
			return "Error spam, pls try again later";
		} else if (errorcode.equals(SVC_INVALID_CARD)) {
			return "Card invalid, pls check again";
		} else if (errorcode.equals(SVC_INVALID_REQUESTID)) {
			return ("Invalid requestId");
		} else {
			return ServicesImpl.getErrorDetail("RESPONSE_CODE", errorcode);
		}

	}

	public static String getErrorByCause(String cause) {
		if  (Constants.ERROR_DUPLICATED.equals(cause)) {
			return SVC_TRANSACTION_DUPLICATED;
		} else if (Constants.ERROR_TIMEOUT.equals(cause)) {
			return SVC_TIMEOUT;
		} else if (Constants.SUCCESS.equals(cause)) {
			return SVC_SUCCESS;
		} else if (Constants.ERROR.equals(cause)) {
			return SVC_ERROR;
		}  else if (Constants.ERROR_INVALID_SYNTAX.equals(cause)) {
			return SVC_INVALID_SYNTAX;
		} else if (Constants.ERROR_DENIED_SUBSCRIBER_TYPE.equals(cause)) {
			return SVC_DENIED_SUBSCRIBER_TYPE;
		} else if (Constants.ERROR_INVALID_REQUEST.equals(cause)) {
			return SVC_TRANSACTION_NOT_EXIST;
		}  else if (Constants.ERROR_INVALID_AMOUNT.equals(cause)) {
			return SVC_INVALID_AMOUNT;
		} else if (Constants.ERROR_SPAM.equals(cause)) {
			return SVC_SPAM;
		} else if (Constants.ERROR_CARD.equals(cause)) {
			return SVC_INVALID_CARD;
		} else {
			return cause;
		}
	}

}
