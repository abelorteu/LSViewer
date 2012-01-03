package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
		initActionBar();
		
		ListView listView = (ListView)findViewById(R.id.Llista);
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");			
		
		// Server Connection and convert response to string 	
		sc = new ServerConnection(this);		
		String params = getResources().getString(R.string.llistatXarxes) + "?session=" + session;
		String result = sc.getDataConnection(params);
		     		
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
              
	              //Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
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
	        //Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
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
				finish();
            }
          };
          listView.setOnItemClickListener(listener);			
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(this.getClass().getName(), "back button pressed");
	    	Intent intent = new Intent(NetList.this, Home.class);							
			Bundle bundle = new Bundle();
			bundle.putString("session", session);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void initActionBar() {
		addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_bar_compass)), R.id.action_bar_maps);
		addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this, R.drawable.ic_menu_home)), R.id.action_bar_view_home);		
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
			case R.id.action_bar_maps:
				Intent intent2 = new Intent(NetList.this, MapsList.class);							
				Bundle bundle2 = new Bundle();
				bundle2.putString("session", session);
				intent2.putExtras(bundle2);
				startActivity(intent2);
				finish();				
			break;	
			case R.id.action_bar_view_home:
				Intent intent = new Intent(NetList.this, Home.class);							
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
}