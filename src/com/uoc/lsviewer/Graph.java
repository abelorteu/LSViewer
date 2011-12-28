package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
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
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       Bundle bundle = getIntent().getExtras();
		   session = bundle.getString("session");
	       sensor = bundle.getString("sensor");	       
	       setActionBarContentView(R.layout.graph);
	       
	       // Server Connection and convert response to string 
	       sc = new ServerConnection(this);
			String params = getResources().getString(R.string.sensorInfo) + "?session=" + session +"&sensor="+sensor;
			String result = sc.getDataConnection(params);
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
	        		   valorsData="|" + tokens[1] + "-" + tokens[2];
	        	   }else{
	        		   valorsData=tmp + "|" + json_Valors.getString("date");
	        	   }
	        	   tmp=valors;	
	        	   if(i==0){
	        		   valors=json_Valors.getString("value");
	        	   }else{
	        		   valors=tmp + "," + json_Valors.getString("value");
	        	   }
	        	   
	        	   
	           }
	           Log.w("Error al carregar la informació", valors.toString());
	           //min='1.630'; Max='1.640'; escala 0.01 chxr
		       String embeddedWeb=Web + "&chxl=0:" + valorsData + "&chd=t:" + valors + "&chxr=1,1.62,1.65,0.01&chds=1.620,1.650,0,10";
		       embeddedWebView = (WebView)findViewById(R.id.Grafica);
		       embeddedWebView.loadUrl(embeddedWeb);
		   }catch(JSONException ex){
               Log.w("Error al carregar la informació", ex.toString());
               Toast.makeText(getApplicationContext(), "Error al carregar la informació", Toast.LENGTH_SHORT).show();
		   }
	   }

}

