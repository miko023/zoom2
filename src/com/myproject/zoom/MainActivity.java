package com.myproject.zoom;

import com.myproject.zoom.GPS;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	//http://stackoverflow.com/questions/8846396/how-to-change-initial-screen
	
	TextView textLat; 
	TextView textLng; 
	GPS gps; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
//		Intent intent1 = new Intent(this,LoginActivity.class);
//		startActivity(intent1);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if( !enabled){
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		
	}

	class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location != null ){
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				
				textLat.setText(Double.toString(lat));
				textLat.setText(Double.toString(lng));
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	public void DisplayGallery(View view){
		Intent intent = new Intent (this,GridViewActivity.class);
		startActivity(intent);
	}	
	
	public void DisplayMap(View view){
		Intent intent = new Intent (this,DisplayMap.class);
		startActivity(intent);
	}	
	
	public void OpenCamera(View view){
		Intent intent = new Intent (this,OpenCamera.class);
		startActivity(intent);
	}	
	
	public void OpenSetting(View view){
		Intent intent = new Intent (this,Setting.class);
		startActivity(intent);
	}	
	
	public void OpenProfile(View view){
		Intent intent = new Intent(this,OpenProfile.class);
		startActivity(intent);
	}
	
	public void TestCamera(View view){
		Intent intent = new Intent(this,TestCamera.class);
		startActivity(intent);
		
	}

}
