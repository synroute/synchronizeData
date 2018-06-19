package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.DbConfigService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author yagnwentian
* @version 创建时间：2018年6月19日 上午9:45:02
* 类说明
*/
@SuppressWarnings("all")
public class SourceDbConfig  extends HttpServlet  {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SourceDbConfig() {
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
		if (szActionValue.equals("saveSourceDbConfig")) {//保存来源数据库
			try {
				String msg = "";
				String dbType = request.getParameter("dbType");
				String dbIp = request.getParameter("dbIp");
				String dbPort = request.getParameter("dbPort");
				String dbSid = request.getParameter("dbSid");
				String dbUser = request.getParameter("dbUser");
				String dbPassword = request.getParameter("dbPassword");
				if (!DbConfigService.saveSourceDbConfig(dbType,dbIp,dbPort,dbSid,dbUser,dbPassword)) {
					msg = "保存失败";
				} else {
					msg = "保存成功";
				}
				printWriter.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getSourceDbConfig")) {//获取来源数据库配置
			try {
				JSONArray sourceDbConfig = DbConfigService.getSourceDbConfig();
				printWriter.print(sourceDbConfig.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("testSourceDbConfig")) {//测试来源数据库连接
			String msg = "";
			if (!DbConfigService.testSourceDbConfig()) {
				msg = "获取来源数据库连接失败";
			} else {
				msg = "获取来源数据库连接成功";
			}
			printWriter.write(msg);
		} 
	}

}
