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
import data.IntervalService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class IntervalController
 */
@SuppressWarnings("all")
public class IntervalController extends HttpServlet {
	public static Logger logger = Logger.getLogger(IntervalController.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntervalController() {
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
		if (szActionValue.equals("saveInterval")) {//保存同步间隔
			logger.info(String.format("saveInterval请求成功"));
			try {
				String msg = "";
				int interval =Integer.valueOf(request.getParameter("interval"));
				if (!IntervalService.saveInterval(interval)) {
					msg = "同步间隔保存失败";
					logger.error(String.format("saveInterval"+msg));
				} else {
					msg = "同步间隔保存成功";
				}
				printWriter.write(msg);
			} catch (Exception e) {
				logger.error(String.format("saveInterval异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getIntervalAndState")) {//获取同步间隔和启动状态
			logger.info(String.format("getIntervalAndState请求成功"));
			try {
				JSONObject intervalAndState = IntervalService.getIntervalAndState();
				printWriter.print(intervalAndState.toString());
			} catch (Exception e) {
				logger.error(String.format("getIntervalAndState异常"+e.toString()));
				e.printStackTrace();
			}
		}
	}

}
