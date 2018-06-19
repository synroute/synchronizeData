package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DbConfigService;
import data.TableConfigService;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class tableConfigController
 */
@SuppressWarnings("all")
public class TableConfigController extends HttpServlet {
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
			try {
				JSONArray tableInfo = TableConfigService.getAllTableFromSource();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getAllFieldByTableName")) {//根据表名称获取所有列
			String tableName = request.getParameter("tableName");
			try {
				JSONArray fieldInfo = TableConfigService.getAllFieldByTableName(tableName);
				printWriter.print(fieldInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("saveTableAndField")) {//保存需要更新的表和标识列名
			String data = request.getParameter("data");
			String msg = "";
			if (!TableConfigService.saveTableAndField(data)) {
				msg = "保存失败";
			} else {
				msg = "保存成功";
			}
			printWriter.write(msg);
		} else if (szActionValue.equals("getTableAndField")) {//获取需要更新的表和标识列名
			try {
				JSONArray tableInfo = TableConfigService.getTableAndField();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  else if (szActionValue.equals("testTable")) {//测试表（单个测试）
			String tableName = request.getParameter("tableName");
			String msg = "";
			msg = TableConfigService.testTable(tableName);
			printWriter.print(msg);
		} 
	}

}
