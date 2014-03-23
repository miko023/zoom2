package com.myproject.zoom;

import java.io.File;
import java.io.IOException;

import com.myproject.zoom.GeoTag;
import com.myproject.zoom.PagerContainer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public class DisplayMap extends Activity {
	
	
	private String root ="/DCIM/Zoom";
	private File photoFile;
	
	private GoogleMap googleMap;
	private final LatLng LOCATION_VANCOUVER = new LatLng(49.2505,-123);
	
	private LatLng newMarker; 
	
	private PagerContainer mContainer; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_map);
		
		try{
			initializeMap();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		photoFile = new File(Environment.getExternalStorageDirectory() + root);
		if (photoFile.exists() == false){
			photoFile.mkdirs();
		}
		
		File[] filearr = photoFile.listFiles();
		GeoTag geotag = new GeoTag();
		
		for (int i = 0; i<filearr.length;i++){
		System.out.println("The files in" + root + "folder are: " + filearr[i]);
		try {
			System.out.println("Filepath is: " + filearr[i].getCanonicalPath().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		geotag.ReadGeoTag(filearr[i]);
		}
		
		newMarker = new LatLng(geotag.latitude,geotag.longitude);
		
		// create marker
		//MarkerOptions marker = new MarkerOptions().position(new LatLng(LOCATION_VANCOUVER.latitude,LOCATION_VANCOUVER.longitude)).title("Vancouver");
		MarkerOptions marker1 = new MarkerOptions().position(new LatLng(newMarker.latitude,newMarker.longitude)).title("Vancouver");
		//MarkerOptions marker2 = new MarkerOptions().position(new LatLng(newMarker.latitude,newMarker.longitude)).title("Vancouver");
		
		// adding marker
		//googleMap.addMarker(marker);
		googleMap.addMarker(marker1);
		//googleMap.addMarker(marker2);
		
		//moving camera to marker
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(newMarker.latitude,newMarker.longitude)).zoom(5).build();
 
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		//set map type
//		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		
		//show current location
		googleMap.setMyLocationEnabled(false); 
		
		mContainer = (PagerContainer) findViewById(R.id.pager_container);
		
		
		//http://examples.javacodegeeks.com/android/core/ui/horizontalscrollview/android-horizontalscrollview-example/ 
		//http://stackoverflow.com/questions/14105126/horizontal-swipe-scroll-view-with-edit-text
		//http://ttlnews.blogspot.ca/2011/04/horizontally-sliding-grid-of-images-in.html
		//http://stackoverflow.com/questions/15896373/horizontal-carousel-or-scrollview-with-text-over-image
		
		ViewPager pager = mContainer.getViewPager();
		PagerAdapter adapter = new MyPagerAdapter();
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(adapter.getCount());
		pager.setPageMargin(15);
		pager.setClipChildren(false);
		
	}

    private class MyPagerAdapter extends PagerAdapter {
    	 
    	private Context mContext;
    	private Activity activity;
    	private int[] drawableIDs;
    	
//    	public MyPagerAdapter(Activity activity, int[] drawableIDs){
//    		this.activity = activity; 
//    		this.drawableIDs = drawableIDs; 
//    	}
    	
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view = new TextView(DisplayMap.this);
           view.setText("Item "+position);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50));
        	
            container.addView(view);
            return view;
        }
 
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
 
        @Override
        public int getCount() {
            return 8;
        }
 
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
        
        @Override
        public float getPageWidth(final int position){
        	return 0.4f;
        }
    }
    
	private void initializeMap(){
		if(googleMap == null)
		{
			googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.Map)).getMap();
			if(googleMap == null) {
				Toast.makeText(getApplicationContext(), "Map not created", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override 
	protected void onResume(){
		super.onResume();
		initializeMap();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_map, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_search:
			return true;
		case R.id.action_refresh:
			return true; 
		case R.id.action_help:
			return true; 
		case R.id.action_check_update:
			return true; 
		}
		return super.onOptionsItemSelected(item);
	}

}
