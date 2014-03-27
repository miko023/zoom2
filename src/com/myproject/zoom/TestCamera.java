package com.myproject.zoom;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore;

public class TestCamera extends Activity implements LocationListener {
	
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri;
	private PhotoFile pf;
	private LocationManager locationManager; 
	private String provider; 
	private ImageView imgPreview;
	private EditText descriptionText;
//	private int rotate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_camera);
		imgPreview = (ImageView) findViewById(R.id.imagePreview);
		descriptionText = (EditText) findViewById(R.id.editDescription);
		
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
		ExifInterface exif;
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		GeoTag.SetGeoTag(pf.getOutputMediaFile().getAbsoluteFile().toString(),location.getLatitude(),location.getLongitude());
	
		
	}

	private void PreviewCapturedPhoto(){
		
		int rotate = 0;
		ExifInterface exif;
//		
		try {
			exif = new ExifInterface (pf.getOutputMediaFile().getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			System.out.println("Orientation Check!!!!!!!");
			System.out.println("Orientation is:" + orientation);
			switch(orientation){
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate-= 90;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 0;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate= 90;
					
			}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		try {
			
		System.out.println("bitmap step!!!!");
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                options);
        Bitmap mutate = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(mutate);
        canvas.rotate(rotate);
        
        System.out.println("PREVIEWCAPTUREDPHOTO FILE URI" + fileUri);
        imgPreview.setImageBitmap(mutate);
//        imgPreview.setImageURI(Uri.parse(fileUri.getPath()));
        
        }
		catch (NullPointerException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	
		imgPreview = (ImageView) findViewById(R.id.imagePreview);
		PreviewCapturedPhoto();

		
		
//		 TODO Auto-generated method stub
		if (resultCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
//			PreviewCapturedPhoto();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void savePhoto(View view){
		System.out.println("writing description");


		String description = descriptionText.getText().toString();
		System.out.println(description);
		pf.setDescription(description);
		
		pf.searchForTags(description);
		
		Intent intent = new Intent(this,DisplayGallery.class);
		startActivity(intent);
//		System.out.println("description written");
	}

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
