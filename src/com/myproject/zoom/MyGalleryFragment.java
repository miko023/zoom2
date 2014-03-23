package com.myproject.zoom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyGalleryFragment extends Fragment {

	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
	
		View rootView = inflater.inflate(R.layout.fragment_my_gallery,container , false);
		return rootView; 
		
	}
	
}
