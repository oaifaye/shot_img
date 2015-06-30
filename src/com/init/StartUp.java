package com.init;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.param.ParamHelper;
import com.shot_img.ShotHandler;
import com.shot_img.ShotHttpServer;
import com.timer_task.DelTempDirTimerTask;
public class StartUp implements ServletContextListener {

	public StartUp() {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			initParam(sce);
			initServer(sce);
			startTimerTask();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0); 
		}
	}
	
	private void initParam(ServletContextEvent sce) throws Exception {
		String path = sce.getServletContext().getInitParameter("img_file_path");
		if(path == null){
			throw new Exception("未发现参数img_file_path");
		}
		new ParamHelper().setImgFilePath(path);
		
		long timeout = Long.valueOf(sce.getServletContext().getInitParameter("shot_time_out"));
		new ParamHelper().setShotTimeOut(timeout);
		
		new ParamHelper().setShotScriptPath(sce.getServletContext().getInitParameter("shot_script_path"));
		
	}
	
	private void initServer(ServletContextEvent sce) throws Exception {
		String port = sce.getServletContext().getInitParameter("server_port");
		if(port == null){
			throw new Exception("未发现参数port");
		}
		new ShotHttpServer().serverStart(Integer.valueOf(port));
	}
	
	private void startTimerTask(){
		Timer timer = new Timer();
		timer.schedule(new DelTempDirTimerTask(), 5000 , 3600 * 24 * 1000);
	}
}
