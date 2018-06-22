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
import data.TableConfigService;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class tableConfigController
 */
@SuppressWarnings("all")
public class TableConfigController extends HttpServlet {
	public static Logger logger = Logger.getLogger(TableConfigController.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TableConfigController() {
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
		if (szActionValue.equals("getAllTableFromSource")) {//获取来源数据库所有表
			logger.info(String.format("getAllTableFromSource请求成功"));
			try {
				JSONArray tableInfo = TableConfigService.getAllTableFromSource();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				logger.error(String.format("getAllTableFromSource异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getAllFieldByTableName")) {//根据表名称获取所有列
			logger.info(String.format("getAllFieldByTableName请求成功"));
			String tableName = request.getParameter("tableName");
			try {
				JSONArray fieldInfo = TableConfigService.getAllFieldByTableName(tableName);
				printWriter.print(fieldInfo.toString());
			} catch (Exception e) {
				logger.error(String.format("getAllFieldByTableName异常"+e.toString()));
				e.printStackTrace();
			}
		} else if (szActionValue.equals("saveTableAndField")) {//保存需要更新的表和标识列名
			logger.info(String.format("saveTableAndField请求成功"));
			String data = request.getParameter("data");
			String msg = "";
			if (!TableConfigService.saveTableAndField(data)) {
				msg = "表和标识列名保存失败";
				logger.error(String.format("getAllFieldByTableName"+msg));
			} else {
				msg = "表和标识列名保存成功";
			}
			printWriter.write(msg);
		} else if (szActionValue.equals("getTableAndField")) {//获取需要更新的表和标识列名
			logger.info(String.format("getTableAndField请求成功"));
			try {
				JSONArray tableInfo = TableConfigService.getTableAndField();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				logger.error(String.format("getTableAndField异常"+e.toString()));
				e.printStackTrace();
			}
		}  else if (szActionValue.equals("testTable")) {//测试表（单个测试）
			logger.info(String.format("testTable请求成功"));
			String tableName = request.getParameter("tableName");
			String msg = "";
			msg = TableConfigService.testTable(tableName);
			printWriter.print(msg);
		}  else if (szActionValue.equals("allTablePass")) {//是否全部通过
			logger.info(String.format("allTablePass请求成功"));
			String msg = "";
			msg = TableConfigService.allTablePass();
			printWriter.print(msg);
		} 
	}

}
