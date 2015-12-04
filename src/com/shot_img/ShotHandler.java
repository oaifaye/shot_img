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
        System.out.println("��ʼ");
        
        
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//Сд��mm��ʾ���Ƿ���  
		String dateStr = sdf.format(new Date());  
		String filePath = new ParamHelper().getImgFilePath() + "/" + dateStr + "/";
		String filePathName = filePath + UUID.randomUUID().toString().replaceAll("-","") + ".jpg";
//		SeqHelper seqHelper = new SeqHelper();
		//�½�Ŀ¼�ļ�
		new File(filePath).mkdirs();
		new File(filePathName).createNewFile();
		
//		String urlParth = url + " " + filePathName;
//		seqHelper.add(urlParth);
//		long timeout = new ParamHelper().getShotTimeOut();
//		long time = 0;
//		while(true){
//			if(time > timeout){
//				throw new Exception("��ͼ��ʱ");
//			}
//			Thread.sleep(100);
//			time += 100;
//			if(new SeqHelper().hasFinished(urlParth)){
//				new SeqHelper().removeFinished(urlParth);
//				break;
//			}
//		}
		
		
		//ִ�н�ͼ����
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
	 * ���ݲ�ͬ���ļ�����ɾ����ʱ�ļ���
	 * @param attDir
	 * @throws Exception 
	 */
	public void delTempDir() throws Exception {
		Calendar calendar=Calendar.getInstance(); 
		Calendar calendarToday=Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//Сд��mm��ʾ���Ƿ���  
		calendarToday.setTime(new Date()); 
		int today = calendar.get(Calendar.DAY_OF_YEAR);
		
		String imgRootPath = new ParamHelper().getImgFilePath() + "/" ;
		File[] files = new File(imgRootPath).listFiles();
		for (File file : files) {
			try{
				String fileName = file.getName();
				Date fileDate = sdf.parse(fileName);
				calendar.setTime(fileDate); 
				//ɾ������ǰ
				int date = calendar.get(Calendar.DAY_OF_YEAR)+2;
				if(date <= today){
					deleteFile(file);
					System.out.println("ɾ����"+file.getName());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 *  ����·��ɾ��ָ����Ŀ¼���ļ������۴������ 
	 *@param sPath  Ҫɾ����Ŀ¼���ļ� 
	 *@return ɾ���ɹ����� true�����򷵻� false�� 
	 */  
	private void deleteFile(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					this.deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		} else {
			System.out.println("��ɾ�����ļ������ڣ�" + '\n');
		}
	}
	
}
