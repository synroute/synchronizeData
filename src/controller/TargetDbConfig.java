package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DbConfigService;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class TargetDbConfig
 */
@SuppressWarnings("all")
public class TargetDbConfig extends HttpServlet {
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
		if (szActionValue.equals("addTargetDbConfig")) {
			try {
				String msg = "";
				String dbType = request.getParameter("dbType");
				String dbIp = request.getParameter("dbIp");
				String dbPort = request.getParameter("dbPort");
				String dbSid = request.getParameter("dbSid");
				String dbUser = request.getParameter("dbUser");
				String dbPassword = request.getParameter("dbPassword");
				if (!DbConfigService.addTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
					msg = "添加失败";
				} else {
					msg = "添加成功";
				}
				printWriter.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("dropTargetDbConfig")) {
			try {
				String msg = "";
				String dbType = request.getParameter("dbType");
				String dbIp = request.getParameter("dbIp");
				String dbPort = request.getParameter("dbPort");
				String dbSid = request.getParameter("dbSid");
				String dbUser = request.getParameter("dbUser");
				String dbPassword = request.getParameter("dbPassword");
				if (!DbConfigService.dropTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
					msg = "删除失败";
				} else {
					msg = "删除成功";
				}
				printWriter.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getTargetDbConfig")) {
			try {
				JSONArray targetDbConfig = DbConfigService.getTargetDbConfig();
				printWriter.print(targetDbConfig.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("testTargetDbConfig")) {
			String msg = "";
			String dbType = request.getParameter("dbType");
			String dbIp = request.getParameter("dbIp");
			String dbPort = request.getParameter("dbPort");
			String dbSid = request.getParameter("dbSid");
			String dbUser = request.getParameter("dbUser");
			String dbPassword = request.getParameter("dbPassword");
			if (!DbConfigService.testTargetDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
				msg = "获取来源数据库连接失败";
			} else {
				msg = "获取来源数据库连接成功";
			}
			printWriter.write(msg);
		} 
	}

}
