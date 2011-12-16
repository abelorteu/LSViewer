package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//http://devblogs.net/2011/01/04/custom-listview-with-image-using-simpleadapter/
public class NetList extends GDActivity {
	// I use HashMap arraList which takes objects
		private ArrayList <HashMap<String, Object>> Xarxa;
		private static final String idKEY = "IdXarxa";
		private static final String nomKEY = "NomXarxa";
		private static final String numKEY = "NumSensors";	
		
		String session;
		ServerConnection sc;		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.netlist);
		
		ListView listView = (ListView)findViewById(R.id.Llista);
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");			
		
		// Server Connection and convert response to string 
		sc = new ServerConnection(this, 0);		
		String aParams[] = {session};
		String result = sc.getDataConnection(aParams);		
		     		
		//parse json data	     
	    try{
	    	JSONArray jArray = new JSONArray(result);
	        Xarxa = new ArrayList<HashMap<String, Object>>();
	        HashMap<String, Object> hm;
	        for(int i = 0; i < jArray.length(); i++){	        	
	               JSONObject json_data = jArray.getJSONObject(i);
	               hm = new HashMap<String, Object>();
	               hm.put(idKEY, json_data.getString("IdXarxa"));
	               hm.put(nomKEY, json_data.getString("Nom"));
	               hm.put(numKEY, json_data.getString("Sensors"));
	               Xarxa.add(hm);
	               //http://p-xr.com/android-tutorial-how-to-parse-read-json-data-into-a-android-listview/
              
	              Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
	         }
		     // Define SimpleAdapter and Map the values with Row view R.layout.listbox
		      SimpleAdapter adapter = new SimpleAdapter(
		    		  this, 
		    		  Xarxa, 
		    		  R.layout.listbox, 
		    		  new String[]{idKEY, nomKEY, numKEY}, 
		    		  new int[]{R.id.idXarxa, R.id.nomXarxa, R.id.numSensors}
		      );	        		
		      listView.setAdapter(adapter);
		      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	       
	    }catch(JSONException e){
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	        Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
	    }
		              
        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            	TextView txt;
            	//setTitle(parent.getItemAtPosition(position).toString());
                Intent intent = new Intent(NetList.this, SensorList.class);	
              	Bundle bundle = new Bundle();
              	bundle.putString("session", session);
              	txt =(TextView)parent.getChildAt(position).findViewById(R.id.idXarxa);
				bundle.putString("idXarxa", txt.getText().toString());
				txt =(TextView)parent.getChildAt(position).findViewById(R.id.nomXarxa);
				bundle.putString("nomXarxa", txt.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
            }
          };
          listView.setOnItemClickListener(listener);			
    }
}