package com.uoc.lsviewer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class QRCode extends GDActivity {
	
	private TextView tvResult;
	private String session;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.qrcode);
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");
		
		//tvResult = (TextView)findViewById(R.id.tvResult);
	
		IntentIntegrator.initiateScan(QRCode.this);
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String sensor = scanResult.getContents();
					//tvResult.setText("The result is: " + upc);
					
					Intent intent = new Intent(QRCode.this, SensorInfo.class);
					Bundle bundle = new Bundle();
					bundle.putString("session", session);
					bundle.putString("sensor", sensor);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
			break;
		}
		}
	}
}
