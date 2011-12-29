package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Graph extends GDActivity {
//http://android-coding.blogspot.com/2011/10/embeded-google-chart-in-webview.html
	  
	 WebView embeddedWebView;
	 ServerConnection sc;	
	 String Web = "http://chart.apis.google.com/chart?chxt=x,y&chs=300x300&cht=lc&chco=FF0000&chls=2,4,0&chm=o,FF0000,0,-2,6&chf=c,s,D1D0D0|bg,s,D1D0D0";
	 String embeddedWeb;
	 String session;
	 String sensor;
	 String valorsData;
	 String valors;
	 String TipusGrafic="0";
	 String[] NomTipusGrafic = new String[]{"Sensor strain (V)","Excitation power (V)","Counter (cnts)"};
	 
	 
	 private TextView txtTitol;
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       Bundle bundle = getIntent().getExtras();
		   session = bundle.getString("session");
	       sensor = bundle.getString("sensor");	       
	       setActionBarContentView(R.layout.graph);
	       Spinner cmbOpcions = (Spinner) findViewById(R.id.CmbOpcions);
	       ArrayAdapter<String> adaptador =
	               new ArrayAdapter<String>(this,
	                   android.R.layout.simple_spinner_item, NomTipusGrafic);
	           
	           adaptador.setDropDownViewResource(
	                   android.R.layout.simple_spinner_dropdown_item);
	            
	           cmbOpcions.setAdapter(adaptador);

	           cmbOpcions.setOnItemSelectedListener(
	           	new AdapterView.OnItemSelectedListener() {
	                   public void onItemSelected(AdapterView<?> parent,
	                       android.view.View v, int position, long id) {
	                	       Grafica(session, sensor,  position, NomTipusGrafic[position]);
	                   }
	            
	                   public void onNothingSelected(AdapterView<?> parent) {
	                	   Grafica(session, sensor, 0, "Sensor strain");
	                   }
	           });       
	       
	   }
	   public void Grafica(String session, String sensor, int TipusGrafic, String Titol){
		// Server Connection and convert response to string 
	        sc = new ServerConnection(this);
			String params = getResources().getString(R.string.valorsGrafic) + "?session=" + session +"&sensor="+sensor+"&TipusGrafic="+TipusGrafic;
			String result = sc.getDataConnection(params);
			String embeddedWeb;
		   try{
	           JSONArray jArray = new JSONArray(result);
	           JSONObject json_data = jArray.getJSONObject(0);
	           String Valors_JSON=json_data.getString("ValorsGrafica");           
	           JSONArray ValorsArray = new JSONArray(Valors_JSON);
	           String tmp="";
	           for(int i = 0; i < ValorsArray.length(); i++){
	        	   JSONObject json_Valors = ValorsArray.getJSONObject(i);
	        	   String[] tokens = json_Valors.getString("date").split("-");
	        	   tmp= valorsData;
	        	   if(i==0){
	        		   valorsData="|" + tokens[2];
	        	   }else{
	        		   valorsData=tmp + "|"+ tokens[2];
	        	   }
	        	   tmp=valors;	
	        	   if(i==0){
	        		   valors=json_Valors.getString("value");
	        	   }else{
	        		   valors=tmp + "," + json_Valors.getString("value");
	        	   }     	   
	        	   
	           }
	           
	           /*   
	            * 0:Sensor strain (V)		//min='1.630'; Max='1.640'; escala 0.01 chxr
	            * 1:Excitation power (V)	//min='3.400'; Max='3.200';	escala 0.01 chxr
	            * 2:counter (cnts)			//min='-5.000'; Max='20.000';
			   */
	           
	           switch(TipusGrafic){
		           case 0: 
		        	   embeddedWeb=Web + "&chxl=0:" + valorsData + "&chd=t:" + valors + "&chxr=1,1.62,1.65,0.01&chds=1.620,1.650,0,10";
		        	   break; 
		           case 1: 
		        	   embeddedWeb=Web + "&chxl=0:" + valorsData + "&chd=t:" + valors + "&chxr=1,3.2,3.4,0.01&chds=3.2,3.4,0,10";
		        	   break;
		           case 2: 
		        	   embeddedWeb=Web + "&chxl=0:" + valorsData + "&chd=t:" + valors + "&chxr=1,-5,20,1&chds=-5,20,0,10";
		        	   break;
		           default:
		        	 embeddedWeb=Web + "&chxl=0:" + valorsData + "&chd=t:" + valors + "&chxr=1,1.62,1.65,0.01&chds=1.620,1.650,0,10";
		        	   break;
	           }
		       
		       txtTitol = (TextView)findViewById(R.id.Titol);
	           txtTitol.setText(Titol);
		       embeddedWebView = (WebView)findViewById(R.id.Grafica);
		       embeddedWebView.loadUrl(embeddedWeb);
		       Log.w("Error al carregar la informació", embeddedWeb.toString());
		   }catch(JSONException ex){
              Log.w("Error al carregar la informació", ex.toString());
              Toast.makeText(getApplicationContext(), "Error al carregar la informació", Toast.LENGTH_SHORT).show();
		   }
	   }
	   
}

