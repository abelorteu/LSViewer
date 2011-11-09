package com.uoc.lsviewer;

import android.content.Intent;
import greendroid.app.GDApplication;

public class GDActionBarApplication extends GDApplication {
	
	@Override
	public Class<?> getHomeActivityClass() {
		return null;
	}
	 
	@Override
	public Intent getMainApplicationIntent() {
	    return new Intent(Intent.ACTION_DEFAULT);
	}
	
}
