package com.myproject.zoom;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.provider.MediaStore;

public class TestCamera extends Activity implements LocationListener {
	
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri;
	private PhotoFile pf;
	private LocationManager locationManager; 
	private String provider; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		//get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria(); 
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		
		//initialize the location fields
		if (location != null){
			System.out.println("Provider" + provider + "has been selected");
			onLocationChanged(location);
		}
		else 
		{
			System.out.println("Location not available");
			System.out.println("Location not available");
			System.out.println("Location not available");
			System.out.println("Location not available");
		}
		setContentView(R.layout.activity_test_camera);
		pf = new PhotoFile(); 
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = pf.getOutputMediaFileUri();
		System.out.println("OUR FILEURIE");
		System.out.println(fileUri.toString());
		
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_test_camera,
					container, false);
			return rootView;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		GeoTag.SetGeoTag(pf.getOutputMediaFile().getAbsoluteFile().toString(),location.getLatitude(),location.getLongitude());	
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Location location = locationManager.getLastKnownLocation(provider);
//		locationManager.requestLocationUpdates(provider, 400, 1, this);
//	
//		// TODO Auto-generated method stub
//		if (resultCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
//			if (resultCode == RESULT_OK){
//				GeoTag.SetGeoTag(pf.getOutputMediaFile().getAbsoluteFile().toString(),location.getLatitude(),location.getLongitude());	
//			}
//		}
//		
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = (double) (location.getLatitude());
		double lng = (double) (location.getLongitude());
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


}
