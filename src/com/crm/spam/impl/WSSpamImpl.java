/**
 * 
 */
package com.crm.spam.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.crm.kernel.sql.Database;
import com.crm.spam.bean.SpamEntry;
import com.crm.spam.bean.SpamLog;
import com.crm.util.DateUtil;

/**
 * @author hungdt
 * 
 */
public class WSSpamImpl {
	public static SpamEntry getSpamEntry(ResultSet rsSpam) throws Exception {
		SpamEntry spam = new SpamEntry();
		try {
			spam.setCommandId(rsSpam.getString("COMMANDID"));
			spam.setCompanyId(rsSpam.getLong("COMPANYID"));
			spam.setCreateDate(rsSpam.getDate("CREATEDATE"));
			spam.setDescription(rsSpam.getString("DESCRIPTION"));
			spam.setEntryId(rsSpam.getLong("ENTRYID"));
			spam.setGroupId(rsSpam.getLong("GroupId"));
			spam.setModifiedDate(rsSpam.getDate("ModifiedDate"));
			spam.setProductId(rsSpam.getString("ProductId"));
			spam.setProperties(rsSpam.getString("Properties"));
			spam.setResourcePrimKey(rsSpam.getLong("ResourcePrimKey"));
			spam.setSpamId(rsSpam.getString("SpamId"));
			spam.setSpamInterval(rsSpam.getInt("SpamInterval"));
			spam.setSpamTimes(rsSpam.getInt("SpamTimes"));
			spam.setSuspendTime(rsSpam.getInt("SuspendTime"));
			spam.setTitle(rsSpam.getString("Title"));
			spam.setUserId(rsSpam.getLong("UserId"));
			spam.setUserName(rsSpam.getString("UserName"));
			spam.setAction(rsSpam.getString("Action"));
		} catch (Exception e) {
			throw e;
		}
		return spam;
	}

	public static SpamLog getSpamLog(ResultSet rsSpam) throws Exception {
		SpamLog spam = new SpamLog();
		try {
			spam.setLogId(rsSpam.getLong("LogId"));
			spam.setCreateDate(rsSpam.getTimestamp("CreateDate"));
			spam.setExpireDate(rsSpam.getTimestamp("ExpireDate"));
			spam.setModifiedDate(rsSpam.getTimestamp("ModifiedDate"));
			spam.setSpamTimes(rsSpam.getInt("SpamTimes"));
			spam.setSpamAddress(rsSpam.getString("SpamAddress"));
			spam.setSpamDate(rsSpam.getTimestamp("SpamDate"));
			spam.setSpamType(rsSpam.getString("SpamType"));
			spam.setAction(rsSpam.getString("action"));
			spam.setIp(rsSpam.getString("iplogin"));
		} catch (Exception e) {
			throw e;
		}
		return spam;
	}

	public static int addSpam(String productId, String action,
			String spamAddress, String spamType, String description,
			long userId, String username, long groupid, long merchantid,
			long companyid, String ip) throws Exception {
		int result = 0;
		try {
			SpamLog spamLog = getSpamLog(action, productId, ip);
			SpamEntry spamEntry = getSpamEntry(productId, action);
			if (spamLog == null) {

				createSpamLog(productId, action, spamAddress, spamType,
						description, userId, username, groupid, merchantid,
						companyid, spamEntry.getSpamInterval(), ip);
				result = 1;
			} else {
				Calendar now = Calendar.getInstance();
				int spamtimes = spamLog.getSpamTimes();
				if (spamtimes <= spamEntry.getSpamTimes()) {
					if (now.getTimeInMillis() < spamLog.getExpireDate()
							.getTime()) {
						if (spamtimes == spamEntry.getSpamTimes()) {
							updateSpamLog(spamLog.getLogId(), spamtimes,
									productId, action, ip);
						} else {
							updateSpamLog(spamLog.getLogId(), spamtimes + 1,
									productId, action, ip);
						}

					} else if (now.getTimeInMillis() > spamLog.getExpireDate()
							.getTime()) {
						updateSpamLog(spamLog.getLogId(), 1, productId, action,
								ip);
						spamtimes = 1;
					}
					if (spamtimes + 1 < spamEntry.getSpamTimes()) {
						result = 2;
					} else if (spamtimes + 1 >= spamEntry.getSpamTimes()) {
						result = 3;
					}
				} else {
					result = 3;
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public static int checkSpam(String action, String iplogin, long merchantId, String productId)
			throws Exception {
		int result = 0;

		try {

			SpamLog spamLog = getSpamLog(action, "0", iplogin);
			SpamEntry spamEntry = getSpamEntry("0", action);

			if (spamLog == null) {
				result = 0;
			} else {
				Calendar now = Calendar.getInstance();
				int spamtimes = spamLog.getSpamTimes();
				if (spamtimes <= spamEntry.getSpamTimes()) {
					if (now.getTimeInMillis() < spamLog.getExpireDate()
							.getTime()) {
						
					} else if (now.getTimeInMillis() > spamLog.getExpireDate()
							.getTime()) {
						updateSpamLog(spamLog.getLogId(), 0, productId, action,
								iplogin);
						spamtimes = 1;
					}
					
					if (spamtimes + 1 < spamEntry.getSpamTimes()) {
						result = 2;
					} else if (spamtimes >= spamEntry.getSpamTimes()) {
						result = 3;
					}
				} else {
					result = 3;
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public static boolean isSpam(String productId, String action,
			String spamAddress, String spamType, String ip) throws Exception {
		Connection connection = null;
		PreparedStatement stmtSpam = null;
		SpamLog spam = null;
		ResultSet rsSpam = null;
		try {
			connection = Database.getConnection();
			String sql = "select * from spamlog where spamAddress = ? and productId = ? and action = ? and spamtype = ? and iplogin = ? and createdate > trunc(sysdate)";

			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setString(1, spamAddress);
			stmtSpam.setString(2, productId);
			stmtSpam.setString(3, action);
			stmtSpam.setString(4, spamType);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamLog(rsSpam);
			}
			SpamEntry spamEntry = getSpamEntry(productId, action);
			if (spam != null) {
				if (spam.getSpamTimes() >= spamEntry.getSpamTimes()) {
					Calendar now = Calendar.getInstance();
					if (now.getTimeInMillis() > spam.getExpireDate().getTime()) {
						updateSpamLog(spam.getLogId(), 0, productId, action, ip);
					} else {
						return true;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(connection);
			Database.closeObject(rsSpam);
			Database.closeObject(stmtSpam);
		}
		return false;
	}

	public static SpamLog deleteSpamLog(long logId, Date spamDate,
			Connection connection) {
		SpamLog spamlog = new SpamLog();
		// try {
		// String sql = "delete from spamlog where logid = ?";
		// } catch (Exception e) {
		//
		// }
		return spamlog;
	}

	/**
	 * @author Do Tien Hung
	 * @param java
	 *            .sql.Connection connection
	 * @param String
	 *            productId
	 * @param String
	 *            commandId
	 * @return info spamentry of product
	 * @throws Exception
	 */
	public static SpamEntry getSpamEntry(String productId, String action)
			throws Exception {
		SpamEntry spam = null;
		PreparedStatement stmtSpam = null;
		ResultSet rsSpam = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String sql = "select * from spamentry where productid = ? and action = ?";

			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setString(1, productId);
			stmtSpam.setString(2, action);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamEntry(rsSpam);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsSpam);
			Database.closeObject(stmtSpam);
			Database.closeObject(connection);
		}
		return spam;
	}

	/**
	 * @author Do Tien Hung
	 * @param connection
	 * @param spamAddress
	 * @return info spamlog of isdn
	 * @throws Exception
	 */
	public static SpamLog getSpamLog(String action, String productId, String ip)
			throws Exception {
		SpamLog spam = null;
		PreparedStatement stmtSpam = null;
		ResultSet rsSpam = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String sql = "select * from spamlog where action = ? and productid = ? and iplogin = ? and createdate > trunc(sysdate)";

			stmtSpam = connection.prepareStatement(sql);
			// stmtSpam.setString(1, spamAddress);
			stmtSpam.setString(1, action);
			stmtSpam.setString(2, productId);
			stmtSpam.setString(3, ip);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamLog(rsSpam);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsSpam);
			Database.closeObject(stmtSpam);
			Database.closeObject(connection);
		}
		return spam;
	}

	public static void createSpamLog(String productId, String action,
			String spamAddress, String spamType, String description,
			long userId, String username, long groupid, long merchantid,
			long companyid, int SpamInterval, String ip) throws Exception {

		PreparedStatement stmtSpam = null;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String sql = "Insert into spamlog(logid, groupid, companyid, userid, username, createdate, modifieddate, "
					+ "spamdate, spamaddress, spamtype, productid, spamtimes, expiredate, description, status, merchantid, action, iplogin) "
					+ "values (SPAM_LOG_SEQ.nextval,?,?,?,?,sysdate, sysdate, sysdate,?,?,?,?,?,?,?,?,?,?)";
			Calendar now = Calendar.getInstance();
			long expiredate = now.getTimeInMillis() + SpamInterval * 1000;
			stmtSpam = connection.prepareStatement(sql);

			stmtSpam.setLong(1, groupid);
			stmtSpam.setLong(2, companyid);
			stmtSpam.setLong(3, userId);
			stmtSpam.setString(4, username);
			stmtSpam.setString(5, spamAddress);
			stmtSpam.setString(6, spamType);
			stmtSpam.setString(7, productId);
			// stmtSpam.setString(8, commandId);
			stmtSpam.setInt(8, 1);
			stmtSpam.setTimestamp(9,
					DateUtil.getTimestampSQL(new Date(expiredate)));
			stmtSpam.setString(10, description);
			stmtSpam.setInt(11, 0);
			stmtSpam.setLong(12, merchantid);
			stmtSpam.setString(13, action);
			stmtSpam.setString(14, ip);

			stmtSpam.executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtSpam);
			Database.closeObject(connection);

		}

	}

	public static void updateSpamLog(long logid, int spamtimes,
			String productId, String commandId, String ip) throws Exception {
		PreparedStatement stmtSpam = null;
		Connection connection = null;
		SpamEntry spamEntry = getSpamEntry(productId, commandId);
		try {
			connection = Database.getConnection();
			String sql = "update spamlog set modifieddate = sysdate, spamdate = sysdate, spamtimes = ?, expiredate = ?, iplogin = ? where logid = ?";
			Calendar now = Calendar.getInstance();
			long expiredate = 0;
			if (spamtimes < spamEntry.getSpamTimes()) {
				expiredate = now.getTimeInMillis()
						+ spamEntry.getSpamInterval() * 1000;
			} else {
				expiredate = now.getTimeInMillis() + spamEntry.getSuspendTime()
						* 1000;
			}
			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setInt(1, spamtimes);
			stmtSpam.setTimestamp(2,
					DateUtil.getTimestampSQL(new java.util.Date(expiredate)));
			stmtSpam.setString(3, ip);
			stmtSpam.setLong(4, logid);

			stmtSpam.execute();

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtSpam);
			Database.closeObject(connection);
		}
	}
}
