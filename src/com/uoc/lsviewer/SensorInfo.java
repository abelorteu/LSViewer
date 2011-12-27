package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
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
	
	String session;
	String sensor;
	String activity;
	ServerConnection sc;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		
	        super.onCreate(savedInstanceState);
	        setActionBarContentView(R.layout.sensorinfo);
	        
	        Bundle bundle = getIntent().getExtras();
			session = bundle.getString("session");
			sensor = bundle.getString("sensor");
			activity = bundle.getString("activity");
			
			if(activity == "ImageSensors"){
				
			} else {
				
			}
					
			sc = new ServerConnection(this);                
            String params = getResources().getString(R.string.sensorInfo) + "?session=" + session + "&sensor=" + sensor;
            String result = sc.getDataConnection(params);
            try{
	            JSONArray jArray = new JSONArray(result);
	            JSONObject json_data = jArray.getJSONObject(0);
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
            }catch(JSONException ex){
	               Log.w("Error al carregar la informació", ex.toString());
	               Toast.makeText(getApplicationContext(), "Error al carregar la informació", Toast.LENGTH_SHORT).show();
	         }

	}

}
