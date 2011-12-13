package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.home);
		initActionBar();
		
		btnList = (Button)findViewById(R.id.btnList);
		btnMapa = (Button)findViewById(R.id.btnMap);
		btnQRCode = (Button)findViewById(R.id.btnQRCode);
		btnHelp = (Button)findViewById(R.id.btnHelp);
		
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
				Intent intent = new Intent(Home.this, QRCode.class);
				
				startActivity(intent);					
			}
		});	
		
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
	    switch (item.getItemId()) {
	        case R.id.m_opcions:
	        	
	            //lblMensaje.setText("Opcion 1 pulsada!");
	            return true;
	        case R.id.m_sortir:
	            //lblMensaje.setText("Opcion 2 pulsada!");;
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.mDialogSortir)
	        	       .setCancelable(false)
	        	       .setPositiveButton(R.string.mDialogSortirSi, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   Home.this.finish();
	        	           }
	        	       })
	        	       .setNegativeButton(R.string.mDialogSortirNo, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       });
	        	AlertDialog alert = builder.create();	        	
	        	alert.show();
	        		        	
	            return true;
	        case R.id.m_tancar_sessio:
	            //lblMensaje.setText("Opcion 3 pulsada!");;
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
}
