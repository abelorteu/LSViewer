package com.uoc.lsviewer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends Activity{

	private TextView tvUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		tvUser = (TextView)findViewById(R.id.tvUser);
		
		Bundle bundle = getIntent().getExtras();
		
		tvUser.setText("Hello " + bundle.getString("user"));
		  
	}
}
