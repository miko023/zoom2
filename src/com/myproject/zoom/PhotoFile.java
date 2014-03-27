package com.myproject.zoom;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

public class PhotoFile {

	private File file;
	private double Latitude; 
	private double Longitude; 
	private String Latitude_ref; 
	private String Longitude_ref; 
	private Uri uri; 
	private File mediafile;
	private String timestamp;
	private String description;
	
	private List<Tags> taglist;
	
	
	public PhotoFile(){
		uri = getOutputMediaFileUri();
		timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		taglist = new ArrayList<Tags>();
		description = "";
	}
	
	public void addTag(String tagName){
		Tags tag = new Tags(tagName);
		taglist.add(tag);
	}
	
	public void removeTag(String tagName){
		for (int i=0; i<taglist.size();i++){
			if(taglist.get(i).getTag().equals(tagName)){
				taglist.remove(i);
			}			
		}
	}
	
	public String getDescription() {
		
//		eg."alrgkmaelrkgmaeg #tag #hongkong"
		return description;
	}

	public void setDescription(String description) {
		
		/*
		 "aelrgkmaeg #tag#tag" 
		 "alrgkmaelrkgmaeg #tag #hongkong"
		"aerlgkmaelgaergmkl#tag aelgkmaerg"
  		"#tag#tag1 aaelkgaeg"
  
		 */
		
		
		this.description = description;
	}
	
	/**
	 * SEARCH FOR TAGS IN DESCRIPTION AND ADD INTO TAGLIST
	 */
	public void searchForTags (String description){
		
		
		StringTokenizer str = new StringTokenizer (description);
		String tag;
//		//loop space for tokens
//		while(str.hasMoreTokens()){
//			StringTokenizer str2 = new StringTokenizer(str.nextToken(), "#@");
//			String tmp[] = str2.toString().split("#@");
//			for(int i = 0; i < tmp.length; i++){
//				addTag(tmp[i]);
//			}
		String seperateSpace[] = description.split("\\s+");
		for (int i = 0; i<seperateSpace.length;i++){
			String afterSpace[] = seperateSpace[i].split("#@");
			for (int j = 0; j<afterSpace.length;j++){
				if(afterSpace[j].contains("#") || afterSpace[j].contains("@") ){
				tag = afterSpace[j].substring(1);
				addTag(tag);
				}
			}
		}
		printTags();
	}
	
	public void printTags(){
		for(Tags tag: taglist){
			System.out.println(tag.getTag());
		}
	}
	

	public Uri getOutputMediaFileUri(){
		return Uri.fromFile(getOutputMediaFile());	
	}
	
	public File getOutputMediaFile(){
//		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		
		mediafile = new File(Environment.getExternalStorageDirectory()+ "/DCIM/Zoom", timestamp +".jpg");
		return mediafile;
		
	} 	
	
	public File getFile() {
		return file;
	}



	public void setFile(File file) {
		this.file = file;
	}



	public double getLatitude() {
		return Latitude;
	}



	public void setLatitude(double latitude) {
		Latitude = latitude;
	}



	public double getLongitude() {
		return Longitude;
	}


	public String getLatitude_ref() {
		return Latitude_ref;
	}



	public void setLatitude_ref(String latitude_ref) {
		Latitude_ref = latitude_ref;
	}



	public String getLongitude_ref() {
		return Longitude_ref;
	}

	public void setLatLong(double latitude, double longitude){
		GeoTag.SetGeoTag(mediafile.getAbsolutePath(), latitude, longitude);
	}


	
	
}
