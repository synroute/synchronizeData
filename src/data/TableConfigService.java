package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author yagnwentian
* @version 创建时间：2018年6月19日 上午11:50:16
* 类说明
*/
public class TableConfigService {

	public static JSONArray getAllTableFromSource() {
		JSONArray tableInfo = new JSONArray();
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			return tableInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
			szSql = "SELECT TABLE_NAME FROM USER_TABLES";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject table = new JSONObject();
				table.put("tableName", rs.getString(1));
				tableInfo.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return tableInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return tableInfo;
	}

	public static JSONArray getAllFieldByTableName(String tableName) {
		JSONArray fieldInfo = new JSONArray();
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			return fieldInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
			szSql = String.format("SELECT COLUMN_NAME FROM USER_TAB_COLUMNS  where TABLE_NAME='%s' ", tableName) ;
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject field = new JSONObject();
				field.put("fieldName", rs.getString(1));
				fieldInfo.add(field);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return fieldInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return fieldInfo;
	}

	public static boolean saveTableAndField(String data) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		ResultSet rs = null;
		int tableId = 0;
		try {
			dbConn = DbUtil.getConnection();
			dbConn.setAutoCommit(false);	
			szSql = String.format("SELECT TABLEID FROM SYNCHRON_CFG_DBCONN  where TYPE= 1") ;
			PreparedStatement stmt2 = dbConn.prepareStatement(szSql);
			ResultSet rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				tableId = rs.getInt(1);
				JsonParser jsonParser=new JsonParser();
				JsonArray jsonArray=jsonParser.parse(data).getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject = (JsonObject) jsonArray.get(i);
					String tableName  = jsonObject.get("tableName").getAsString();
					String fieldName  = jsonObject.get("fieldName").getAsString();
					int count = 0;
					szSql = String.format("SELECT count(*) FROM SYNCHRON_CFG_TABLE  where TABLENAME='%s' and FIELD ='%s' and TABLEID = %d ", tableName,fieldName,tableId) ;
					stmt = dbConn.prepareStatement(szSql);
					rs = stmt.executeQuery();
					if (rs.next()) {
						count = rs.getInt(1);
					}
					DbUtil.closeRs(rs,stmt);
					if(count == 0) {
						szSql = String.format("delete from  SYNCHRON_CFG_TABLE  where TABLENAME =='%s' and TABLEID = %d",tableName,tableId);
						stmt = dbConn.prepareStatement(szSql);
						stmt.execute();	
						DbUtil.closeST(stmt);	
						szSql = String.format("insert into SYNCHRON_CFG_TABLE (TABLENAME,FIELD,TABLEID) values ('%s','%s',%d)", tableName,fieldName,tableId);
						stmt = dbConn.prepareStatement(szSql);
						stmt.execute();	
						DbUtil.closeST(stmt);					
					}	
				}
			}
			DbUtil.closeRs(rs2,stmt2);
//			DbUtil.closeAll(rs2,stmt2,dbConn);
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

	public static JSONArray getTableAndField() {
		JSONArray tableInfo = new JSONArray();
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select TABLENAME,FIELD from SYNCHRON_CFG_TABLE";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject tableInfoObj = new JSONObject();
				tableInfoObj.put("tableName", rs.getString(1));
				tableInfoObj.put("fieldName", rs.getString(2));
				tableInfo.add(tableInfoObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return tableInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		return tableInfo;
	}

	public static String testTable(String tableName) {
		String msg = "";
		JSONArray targetInfo = new JSONArray();
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String url = "";
		String user = "";
		String pwd = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST from SYNCHRON_CFG_DBCONN where type =1";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals("oracle")) {
					url += "jdbc:oracle:thin:@";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ":";
					url += rs.getString(4);
				}
				user = rs.getString(5);
				pwd = rs.getString(6);	
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("url", url);
				jsonObject.put("user", user);
				jsonObject.put("pwd", pwd);
				targetInfo.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			for (int i=0;i<targetInfo.size();i++) {
				JSONObject jsonObject = (JSONObject) targetInfo.get(i);
				String url2 = jsonObject.getString("url");
				String user2 = jsonObject.getString("user");
				String pwd2 = jsonObject.getString("pwd");
				dbConn = DbUtil.getConnection(url2,user2,pwd2);
				int count = 0;
				szSql = String.format("SELECT count(*) FROM USER_TABLES where TABLE_NAME = '%s'", tableName);
				stmt = dbConn.prepareStatement(szSql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
				}
				if (count == 0) {
					msg += tableName;
					msg += "在";
					msg += url2 ;
					msg += "中不存在" ;
					return msg;
				}
				DbUtil.closeAll(rs, stmt, dbConn);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "异常";
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return tableName+"测试通过";
	}

}