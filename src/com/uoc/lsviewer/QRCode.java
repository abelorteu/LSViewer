package com.uoc.lsviewer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class QRCode extends GDActivity {
	
	private TextView tvResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.qrcode);
		
		tvResult = (TextView)findViewById(R.id.tvResult);
	
		IntentIntegrator.initiateScan(QRCode.this);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String upc = scanResult.getContents();
					tvResult.setText("The result is: " + upc);
				}
			}
			break;
		}
		}
	}
}
