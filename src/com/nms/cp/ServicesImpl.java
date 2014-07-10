/**
 * 
 */
package com.nms.cp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;

/**
 * @author Hung
 * 
 */
public class ServicesImpl {

	public final static String CONDITION_ACTIVE = " (supplierStatus = "
			+ Constants.SUPPLIER_ACTIVE_STATUS + ") ";

	public final static String CONDITION_BARRING = " (supplierStatus = "
			+ Constants.SUPPLIER_BARRING_STATUS + ") ";

	public final static String CONDITION_TERMINATED = " (supplierStatus = "
			+ Constants.SUPPLIER_CANCEL_STATUS + ") ";

	public final static String CONDITION_UNTERMINATED = " (supplierStatus != "
			+ Constants.SUPPLIER_CANCEL_STATUS + ") ";

	// public static ServiceStatus getStatus(ServiceRequest request)
	// throws Exception {
	//
	// ServiceStatus response = new ServiceStatus();
	//
	// Connection connection = null;
	//
	// PreparedStatement stmtSubscription = null;
	//
	// ResultSet rsSubscription = null;
	//
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	//
	// try {
	//
	// connection = Database.getConnection();
	//
	// String sql = "Select A.* From SubscriberProduct A, ProductEntry B "
	// +
	// "Where A.productId = B.productId and isdn = ? and B.code = ? order by A.registerDate desc";
	//
	// stmtSubscription = connection.prepareStatement(sql);
	//
	// stmtSubscription.setString(1, request.getIsdn());
	// stmtSubscription.setString(2, request.getProduct());
	//
	// rsSubscription = stmtSubscription.executeQuery();
	//
	// if (rsSubscription.next()) {
	// response.setStatus(rsSubscription.getInt("supplierStatus"));
	// response.setRegisterDate(rsSubscription
	// .getTimestamp("registerDate"));
	// response.setExpirationDate(rsSubscription
	// .getTimestamp("expirationDate"));
	// }
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// Database.closeObject(rsSubscription);
	// Database.closeObject(stmtSubscription);
	// Database.closeObject(connection);
	// response.setResult(ErrorCode.SVC_SUCCESS);
	// response.setResultDescription(ErrorCode
	// .getErrorDetail(ErrorCode.SVC_SUCCESS));
	// }
	//
	// return response;
	// }

	public static boolean getActive(String isdn, String productCode)
			throws Exception {
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String SQL = "Select * "
					+ "From SubscriberProduct "
					+ "Where isdn = ? and productId = (select productId from productEntry where code = ?) and "
					+ CONDITION_ACTIVE + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setString(2, productCode);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next()) {
				return true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(connection);
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return false;
	}

	public static boolean checkRetry(String isdn, String productCode,
			int retryNum) throws Exception {
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;
		Connection connection = null;
		int count = 0;
		try {
			connection = Database.getConnection();
			String SQL = "Select * "
					+ "From SubscriberProduct "
					+ "Where isdn = ? and productId = (select productId from productEntry where code = ?) and "
					+ CONDITION_ACTIVE + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setString(2, productCode);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next()) {
				count = rsActive.getInt("retry");
			}
			if (count <= retryNum) {
				return true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(connection);
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return false;
	}
	public static boolean checkRequestId(long requestId, long partnerId, long agentId) throws Exception
	{
		boolean check = false;
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String sql = "select * from subscriberOrder where orderNo = ? and merchantId = ? and agentId = ?";
			stmtActive = connection.prepareStatement(sql);
			stmtActive.setLong(1, requestId);
			stmtActive.setLong(2, partnerId);
			stmtActive.setLong(3, agentId);
			
			rsActive = stmtActive.executeQuery();
			if(rsActive.next())
			{
				check = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return check;
	}
	
	//get error detail from appdomain
	public static String getErrorDetail(String type, String errorCode) throws Exception
	{
		String detail = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String sql = "select * from appdomain where type_ = ? and alias_ = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, type);
			stmt.setString(2, "RESP."+errorCode);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				detail = rs.getString("description");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return detail;
	}
}
