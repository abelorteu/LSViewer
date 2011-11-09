package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends GDActivity{

	private TextView tvUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.home);
		
		tvUser = (TextView)findViewById(R.id.tvUser);
		
		Bundle bundle = getIntent().getExtras();
		
		tvUser.setText("Hello " + bundle.getString("user"));
		  
	}
}
