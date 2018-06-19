package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;





/**
* @author yagnwentian
* @version 创建时间：2018年6月13日 上午10:24:44
* 类说明
*/
public class SynchronizeService {
	public static int interval;
	
	//获取更新间隔，毫秒
	public static int getInterval() {
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select INTERVAL from SYNCHRON_CFG_INTELVAL ";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				interval = rs.getInt(1)*60*1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}
		return interval;
	}

	//数据同步服务
	public static void synchronizeData() {
		Map<String , Object> mapDbInfo = new HashMap<>();
		//获取来源库和目标库配置
		getDbInfo(mapDbInfo);
		//获取 同步的表/字段/上次更新标识
		getTableInfo(mapDbInfo);
		//判断标识字段类型，根据标识拿到需要更新的数据,并同步到目标表中
		synchronize(mapDbInfo);
	}

	//同步数据 ，遍历表，依次从表中拿出数据，依次更新，加入日志
	private static void synchronize(Map<String, Object> mapDbInfo) {
		String tablestr = mapDbInfo.get("tableInfo").toString();
		JsonParser jsonParser=new JsonParser();
		JsonArray jsonArray=jsonParser.parse(tablestr).getAsJsonArray();
		//遍历所有的表
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonObject = (JsonObject) jsonArray.get(i);
			String tableName = jsonObject.get("tableName").getAsString();
			String fieldName = jsonObject.get("fieldName").getAsString();
			String identity = "";
			if (!jsonObject.get("identity").isJsonNull()) {
				identity  = jsonObject.get("identity").getAsString();
			}
			//根据表名，字段名，记录标识从来源表中拿出需更新的数据，转码utf-8
			List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
			queryDataFromSourceDb(tableName,fieldName,identity,mapDbInfo,listData);
			
			//将获取到的数据同步到目标表中，插入失败事务回滚，插入成功更新记录标识
			insertDataToTargetDb(tableName,fieldName,mapDbInfo,listData);
			
		}
		
		
	}

	//将获取到的数据同步到目标表中，插入失败事务回滚，插入成功更新记录标识
	private static void insertDataToTargetDb(String tableName, String fieldName, Map<String, Object> mapDbInfo,
			List<Map<String, Object>> listData) {
		String targetUrl = mapDbInfo.get("targetUrl").toString();
		String targetUser = mapDbInfo.get("targetUser").toString();
		String targetPwd = mapDbInfo.get("targetPwd").toString();
		//表列名 list
		List<Map<String, String>> listCol = new ArrayList<Map<String, String>>();
		Connection dbConn = null;
		String insert = "";
		String szSql = "";
		String identity = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection(targetUrl, targetUser, targetPwd);
			szSql = String.format("SELECT COLUMN_NAME,DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '%s'", tableName);
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			insert +="insert into ";
			insert +=tableName;
			insert +=" (";
			while (rs.next()) {
				insert += rs.getString(1);
				insert +=",";
				Map<String , String> mapCol = new HashMap<>();
				mapCol.put("colName", rs.getString(1));
				mapCol.put("dataType", rs.getString(2));
				listCol.add(mapCol);
			}
			insert = insert.substring(0,insert.length() - 1);
			insert +=") ";
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		
		try {
			//再次获取连接
			dbConn = DbUtil.getConnection(targetUrl, targetUser, targetPwd);
			dbConn.setAutoCommit(false);
			for (int i = 0; i < listData.size(); i++) {
				String insertSql = insert;
				String values = "";
				Map<String, Object> mapData = listData.get(i);
				for (Map<String, String> mapCol : listCol) {
					String colName =  mapCol.get("colName");
					String dataType =  mapCol.get("dataType");
					if (dataType.equals("DATE")) {
						values += "to_date('";
						values += mapData.get(colName);
						values += "','yyyy-mm-dd hh24:mi:ss') ";
					} else {
						values +="'";
						values += mapData.get(colName);
						values +="' ";
					}
					values +=",";
					if (colName.equals(fieldName)) {
						identity = mapData.get(colName).toString();
					}
				}
				values = values.substring(0,values.length() - 1);
				insertSql += " values (";
				insertSql += values;
				insertSql +=")";
				stmt = dbConn.prepareStatement(insertSql);
				stmt.execute();		
				DbUtil.closeST(stmt);
			}
			dbConn.commit();
			//更新同步数据标识
			updateIdentity(tableName,fieldName,identity);
			
		} catch (Exception e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			e.printStackTrace();
		} finally {
			try {
				dbConn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		
	}

	//更新同步数据标识
	@SuppressWarnings("resource")
	private static void updateIdentity(String tableName, String fieldName, String identity) throws Exception {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			dbConn.setAutoCommit(false);
			if (!identity.equals("")) {
				szSql = String.format("delete from SYNCHRON_CFG_TABLE where TABLENAME ='%s' and FIELD ='%s' ", tableName,fieldName);
				stmt = dbConn.prepareStatement(szSql);
				stmt.execute();	
				szSql = String.format("insert into SYNCHRON_CFG_TABLE (TABLENAME,FIELD,IDENTIFY) values ('%s','%s','%s')", tableName,fieldName,identity);
				stmt = dbConn.prepareStatement(szSql);
				stmt.execute();	
				dbConn.commit();				
			}
			//select TABLENAME,FIELD,IDENTIFY from SYNCHRON_CFG_TABLE
		} catch (SQLException e) {
			try {
                dbConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
			e.printStackTrace();
		} finally {
			try {
				dbConn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeDbST(stmt, dbConn);
		}	
		
		
		
		
	}

	//根据表名，字段名，记录标识拿出需更新的数据，转码utf-8
	private static void queryDataFromSourceDb(String tableName, String fieldName, String identity, Map<String, Object> mapDbInfo, List<Map<String, Object>> listData) {
		String sourceUrl = mapDbInfo.get("sourceUrl").toString();
		String sourceUser = mapDbInfo.get("sourceUser").toString();
		String sourcePwd = mapDbInfo.get("sourcePwd").toString();
		//表列名 list
		List<Map<String, String>> listCol = new ArrayList<Map<String, String>>();
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String querySql = "";
		String fieldType = "";
		try {
			//获取来源库DB连接
			dbConn = DbUtil.getConnection(sourceUrl,sourceUser,sourcePwd);
			szSql = String.format("SELECT COLUMN_NAME,DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '%s'", tableName);
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			querySql +="select  ";
			while (rs.next()) {
				if (rs.getString(2).equals("DATE")) {
					querySql += "to_char(";
					querySql += rs.getString(1);
					querySql += ",'yyyy-mm-dd hh24:mi:ss')";
				} else {
					querySql += rs.getString(1);
				}
				querySql +=",";
				if (rs.getString(1).equals(fieldName)) {
					fieldType = rs.getString(2);
				}
				Map<String , String> mapCol = new HashMap<>();
				mapCol.put("colName", rs.getString(1));
				mapCol.put("dataType", rs.getString(2));
				listCol.add(mapCol);
			}
			querySql = querySql.substring(0,querySql.length() - 1);
			querySql +=" from ";
			querySql += tableName;
			if (!identity.equals("")) {
				querySql +=" where ";
				if (fieldType.equals("DATE")) {
					querySql += fieldName;
					querySql += " > to_date('";
					querySql += identity;
					querySql += "','yyyy-mm-dd hh24:mi:ss')";
				} else {
					querySql += fieldName;
					querySql += " > ";
					querySql += identity;
				}	
			} 
			querySql += " order by ";
			querySql += fieldName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeRs(rs, stmt);
		}	
		try {
			stmt = dbConn.prepareStatement(querySql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String , Object> mapData = new HashMap<>();
				for (int i = 0; i < listCol.size(); i++) {
					Map<String, String> map = listCol.get(i);
					String key = map.get("colName");
					String str = rs.getString(i+1);
					String value = new String(str.getBytes("UTF-8"));
					mapData.put(key, value);
				}
//				for (Map<String, String> map : listCol) {
//					String key = map.get("colName");
//					String str = rs.getString(key);
//					String value = new String(str.getBytes("UTF-8"));
//					mapData.put(key, value);
//				}
				listData.add(mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
	}

	//获取 同步的表/字段/上次更新标识
	private static void getTableInfo(Map<String, Object> mapDbInfo) {
		List<Map<String, Object>> listTableInfo = new ArrayList<Map<String,Object>>();
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select TABLENAME,FIELD,IDENTIFY from SYNCHRON_CFG_TABLE";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> mapTableInfo = new HashMap<>();
				mapTableInfo.put("tableName", rs.getString(1));
				mapTableInfo.put("fieldName", rs.getString(2));
				String identity = "";
				if (rs.getString(3)!=null) {
					identity = rs.getString(3);
				}
				mapTableInfo.put("identity",identity);
				listTableInfo.add(mapTableInfo);
			}
			String tableInfo = JSONArray.fromObject(listTableInfo).toString(); 
			mapDbInfo.put("tableInfo", tableInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		return;
	}

	//获取来源库和目标库配置
	private static void getDbInfo(Map<String , Object> mapDbInfo) {
		Connection dbConn = null;
		String szSql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			dbConn = DbUtil.getConnection();
			szSql = "select DBTYPE,DBIP,DBPORT,DBSID,DBUSER,DBPWD,TYPE,PASSTEST from SYNCHRON_CFG_DBCONN";
			stmt = dbConn.prepareStatement(szSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String url = "";
				if (rs.getString(1).equals("oracle")) {
					url += "jdbc:oracle:thin:@";
					url += rs.getString(2);
					url += ":";
					url += rs.getString(3);
					url += ":";
					url += rs.getString(4);
				}
				int type = rs.getInt(7);
				if (type == 0) {
					mapDbInfo.put("sourceUrl", url);
					mapDbInfo.put("sourceUser", rs.getString(5));
					mapDbInfo.put("sourcePwd", rs.getString(6));
				} else if (type == 1) {
					mapDbInfo.put("targetUrl", url);
					mapDbInfo.put("targetUser", rs.getString(5));
					mapDbInfo.put("targetPwd", rs.getString(6));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll(rs, stmt, dbConn);
		}	
		return;
	}
}