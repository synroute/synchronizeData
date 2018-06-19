package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author yagnwentian
* @version 创建时间：2018年6月19日 上午10:04:29
* 类说明
*/
public class DbConfigService {

	public static boolean saveSourceDbConfig(String dbType, String dbIp, String dbPort, String dbSid, String dbUser,
			String dbPassword) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			dbConn.setAutoCommit(false);
			szSql = String.format("delete from SYNCHRON_CFG_DBCONN where type = 0 ");
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
			DbUtil.closeST(stmt);
			szSql = String.format("insert into SYNCHRON_CFG_DBCONN (DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST,ID) values ('%s','%s','%s','%s','%s','%s',0,0,S_SYNCHRON_CFG_DBCONN.nextval)", dbType,dbIp,dbPort,dbSid,dbUser,dbPassword);
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
			DbUtil.closeST(stmt);
			dbConn.commit();				
		} catch (SQLException e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			e.printStackTrace();
			return false;
		}  catch (Exception e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			e.printStackTrace();
			return false;
		} finally {
			try {
				dbConn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeDbST(stmt, dbConn);
		}	
		return true;
	}

	public static JSONArray getSourceDbConfig() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		try {
			String sql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST,ID from SYNCHRON_CFG_DBCONN where type =0";
			conn = DbUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("dbType", rs.getString("DBTYPE"));
				jsonObject.put("dbIp", rs.getString("DBIP"));
				jsonObject.put("dbPort", rs.getString("DBPORT"));
				jsonObject.put("dbSid", rs.getString("DBSID"));
				jsonObject.put("dbUser", rs.getString("DBUSER"));
				jsonObject.put("dbPassword", rs.getString("DBPWD"));
				jsonObject.put("type", rs.getString("TYPE"));
				jsonObject.put("passTest", rs.getString("PASSTEST"));
				jsonObject.put("tableId", rs.getString("ID"));
				jsonArray.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtil.closeAll(rs, stmt, conn);
		}
		return jsonArray;
	}

	public static boolean testSourceDbConfig() {
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String url = "";
		String sourceUser = "";
		String sourcePwd = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST from SYNCHRON_CFG_DBCONN where type =0";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals("oracle")) {
					url += "jdbc:oracle:thin:@";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ":";
					url += rs.getString(4);
				}
				sourceUser = rs.getString(5);
				sourcePwd = rs.getString(6);	
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
			if (dbConn != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DbUtil.closeDbconn(dbConn);
		}
	}

	public static boolean addTargetDbConfig(String dbType, String dbIp, String dbPort, String dbSid, String dbUser,
			String dbPassword) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("insert into SYNCHRON_CFG_DBCONN (DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST,ID) values ('%s','%s','%s','%s','%s','%s',1,0,S_SYNCHRON_CFG_DBCONN.nextval)", dbType,dbIp,dbPort,dbSid,dbUser,dbPassword);
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
			DbUtil.closeST(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DbUtil.closeDbST(stmt, dbConn);
		}	
		return true;
	}

	public static boolean dropTargetDbConfig(String tableId) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("delete from SYNCHRON_CFG_DBCONN  where ID='%s' ", tableId);
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
			dropTargetTableConfig(tableId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DbUtil.closeDbST(stmt, dbConn);
		}	
		return true;
	}

	private static void dropTargetTableConfig(String tableId) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("delete from SYNCHRON_CFG_TABLE  where TABLEID='%s' ", tableId);
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}  catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			DbUtil.closeDbST(stmt, dbConn);
		}	
		return;
	}

	public static JSONArray getTargetDbConfig() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		try {
			String sql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST,ID from SYNCHRON_CFG_DBCONN where type =1";
			conn = DbUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("dbType", rs.getString("DBTYPE"));
				jsonObject.put("dbIp", rs.getString("DBIP"));
				jsonObject.put("dbPort", rs.getString("DBPORT"));
				jsonObject.put("dbSid", rs.getString("DBSID"));
				jsonObject.put("dbUser", rs.getString("DBUSER"));
				jsonObject.put("dbPassword", rs.getString("DBPWD"));
				jsonObject.put("type", rs.getString("TYPE"));
				jsonObject.put("passTest", rs.getString("PASSTEST"));
				jsonObject.put("tableId", rs.getString("ID"));
				jsonArray.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtil.closeAll(rs, stmt, conn);
		}
		return jsonArray;
	}

	public static boolean testTargetDbConfig(String dbType, String dbIp, String dbPort, String dbSid, String dbUser, String dbPassword) {
		Connection dbConn = null;
		String url = "";
		String sourceUser = "";
		String sourcePwd = "";
		try {
			if (dbType.equals("oracle")) {
				url += "jdbc:oracle:thin:@";
				url += dbIp;
				url += ":";
				url += dbPort;
				url += ":";
				url += dbSid;
			sourceUser = dbUser;
			sourcePwd = dbPassword;	
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		try {
			dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
			if (dbConn != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DbUtil.closeDbconn(dbConn);
		}
	}
}
