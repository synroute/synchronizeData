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
@WebServlet("/tableConfigController")
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
		if (szActionValue.equals("getAllTableFromSource")) {
			try {
				JSONArray tableInfo = TableConfigService.getAllTableFromSource();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("getAllFieldByTableName")) {
			String tableName = request.getParameter("tableName");
			try {
				JSONArray fieldInfo = TableConfigService.getAllFieldByTableName(tableName);
				printWriter.print(fieldInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (szActionValue.equals("saveTableAndField")) {
			String data = request.getParameter("data");
			String msg = "";
			if (!TableConfigService.saveTableAndField(data)) {
				msg = "保存失败";
			} else {
				msg = "保存成功";
			}
			printWriter.write(msg);
		} else if (szActionValue.equals("getTableAndField")) {
			try {
				JSONArray tableInfo = TableConfigService.getTableAndField();
				printWriter.print(tableInfo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  else if (szActionValue.equals("testTable")) {
			String tableName = request.getParameter("tableName");
			String msg = "";
			msg = TableConfigService.testTable(tableName);
			printWriter.print(msg);
		} 
	}

}
