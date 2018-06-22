package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import data.DbConfigService;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class TargetDbConfig
 */
@SuppressWarnings("all")
public class TargetDbConfig extends HttpServlet {
	public static Logger logger = Logger.getLogger(TargetDbConfig.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TargetDbConfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter printWriter = response.getWriter();
		String szActionValue = request.getParameter("action");
		if (szActionValue.equals("addTargetDbConfig")) {//添加目标数据库配置
			logger.info(String.format("addTargetDbConfig请求成功"));
			try {
				String msg = "";
				String dbType = request.getParameter("dbType");
				String dbIp = request.getParameter("dbIp");
				String dbPort = request.getParameter("dbPort");
				String dbSid = request.getParameter("dbSid");
				String dbUser = request.getParameter("dbUser");
				String dbPassword = request.getParameter("dbPassword");
				msg = DbConfigService.addTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword);
//				if (!DbConfigService.addTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
//					msg = "添加目标数据库配置失败";
//					logger.error(String.format("addTargetDbConfig"+msg));
//				} else {
//					msg = "添加目标数据库配置成功";
//				}
				printWriter.write(msg);
			} catch (Exception e) {
				logger.error(String.format("addTargetDbConfig异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("modifyTargetDbConfig")) {//修改目标数据库配置
			logger.info(String.format("modifyTargetDbConfig请求成功"));
			try {
				String msg = "";
				String tableId = request.getParameter("tableId");//数据库ID
				String dbType = request.getParameter("dbType");
				String dbIp = request.getParameter("dbIp");
				String dbPort = request.getParameter("dbPort");
				String dbSid = request.getParameter("dbSid");
				String dbUser = request.getParameter("dbUser");
				String dbPassword = request.getParameter("dbPassword");
				msg = DbConfigService.modifyTargetDbConfig(tableId,dbType,dbIp,dbPort,dbSid,dbUser,dbPassword);
//				if (!DbConfigService.modifyTargetDbConfig(tableId,dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
//					msg = "修改目标数据库配置失败";
//					logger.error(String.format("addTargetDbConfig"+msg));
//				} else {
//					msg = "修改目标数据库配置成功";
//				}
				printWriter.write(msg);
			} catch (Exception e) {
				logger.error(String.format("modifyTargetDbConfig异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("dropTargetDbConfig")) {//删除目标数据库配置
			logger.info(String.format("dropTargetDbConfig请求成功"));
			try {
				String msg = "";
				String tableId = request.getParameter("tableId");
				if (!DbConfigService.dropTargetDbConfig(tableId)) {
					msg = "删除目标数据库配置失败";
					logger.error(String.format("dropTargetDbConfig"+msg));
				} else {
					msg = "删除目标数据库配置成功";
				}
				printWriter.write(msg);
			} catch (Exception e) {
				logger.error(String.format("dropTargetDbConfig异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getTargetDbConfig")) {//获取目标数据库配置
			logger.info(String.format("getTargetDbConfig请求成功"));
			try {
				JSONArray targetDbConfig = DbConfigService.getTargetDbConfig();
				printWriter.print(targetDbConfig.toString());
			} catch (Exception e) {
				logger.error(String.format("getTargetDbConfig异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("testTargetDbConfig")) {//测试目标数据库连接
			logger.info(String.format("testTargetDbConfig请求成功"));
			String msg = "";
			String dbType = request.getParameter("dbType");
			String dbIp = request.getParameter("dbIp");
			String dbPort = request.getParameter("dbPort");
			String dbSid = request.getParameter("dbSid");
			String dbUser = request.getParameter("dbUser");
			String dbPassword = request.getParameter("dbPassword");
			if (!DbConfigService.testTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
				msg = "获取目标数据库连接失败";
			} else {
				msg = "获取目标数据库连接成功";
				DbConfigService.modifyTargetDbState(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword);
			}
			printWriter.write(msg);
		} 
	}

}
