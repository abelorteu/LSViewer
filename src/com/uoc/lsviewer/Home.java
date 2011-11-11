package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends GDActivity{

	private TextView tvUser;
	private Button btnList;
	private Button btnMapa;
	private Button btnQRCode;
	
	private final int LOCATE = 0;
	private final int REFRESH = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.home);
		initActionBar();
		
		tvUser = (TextView)findViewById(R.id.tvUser);
		btnList = (Button)findViewById(R.id.btnList);
		btnMapa = (Button)findViewById(R.id.btnMap);
		btnQRCode = (Button)findViewById(R.id.btnQRCode);
		
		Bundle bundle = getIntent().getExtras();
		
		tvUser.setText("Hello " + bundle.getString("user"));
		
		btnList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, NetList.class);
				
				startActivity(intent);				
			}
		});
		
		btnMapa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, MapsList.class);
				
				startActivity(intent);				
			}
		});
		
		btnQRCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentIntegrator.initiateScan(Home.this);				
			}
		});		  
	}
	
	private void initActionBar() {
		
		addActionBarItem(Type.Locate, LOCATE);
		addActionBarItem(Type.Refresh, REFRESH);	

	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
			case LOCATE:
				Toast.makeText(getApplicationContext(),
				"Has pulsado el boton LOCATE",
				Toast.LENGTH_SHORT).show();
				break;
	
			case REFRESH:
				Toast.makeText(getApplicationContext(),	
				"Has pulsado el boton REFRESH",	
				Toast.LENGTH_SHORT).show();
				break;
			
	
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
	
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String upc = scanResult.getContents();
					//put whatever you want to do with the code here
					TextView tv = new TextView(this);
					tv.setText(upc);
					setContentView(tv);
				}
			}
			break;
		}
		}
	}
}
