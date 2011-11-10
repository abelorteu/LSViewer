package com.uoc.lsviewer;

import android.os.Bundle;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class SensorInfo extends GDActivity {
	
	private TextView tvSensorInfo;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setActionBarContentView(R.layout.sensorinfo);
	        
	        tvSensorInfo = (TextView)findViewById(R.id.tvSensorInfo);
	        
	        Bundle bundle = getIntent().getExtras();
			
	        tvSensorInfo.setText("Sensor Info is " + bundle.getInt("index"));
	}

}
