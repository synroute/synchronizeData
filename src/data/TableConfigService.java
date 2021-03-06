package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import javax.enterprise.inject.New;
import javax.validation.TraversableResolver;

import org.apache.log4j.Logger;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.asm.Handle;

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
	public static Logger logger = Logger.getLogger(TableConfigService.class);
	@SuppressWarnings("resource")
	public static JSONArray getAllTableFromSource() {
		JSONArray tableInfo = new JSONArray();
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String url = "";
		String sourceUser = "";
		String sourcePwd = "";
		String sourceType = "";
		String dbName = "";
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
					sourceType = "oracle";
				} else if (rs.getString(1).equals("sqlServer")) {
					url += "jdbc:jtds:sqlserver://";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ";DatabaseName=";
					url += rs.getString(4);
					sourceType = "sqlServer";
				}
				dbName =  rs.getString(4);
				sourceUser = rs.getString(5);
				sourcePwd = rs.getString(6);	
			}
		} catch (Exception e) {
			logger.error(String.format("getAllTableFromSource"+szSql));
			e.printStackTrace();
			return tableInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			if (sourceType.equals("oracle")) {
				dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
				szSql = "SELECT TABLE_NAME FROM USER_TABLES order by TABLE_NAME";
			} else if (sourceType.equals("sqlServer")) {
				dbConn = DbUtil.getSqlserverConnection(url,sourceUser,sourcePwd);
				szSql = String.format("SELECT name FROM %s..sysobjects Where xtype='U' ORDER BY name", dbName);
			}
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject table = new JSONObject();
				table.put("tableName", rs.getString(1));
				tableInfo.add(table);
			}
		} catch (Exception e) {
			logger.error(String.format("getAllTableFromSource"+szSql));
			e.printStackTrace();
			return tableInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return tableInfo;
	}

	@SuppressWarnings("resource")
	public static JSONArray getAllFieldByTableName(String tableName) {
		JSONArray fieldInfo = new JSONArray();
		Connection dbConn = null;
		String szSql = "";
		Statement stmt = null;
		ResultSet rs = null;
		String url = "";
		String sourceUser = "";
		String sourcePwd = "";
		String sourceType = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST from SYNCHRON_CFG_DBCONN where type =0";
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(szSql);
			if (rs.next()) {
				if (rs.getString(1).equals("oracle")) {
					url += "jdbc:oracle:thin:@";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ":";
					url += rs.getString(4);
					sourceType = "oracle";
				} else if (rs.getString(1).equals("sqlServer")) {
					url += "jdbc:jtds:sqlserver://";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ";DatabaseName=";
					url += rs.getString(4);
					sourceType = "sqlServer";
				}
				sourceUser = rs.getString(5);
				sourcePwd = rs.getString(6);	
			}
		} catch (Exception e) {
			logger.error(String.format("getAllFieldByTableName"+szSql));
			e.printStackTrace();
			return fieldInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			if (sourceType.equals("oracle")) {
				dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
				szSql = String.format("SELECT b.COLUMN_NAME,b.DATA_TYPE,a.COMMENTS FROM USER_TAB_COLUMNS b,USER_COL_COMMENTS a WHERE b.TABLE_NAME = '%s' AND b.TABLE_NAME = a.TABLE_NAME AND b.COLUMN_NAME = a.COLUMN_NAME", tableName) ;
			} else if (sourceType.equals("sqlServer")) {
				dbConn = DbUtil.getSqlserverConnection(url,sourceUser,sourcePwd);
				szSql = String.format("select column_name,data_type from information_schema.columns where table_name = '%s' ",tableName);
			}
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(szSql);
			JSONObject defaultField = new JSONObject();
			defaultField.put("fieldName", "无标识列");
			defaultField.put("fieldType", "无");
			defaultField.put("comments", "选择无标识列,将全量同步");
			fieldInfo.add(defaultField);
			while (rs.next()) {
				JSONObject field = new JSONObject();
				field.put("fieldName", rs.getString(1));
				field.put("fieldType", rs.getString(2));
//				field.put("comments", rs.getString(3));
				fieldInfo.add(field);
			}
		} catch (Exception e) {
			logger.error(String.format("getAllFieldByTableName"+szSql));
			e.printStackTrace();
			return fieldInfo;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return fieldInfo;
	}

	public static boolean saveTableAndField(String data) {
		Connection dbConn = null;
		Statement stmt = null;
		String szSql = "";
		ResultSet rs = null;
		int tableId = 0;
		List<Map<String, String>> tableNames = new ArrayList<Map<String, String>>();
		try {
			dbConn = DbUtil.getConnection();
			dbConn.setAutoCommit(false);	
			szSql = String.format("SELECT ID FROM SYNCHRON_CFG_DBCONN  where TYPE= 1") ;
			Statement stmt2 = dbConn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(szSql);
			while (rs2.next()) {
				tableId = rs2.getInt(1);
				JsonParser jsonParser=new JsonParser();
				JsonArray jsonArray=jsonParser.parse(data).getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject = (JsonObject) jsonArray.get(i);
					String tableName  = jsonObject.get("tableName").getAsString();
					String fieldName  = jsonObject.get("fieldName").getAsString();
					Map<String, String> mapTableName = new HashMap<>();
					mapTableName.put("tableName", tableName);
					tableNames.add(mapTableName);
					int count = 0;
					szSql = String.format("SELECT count(*) FROM SYNCHRON_CFG_TABLE  where TABLENAME='%s' and FIELD ='%s' and TABLEID = %d ", tableName,fieldName,tableId) ;
					stmt = dbConn.createStatement();
					rs = stmt.executeQuery(szSql);
					if (rs.next()) {
						count = rs.getInt(1);
					}
					DbUtil.closeRs(rs,stmt);
					if(count == 0) {
						szSql = String.format("delete from  SYNCHRON_CFG_TABLE  where TABLENAME ='%s' and TABLEID = %d",tableName,tableId);
						stmt = dbConn.createStatement();
						stmt.execute(szSql);	
						DbUtil.closeST(stmt);
						//同步删除目标数据库里的相应表的数据
						dropDataFromTargetDb(tableName,tableId);
						
						szSql = String.format("insert into SYNCHRON_CFG_TABLE (TABLENAME,FIELD,TABLEID) values ('%s','%s',%d)", tableName,fieldName,tableId);
						stmt = dbConn.createStatement();
						stmt.execute(szSql);	
						DbUtil.closeST(stmt);					
					}	
				}
			}
			DbUtil.closeRs(rs2,stmt2);
			
			String deletesql = "";
			deletesql += "delete from SYNCHRON_CFG_TABLE where TABLENAME not in (";	
			for (int ii = 0; ii < tableNames.size(); ii++) {
				String tableName = tableNames.get(ii).get("tableName");
				deletesql +="'";
				deletesql += tableName;
				deletesql +="'";
				deletesql += ",";
			}
			deletesql = deletesql.substring(0,deletesql.length() - 1);
			deletesql+=")";
			stmt = dbConn.createStatement();
			stmt.execute(deletesql);	
			DbUtil.closeST(stmt);
			
//			DbUtil.closeAll(rs2,stmt2,dbConn);
			dbConn.commit();				
		} catch (SQLException e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			logger.error(String.format("saveTableAndField"+e.toString()));
			e.printStackTrace();
			return false;
		}  catch (Exception e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			logger.error(String.format("saveTableAndField"+e.toString()));
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

	private static void dropDataFromTargetDb(String tableName, int tableId) {
		Connection dbConn = null;
		String szSql = "";
		Statement stmt = null;
		ResultSet rs = null;
		String url = "";
		String sourceUser = "";
		String sourcePwd = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST from SYNCHRON_CFG_DBCONN where type =1 and id = '%s'", tableId) ;
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(szSql);
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
			logger.error(String.format("dropDataFromTargetDb"+szSql));
			e.printStackTrace();
			return;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		try {
			//重新获取连接
			dbConn = DbUtil.getConnection(url,sourceUser,sourcePwd);
			szSql = String.format("delete from '%s'", tableName);
			stmt = dbConn.createStatement();
			stmt.execute(szSql);	
		} catch (Exception e) {
			logger.error(String.format("dropDataFromTargetDb"+szSql));
			e.printStackTrace();
			return;
		} finally {
			DbUtil.closeDbST(stmt, dbConn);
		}
		return;
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
			logger.error(String.format("getTableAndField"+e.toString()));
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
		Statement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST,ID from SYNCHRON_CFG_DBCONN where type =1";
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(szSql);
			while (rs.next()) {
				String url = "";
				String user = "";
				String pwd = "";
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
				String dbId = rs.getString(9);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("url", url);
				jsonObject.put("user", user);
				jsonObject.put("pwd", pwd);
				jsonObject.put("dbId", dbId);
				targetInfo.add(jsonObject);
			}
		} catch (Exception e) {
			logger.error(String.format("testTable"+e.toString()));
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
				int dbId = Integer.valueOf(jsonObject.getString("dbId"));
				dbConn = DbUtil.getConnection(url2,user2,pwd2);
				int count = 0;
				String tableName2 = tableName.toUpperCase();
				szSql = String.format("SELECT count(*) FROM USER_TABLES where TABLE_NAME = '%s'", tableName2);
				stmt = dbConn.createStatement();
				rs = stmt.executeQuery(szSql);
				if (rs.next()) {
					count = rs.getInt(1);
					updateDbStateInfo(1,dbId);
				}
				if (count == 0) {
					msg += tableName;
					msg += "在";
					msg += url2 ;
					msg += "中不存在" ;
					updateDbStateInfo(0,dbId);
					return msg;
				}
				DbUtil.closeAll(rs, stmt, dbConn);	
			}
		} catch (Exception e) {
			logger.error(String.format("testTable"+e.toString()));
			e.printStackTrace();
			return "异常";
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return tableName+"测试通过";
	}

	private static void updateDbStateInfo(int num, int dbId) {
		Connection dbConn = null;
		Statement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("update SYNCHRON_CFG_DBCONN  set TABLEPASSTEST=%d  where TYPE =1 and ID =%d",num,dbId);
			stmt = dbConn.createStatement();
			stmt.execute(szSql);	
		} catch (SQLException e) {
			logger.error(String.format("updateDbStateInfo异常"+e.toString()));
			e.printStackTrace();
			return;
		}  catch (Exception e) {
			logger.error(String.format("updateDbStateInfo异常"+e.toString()));
			e.printStackTrace();
			return;
		} finally {
			DbUtil.closeDbST(stmt, dbConn);
		}	
		return;
	}

	public static String allTablePass() {
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String msg = "0";
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select count(*) from SYNCHRON_CFG_DBCONN where type =1 and tablepasstest = 0";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1)==0) {
					msg = "1";
				}
			}
		} catch (Exception e) {
			logger.error(String.format("getTableAndField"+e.toString()));
			e.printStackTrace();
			return msg;
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		return msg;
	}

}
