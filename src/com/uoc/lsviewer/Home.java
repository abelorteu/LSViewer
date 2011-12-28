package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Home extends GDActivity{

	private Button btnList;
	private Button btnMapa;
	private Button btnQRCode;
	private Button btnHelp;	
	private final int LOCATE = 0;
	private final int REFRESH = 1;	
	static final int DIALOG_EXIT = 0;	
	private String session;
	private static String REMEMBERED = "remembered";
	private static String strUSER = "User";
	private static String strPASS = "Password";
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.home);
		initActionBar();
		
		btnList = (Button)findViewById(R.id.btnList);
		btnMapa = (Button)findViewById(R.id.btnMap);
		btnQRCode = (Button)findViewById(R.id.btnQRCode);
		btnHelp = (Button)findViewById(R.id.btnHelp);
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");		
		
		//Toast.makeText(Home.this, bundle.getString("session"), Toast.LENGTH_LONG).show();
		
		// Llistat de xarxes
		btnList.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, NetList.class);
				// Id session
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				intent.putExtras(bundle);				
				startActivity(intent);				
			}
		});
		
		// Llistat de xarxes al mapa
		btnMapa.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, MapsList.class);				
				// Id session
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				intent.putExtras(bundle);				
				startActivity(intent);				
			}
		});
	
		// Llegir QRCode
		btnQRCode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				IntentIntegrator.initiateScan(Home.this);
			}
		});	
		
		// Ajuda
	    btnHelp.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, Help.class);				
				startActivity(intent);					
			}
		});		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mhome, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
	    switch (item.getItemId()) {
	        case R.id.m_opcions:
	        	
	            return true;
	        case R.id.m_sortir:
	        	
	        	builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.mDialogSortir)
	        	       .setCancelable(false)
	        	       .setPositiveButton(R.string.mDialogSi, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   Home.this.finish();
	        	           }
	        	       })
	        	       .setNegativeButton(R.string.mDialogNo, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       });
	        	AlertDialog alert1 = builder.create();	        	
	        	alert1.show();
	        		        	
	            return true;
	        case R.id.m_tancar_sessio:
	        	builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.mDialogLogout)
	        	       .setCancelable(false)
	        	       .setPositiveButton(R.string.mDialogSi, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   SharedPreferences sp = getApplicationContext().getSharedPreferences("settings", 0);
	        	        	   //Obtenemos el editor de las preferencias.
	        	        	   SharedPreferences.Editor editor = sp.edit();
	        	        	   editor.putBoolean(REMEMBERED, false);
	        	        	   editor.putString(strUSER, "");
	        	        	   editor.putString(strPASS, "");
	        	        	   editor.commit();
	        	        	   Home.this.finish();
	        	           }
	        	       })
	        	       .setNegativeButton(R.string.mDialogNo, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       });
	        	AlertDialog alert2 = builder.create();	        	
	        	alert2.show();        	
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
					String sensor = scanResult.getContents();
					//tvResult.setText("The result is: " + upc);
					
					Intent intent = new Intent(Home.this, SensorInfo.class);
					Bundle bundle = new Bundle();
					bundle.putString("session", session);
					bundle.putString("sensor", sensor);
					bundle.putString("activity", "QRCode");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
			break;
		}
		}
	}
}
