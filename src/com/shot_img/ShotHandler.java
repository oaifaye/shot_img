package com.shot_img;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.param.ParamHelper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ShotHandler implements HttpHandler{

	public void handle(HttpExchange exchange) throws IOException {  
        String requestMethod = exchange.getRequestMethod();  
        System.out.println("开始");
        
        
        Map<String, Object> params = (Map<String, Object>)exchange.getAttribute("parameters"); 

        if (requestMethod.equalsIgnoreCase("GET")) {
        	String action = (String)params.get("action");
        	String op_type = (String)params.get("op_type");
        	String widthStr = (String)params.get("width");
        	String url = (String)params.get("url");
        	System.out.println(action+".."+op_type+".."+widthStr+"..");
        	if("snatch_pic".equals(action) && "url".equals(op_type)){
        		if(widthStr == null || "".equals(widthStr.replaceAll(" ",""))){
        			widthStr = "1024";
        		}
        		int width = Integer.valueOf(widthStr);
        		
        		
        		Headers responseHeaders = exchange.getResponseHeaders();  
        		OutputStream responseBody = exchange.getResponseBody();  
                
                // responseHeaders.set("Content-Type", "text/html; charset=utf-8");  
                exchange.sendResponseHeaders(200, 0);  
                
                byte[] data;
				try {
					responseHeaders.set("Content-Type", "image/x-png");  
					data = shotDo(url);
					responseBody.write(data); 
				} catch (Exception e) {
					responseHeaders.set("Content-Type", "image/x-png");  
					responseBody.write("shot fail".getBytes()); 
					e.printStackTrace();
				}
                responseBody.close();
        	}
        }  
    } 
	
	private byte[] shotDo(String url) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟  
		String dateStr = sdf.format(new Date());  
		String filePath = new ParamHelper().getImgFilePath() + "/" + dateStr + "/";
		String filePathName = filePath + UUID.randomUUID().toString().replaceAll("-","") + ".jpg";
//		SeqHelper seqHelper = new SeqHelper();
		//新建目录文件
		new File(filePath).mkdirs();
		new File(filePathName).createNewFile();
		
//		String urlParth = url + " " + filePathName;
//		seqHelper.add(urlParth);
//		long timeout = new ParamHelper().getShotTimeOut();
//		long time = 0;
//		while(true){
//			if(time > timeout){
//				throw new Exception("截图超时");
//			}
//			Thread.sleep(100);
//			time += 100;
//			if(new SeqHelper().hasFinished(urlParth)){
//				new SeqHelper().removeFinished(urlParth);
//				break;
//			}
//		}
		
		
		//执行截图命令
//		String cmd = "xvfb-run --server-args=\"-screen 0, 1024x768x24\" cutycapt  --javascript=on --plugins=on --url=" + url + " --out=" + filePathName;
		String cmd = "sh " + new ParamHelper().getShotScriptPath() + " " + url + " " + filePathName;
		System.out.println(cmd);
		Runtime rt = Runtime.getRuntime();  
		Process result = rt.exec(cmd);
		
		int i; 
		ByteArrayOutputStream error = new ByteArrayOutputStream(); 
		InputStream is = result.getErrorStream();
		while ((i = is.read()) != -1) { 
			error.write(i); 
			} 
			String errorStr = error.toString(); 
		System.out.println("error:" + errorStr);
		
		return File2byte(filePathName);
	}
	
	private byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	/**
	 * 根据不同的文件类型删除临时文件夹
	 * @param attDir
	 * @throws Exception 
	 */
	public void delTempDir() throws Exception {
		Calendar calendar=Calendar.getInstance(); 
		Calendar calendarToday=Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟  
		calendarToday.setTime(new Date()); 
		int today = calendar.get(Calendar.DAY_OF_YEAR);
		
		String imgRootPath = new ParamHelper().getImgFilePath() + "/" ;
		File[] files = new File(imgRootPath).listFiles();
		for (File file : files) {
			try{
				String fileName = file.getName();
				Date fileDate = sdf.parse(fileName);
				calendar.setTime(fileDate); 
				//删除两天前
				int date = calendar.get(Calendar.DAY_OF_YEAR)+2;
				if(date <= today){
					deleteFile(file);
					System.out.println("删除："+file.getName());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 *  根据路径删除指定的目录或文件，无论存在与否 
	 *@param sPath  要删除的目录或文件 
	 *@return 删除成功返回 true，否则返回 false。 
	 */  
	private void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}
	
}
