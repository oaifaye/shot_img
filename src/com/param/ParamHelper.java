package com.param;

public class ParamHelper {

	private static String IMG_FILE_PATH = "";
	private static long SHOT_TIME_OUT = 15000;
	private static String SHOT_SCRIPT_PATH = "";
	
	public void setImgFilePath(String path){
		IMG_FILE_PATH = path;
	}
	
	public String getImgFilePath(){
		return IMG_FILE_PATH;
	}
	
	public void setShotTimeOut(long l){
		SHOT_TIME_OUT = l;
	}
	
	public long getShotTimeOut(){
		return SHOT_TIME_OUT;
	}
	
	public void setShotScriptPath(String path){
		SHOT_SCRIPT_PATH = path;
	}
	
	public String getShotScriptPath(){
		return SHOT_SCRIPT_PATH;
	}
}
