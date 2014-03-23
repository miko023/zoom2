package com.myproject.zoom;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
	
	
	
	public PhotoFile(){
		uri = getOutputMediaFileUri();
		timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
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
