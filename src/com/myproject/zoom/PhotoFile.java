package com.myproject.zoom;

import java.io.File;
import java.net.URI;

import android.media.ExifInterface;

public class PhotoFile extends File {

	public PhotoFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}
	private File file;
	private ExifInterface exif;
	private double Latitude; 
	private double Longitude; 
	private String Latitude_ref; 
	private String Longitude_ref; 
	
	
}
