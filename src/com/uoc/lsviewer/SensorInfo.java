package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SensorInfo extends GDActivity {
	
	private TextView txtNom;
	private TextView txtPoblacio;
	private TextView txtDescripcio;
	private TextView txtSerialNumber;
	private TextView txtSensor;
	private TextView txtSensorName;
	private TextView txtMeasure;
	private TextView txtMaxLoad;
	private TextView txtSensivity;
	private TextView txtOffSet;
	private TextView txtAlarmAt;
	private TextView txtLastTare;
	private Button btnGraph;	
	private String session;
	private String sensor;
	private String activity;
	private ServerConnection sc;
	private String idXarxa;
	private String nomXarxa;
	private String idIMG;
	private String url;
	
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		
	        super.onCreate(savedInstanceState);
	        setActionBarContentView(R.layout.sensorinfo);
	        initActionBar();
	        
	        Bundle bundle = getIntent().getExtras();
			session = bundle.getString("session");
			sensor = bundle.getString("sensor");
			activity = bundle.getString("activity");
			idXarxa = bundle.getString("idXarxa");
		    nomXarxa = bundle.getString("nomXarxa");		    
		    idIMG = bundle.getString("idIMG");			
			url = bundle.getString("url");
			
			// Server connection
			sc = new ServerConnection(this);
			String params = getResources().getString(R.string.sensorInfo) + "?session=" + session;
			if(activity.compareTo("QRCode") == 0){
				params = params + "&serialNumber=" + sensor;				
			} else {				
				params = params + "&sensor=" + sensor;
			}								                
            
            String result = sc.getDataConnection(params);
            try{
	            JSONArray jArray = new JSONArray(result);
	            JSONObject json_data = jArray.getJSONObject(0);
	            
	            if(json_data.getInt("id") == 0) {
	            	
	            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            	String msg = getResources().getString(R.string.mDialogErrorQRCode) + " " + sensor; 
	    			builder.setMessage(msg)	    			
	    			.setCancelable(false)
	    			.setPositiveButton(R.string.mDialogOK, new DialogInterface.OnClickListener() {
	    				public void onClick(DialogInterface dialog, int id) {
	    					Intent intent = new Intent(SensorInfo.this, Home.class);							
	    					Bundle bundle = new Bundle();
	    					bundle.putString("session", session);
	    					intent.putExtras(bundle);
	    					startActivity(intent);
	    					finish();
	    				}
	    			});
	    			AlertDialog alert1 = builder.create();	        	
	    			alert1.show();
	            	
	            } else {
		            txtNom = (TextView)findViewById(R.id.txtNom);
		            txtNom.setText(json_data.getString("Nom"));
		            txtPoblacio = (TextView)findViewById(R.id.txtPoblacio);
		            txtPoblacio.setText(json_data.getString("Poblacio"));
		            txtDescripcio = (TextView)findViewById(R.id.txtDescripcio);
		            txtDescripcio.setText(json_data.getString("Descripcio"));
		            txtSerialNumber = (TextView)findViewById(R.id.txtSerialNumber);
		            txtSerialNumber.setText(json_data.getString("serialNumber"));
		            txtSensor = (TextView)findViewById(R.id.txtSensor);
		            txtSensor.setText(json_data.getString("sensor"));
		            txtSensorName = (TextView)findViewById(R.id.txtSensorName);
		            txtSensorName.setText(json_data.getString("sensorName"));
		            txtMeasure = (TextView)findViewById(R.id.txtMeasure);
		            txtMeasure.setText(json_data.getString("measure") + " " + json_data.getString("measureUnit"));
		            txtMaxLoad = (TextView)findViewById(R.id.txtMaxLoad);
		            txtMaxLoad.setText(json_data.getString("MaxLoad") + " " + json_data.getString("MaxLoadUnit"));
		            txtSensivity = (TextView)findViewById(R.id.txtSensivity);
		            txtSensivity.setText(json_data.getString("Sensivity") + " " + json_data.getString("SensivityUnit"));
		            txtOffSet = (TextView)findViewById(R.id.txtOffSet);
		            txtOffSet.setText(json_data.getString("offset") + " " + json_data.getString("offsetUnit"));
		            txtAlarmAt = (TextView)findViewById(R.id.txtAlarmAt);
		            txtAlarmAt.setText(json_data.getString("AlarmAt") + " " + json_data.getString("AlarmAtUnit"));
		            txtLastTare = (TextView)findViewById(R.id.txtLastTare);
		            txtLastTare.setText(json_data.getString("LastTare"));
		            
		            btnGraph= (Button)findViewById(R.id.btnGraph);
		            btnGraph.setOnClickListener(new OnClickListener() {	
		            	@Override
		    			public void onClick(View v) {
		    				Intent intent = new Intent(SensorInfo.this, Graph.class);
		    				// Id session
		    				Bundle bundle = new Bundle();
		    				bundle.putString("session", session);
		    				bundle.putString("sensor", sensor);
		    				intent.putExtras(bundle);				
		    				startActivity(intent);				
		    			}
		            });
	            }
            }catch(JSONException ex){
	               Log.w("Error al carregar la informació", ex.toString());
	               Toast.makeText(getApplicationContext(), "Error al carregar la informació", Toast.LENGTH_SHORT).show();
	         }

	}
	
	private void initActionBar() {

		addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this, R.drawable.ic_menu_home)), R.id.action_bar_view_home);
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
			
			case R.id.action_bar_view_home:
				Intent intent = new Intent(SensorInfo.this, Home.class);							
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
				
			break;
		
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(this.getClass().getName(), "back button pressed");

	    	if(activity.compareTo("QRCode") == 0) {
	    		Intent intent = new Intent(SensorInfo.this, Home.class);							
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
	    		
	    	} else if(activity.compareTo("ImageSensors") == 0){
	    		
	    		Intent intent = new Intent(SensorInfo.this, ImageSensors.class);
	    		Bundle bundle = new Bundle();
	    		bundle.putString("session", session);
	    		bundle.putString("idXarxa", idXarxa);
	    		bundle.putString("idIMG", idIMG);
	    		bundle.putString("url", url);
	    		intent.putExtras(bundle);
	    		startActivity(intent);
	    		finish();
	    		
	    	} else {
	    		Intent intent = new Intent(SensorInfo.this, SensorList.class);	
	          	Bundle bundle = new Bundle();
	          	bundle.putString("session", session);
				bundle.putString("idXarxa", idXarxa);
				bundle.putString("nomXarxa", nomXarxa);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
	    	}
	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}