package com.myproject.zoom;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;

public class OpenCamera extends Activity implements LocationListener {

	
	//keep a global count to name photos
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    
	private TextView latitude;
	private TextView longitude; 
	private TextView NS;
	private TextView WE;
	
	private LocationManager locationManager; 
	private String provider; 
	
	Date date = new Date();
	String realtime = date.toString();
	String filename;
	String filename1;
	
	
	private File photoFile; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		filename = realtime + ".jpg";
		filename1 = "Test.jpeg";
		photoFile = new File(Environment.getExternalStorageDirectory()+"/DCIM/Zoom",filename);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_open_camera);
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/DCIM/Zoom",filename)));
		startActivityForResult(cameraIntent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		
		try {
			System.out.println(photoFile.getCanonicalPath().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		latitude = (TextView) findViewById(R.id.latitudeview);
		longitude = (TextView) findViewById(R.id.longitudeview);
		NS = (TextView) findViewById(R.id.latituderef);
		WE = (TextView) findViewById(R.id.longituderef);		
		
		//get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria(); 
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		//initialize the location fields
		if (location != null){
			System.out.println("Provider" + provider + "has been selected");
			onLocationChanged(location);
		}
		else 
		{
			latitude.setText("Location not available");
			longitude.setText("Location not available");
			NS.setText("Location not available");
			WE.setText("Location not available");
		}
		
	
//		SetGeoTag(photoFile,location.getLatitude(),location.getLongitude());
		
	}

	//filePath should be the absolute path, canonical path?
    public void SetGeoTag(String filePath, double latitude, double longitude){
    	
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
		            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");    
		        } else {
		        	exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	//would probably also work with onactivityresult
	@Override 
	protected void onResume(){
		super.onResume();
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		SetGeoTag(photoFile.getAbsolutePath(),location.getLatitude(),location.getLongitude());
		
	}
	
	@Override 
	protected void onPause(){
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		double lat = (double) (location.getLatitude());
		double lng = (double) (location.getLongitude());
		
		latitude.setText(String.valueOf(lat));
		longitude.setText(String.valueOf(lng));
		
		if (lat > 0){
			NS.setText("N");
		}
		else 
		{
			NS.setText("S");
		}
		if (lng > 0){
			WE.setText("E");
		}
		else 
		{
			WE.setText("W");
		}		
	}

	
	
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
