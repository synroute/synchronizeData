package data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
* @author yagnwentian
* @version 创建时间：2018年6月13日 下午2:24:31
* 类说明
*/

public class DbUtil {
	public static Logger logger = Logger.getLogger(DbUtil.class);

	private static String driverClass;
	private static String url;
	private static String user;
	private static String password;
	static{
		try {
			ResourceBundle rb = ResourceBundle.getBundle("database");
			driverClass = rb.getString("jdbc.driverClassName");
			url = rb.getString("jdbc.url");
			user = rb.getString("jdbc.username");
			password = rb.getString("jdbc.password");
			Class.forName(driverClass);
			
		} catch (Exception e) {
        	logger.error(String.format("读取配置文件异常"+e.toString()));
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws Exception{
		return DriverManager.getConnection(url, user, password);
	}
	
	public static Connection getConnection(String url2,String user2,String password2) throws Exception{
		return DriverManager.getConnection(url2, user2, password2);
	}
	
	public static void closeAll(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();	
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	
	public static void closeDbconn(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	public static void closeRs(ResultSet rs,Statement stmt){
		if(rs!=null){
			try {
				rs.close();	
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}
	
	public static void closeDbST(Statement stmt,Connection conn){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	public static void closeST(Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}
}
