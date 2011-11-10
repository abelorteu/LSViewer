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
	private Button btnMapa;
	
	
	private final int LOCATE = 0;
	private final int REFRESH = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.home);
		initActionBar();
		
		tvUser = (TextView)findViewById(R.id.tvUser);
		btnMapa = (Button)findViewById(R.id.btnMapa);
		
		Bundle bundle = getIntent().getExtras();
		
		tvUser.setText("Hello " + bundle.getString("user"));
		
		btnMapa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, MapsList.class);
				
					
				startActivity(intent);
				
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
}
