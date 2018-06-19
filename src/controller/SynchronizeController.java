package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.IntervalService;
import data.SynchronizeService;


/**
* @author yagnwentian
* @version 创建时间：2018年6月13日 上午10:23:22
* 类说明
*/
@SuppressWarnings("all")
public class SynchronizeController extends HttpServlet {
	private static Logger logger = LogManager.getLogger(SynchronizeController.class.getName());
	
	private static final long serialVersionUID = 1L;  
    private MyThread1 myThread1;
    private static boolean stopThread = false;
    private static boolean startThread = false;

	public SynchronizeController() {
		super();
	}

	public void init(){  
        myThread1 = new MyThread1();  
        myThread1.start(); // servlet 上下文初始化时启动 socket   
        startThread = true;
        IntervalService.changeState("start");
    }  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String state = request.getParameter("action");
		if (state.equals("end")) {
			this.destory();
			return;
		} if (state.equals("start")&&!startThread) {	
			this.restart();	
			
		}
				
	}
	
	public void restart() {
		if (myThread1 != null) { 
			stopThread = false;
			this.init();
//			myThread1.start();
//			logger.info(String.format("启动同步服务"));
	    }
	}

	public void destory(){  
        if (myThread1 != null && !myThread1.isInterrupted()) { 
        	stopThread = true;
        	startThread = false;
        	myThread1.stop(); 
        	IntervalService.changeState("stop");
        	
//        	logger.info(String.format("停止同步服务"));
        }  
    }

	public static boolean getStopThread() {
		// TODO Auto-generated method stub
		return stopThread;
	}  
        

}

//线程
class MyThread1 extends Thread {  
	int interval = SynchronizeService.getInterval();
//	int interval = 5000;
    public void run() {  
        while (!SynchronizeController.getStopThread()) {// 线程未中断执行循环  
            try {  
                Thread.sleep(interval);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
            //记录时间   加日志
            Date now = new Date(); 
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		String time = dateFormat.format(now);   
            System.out.println("TIME:" + time);
            
            //同步数据  捕获异常，加日志
            SynchronizeService.synchronizeData();
        }  
    }  
}  



