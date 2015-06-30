package com.hepler;

import java.util.LinkedHashSet;
import java.util.Set;

public class SeqHelper {

	private static Set<String> SEQ = new LinkedHashSet<String>(100);
	private static Set<String> SEQ_FINISHED = new LinkedHashSet<String>(100);
	
	public void add(String url){
		SEQ.add(url);
		System.out.println("size:"+SEQ.size());
	}
	
	public String getTop(){
		if(SEQ.size() > 0){
			String url = SEQ.iterator().next();
			SEQ.remove(url);
			return url;
		}
		return null;
	}
	
	public void addFinished(String url){
		SEQ_FINISHED.add(url);
	}
	
	public boolean hasFinished(String key){
		for (String item : SEQ_FINISHED) {
			if (key.indexOf(item) >= 0 ){
				return true;
			}
		}
		return false;
	}
	
	public void removeFinished(String url){
		SEQ_FINISHED.remove(url);
	}
}
