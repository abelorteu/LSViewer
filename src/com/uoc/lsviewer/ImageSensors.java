package com.uoc.lsviewer;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import greendroid.app.GDActivity;

public class ImageSensors extends GDActivity{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.imagesensors);
		
		Bundle bundle = getIntent().getExtras();
        String session = bundle.getString("session");
        Integer idXarxa = bundle.getInt("index");
        String url = bundle.getString("url");             
        
        ImageView imgView =(ImageView)findViewById(R.id.imgView);
        
        //http://www.androidpeople.com/android-load-image-url-example
        
        Drawable drawable = LoadImageFromWebOperations(url);
        imgView.setBackgroundDrawable(drawable);
        
        //Canvas
        //http://www.droidnova.com/playing-with-graphics-in-android-part-i,147.html

	}

    private Drawable LoadImageFromWebOperations(String url) {
    	try {
    		InputStream is = (InputStream) new URL(url).getContent();
		    Drawable d = Drawable.createFromStream(is, "src name");
		    return d;
    	}catch (Exception e) {
	      	System.out.println("Exc="+e);
	       	return null;
	    }
    }
}
