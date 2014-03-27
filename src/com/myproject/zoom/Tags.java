package com.myproject.zoom;

public class Tags {

	private String tag;
	

	public Tags(String tag){
		this.tag = tag;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public Boolean addTag(String description){
		Boolean done = null; 
		done = description.contains("@");
		
		return done; 
		
	}
	
}
