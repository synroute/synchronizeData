package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

/**
* @author yagnwentian
* @version 创建时间：2018年6月19日 上午11:17:39
* 类说明
*/
public class IntervalService {

	public static boolean saveInterval(int interval) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		try {
			dbConn = DbUtil.getConnection();
			dbConn.setAutoCommit(false);
			szSql = String.format("delete from SYNCHRON_CFG_INTELVAL ");
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
			DbUtil.closeST(stmt);
			szSql = String.format("insert into SYNCHRON_CFG_INTELVAL (INTERVAL,ISSTART) values (%d,0)",interval);
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

	public static JSONObject getIntervalAndState() throws Exception {   
		Connection conn = null;  
		PreparedStatement stmt = null;  
		ResultSet rs = null;  
		JSONObject jsonObject = new JSONObject();
		try {  
		    conn = DbUtil.getConnection();  
		    String sql = "select INTERVAL,ISSTART from SYNCHRON_CFG_INTELVAL";
		    stmt = conn.prepareStatement(sql);  
		    rs = stmt.executeQuery();  
		    if (rs.next()) {  
		    	jsonObject.put("interval", rs.getInt(1));
		    	jsonObject.put("isStart", rs.getInt(2));
		    }  
		}catch(Exception e) {  
		    e.printStackTrace();  
		    return jsonObject;
		}finally {  
			DbUtil.closeAll(rs,stmt,conn);  
		}  
		return jsonObject;  
}

	public static void changeState(String str) {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		String szSql = "";
		int state = 0 ;
		if (str.equals("start")) {
			state = 1;
		} else if (str.equals("stop")) {
			state = 0;
		}
		try {
			dbConn = DbUtil.getConnection();
			szSql = String.format("update SYNCHRON_CFG_INTELVAL set ISSTART = %d",state);
			stmt = dbConn.prepareStatement(szSql);
			stmt.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {	
			DbUtil.closeDbST(stmt, dbConn);
		}	
	}

}
