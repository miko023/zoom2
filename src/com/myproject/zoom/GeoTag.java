package com.myproject.zoom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.media.ExifInterface;

public class GeoTag {

	private double latitude;
	private double longitude;
	private double latitude_ref;
	private double longitude_ref;
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude_ref() {
		return latitude_ref;
	}

	public double getLongitude_ref() {
		return longitude_ref;
	}

	public int ConvertCoordinate1 (double Coordinate){
		int num1 = (int)Math.floor(Coordinate);	
		
		return num1;
	}
	
	public int ConvertCoordinate2(double Coordinate){
		int num1 = (int)Math.floor(Coordinate);
		int num2 = ((int)Math.floor(Coordinate - num1) * 60);
		
		return num2; 
	}
	
	public double ConvertCoordinate3(double Coordinate){
		int num1 = (int)Math.floor(Coordinate);
		int num2 = ((int)Math.floor(Coordinate - num1) * 60);
		double num3 = (Coordinate -((double)num1+((double)num2/60))) * 3600000;
		
		return num3; 
	}
	
	public String SetNorthSouth(double latitude){
		String NS; 
		if(latitude > 0){
			NS = "N";
		}
		else {
			NS = "S";}
		return NS; 
	}
	public String SetEastWest(double longitude){
		String WE; 
		if(longitude > 0){
			WE = "E";
		}
		else {
			WE = "W";}
		return WE; 
	}
	//filePath should be the absolute path, canonical path?
    public static void SetGeoTag(String filePath, double latitude, double longitude){
    	
    	try {
    		//http://stackoverflow.com/questions/8807799/android-find-the-orientation-of-photo-was-took-by-camera
    		//http://stackoverflow.com/questions/13587070/geotagging-photos-after-using-camera-intent
    		//try putting set attribute exif after
    			
    			ExifInterface exif = new ExifInterface(filePath);
			 	int num1Lat = (int)Math.floor(latitude);
		        int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
		        double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

		        int num1Lon = (int)Math.floor(longitude);
		        int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
		        double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

		        String latitudeStr =  num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000";
		        String longitudeStr = num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000";
		        
		        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);
		        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,longitudeStr );
		        
		        if (latitude > 0) {
		            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N"); 
		        } else {
		            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
		        }

		        if (longitude > 0) {
		            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");    
		        } else {
		        	exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
		        }
		      
		        exif.saveAttributes();
		        
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
		        System.out.println("files written");
		        
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    public void ReadGeoTag(File arr){
    	ExifInterface exif = null;
    	Float Latitude;
    	Float Longitude;
		ArrayList<String> item = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
    	try {
            //http://android-er.blogspot.ca/2010/01/convert-exif-gps-info-to-degree-format.html
    		//https://code.google.com/p/metadata-extractor/
    		//http://stackoverflow.com/questions/13587070/geotagging-photos-after-using-camera-intent
    			exif = new ExifInterface(arr.getAbsolutePath().toString());
    			
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
		        System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
    			
    			String attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
    			String attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
    			String attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
    			String attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
    			
    				
    				if(attrLATITUDE_REF.equals("N")){
    					Latitude = convertToDegree(attrLATITUDE);
    				}
    				else{
    					Latitude = 0 - convertToDegree(attrLATITUDE);
    					
    				}
    				
    				if(attrLONGITUDE_REF.equals("E")){
    					Longitude = convertToDegree(attrLONGITUDE);
    				}
    				else{
    					Longitude = 0 - convertToDegree(attrLONGITUDE);
    				}
        			System.out.println("Latitude is: " + Latitude);
        			System.out.println("Longitude is: " + Longitude);
        			latitude = Latitude;
        			longitude = Longitude; 
        			
        			item.add(arr.getName());
        			System.out.println("the path is: " + arr.getPath());
        			path.add(arr.getPath());	

    		
    			   		
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
	
	private Float convertToDegree(String stringDMS){
		 Float result = null;
		 String[] DMS = stringDMS.split(",", 3);

		 String[] stringD = DMS[0].split("/", 2);
		 Double D0 = new Double(stringD[0]);
		 Double D1 = new Double(stringD[1]);
		 Double FloatD = D0/D1;

		 String[] stringM = DMS[1].split("/", 2);
		 Double M0 = new Double(stringM[0]);
		 Double M1 = new Double(stringM[1]);
		 Double FloatM = M0/M1;

		 String[] stringS = DMS[2].split("/", 2);
		 Double S0 = new Double(stringS[0]);
		 Double S1 = new Double(stringS[1]);
		 Double FloatS = S0/S1;

		 result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

		 return result;
		};

	
}
