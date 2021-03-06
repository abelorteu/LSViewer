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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SensorList extends GDActivity{

	private TextView tvNetInfo;
    //Fem la matriu d'informació sobre aquesta xarxa
	// I use HashMap arraList which takes objects
	private ArrayList <HashMap<String, Object>> Sensor;
	private static final String IdKEY = "SensorID";
	private static final String NomKEY = "SensorNom";
	private static final String ChanelKEY = "Canal"; 
	private static final String TipusKEY = "SensorTipus";
	private static final String DescripcioKEY = "SensorDesc";
	
	private ServerConnection sc;	
	private String session;
	private String idXarxa;
	private String nomXarxa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.sensorlist);	
		initActionBar();

	    ListView listView = (ListView)findViewById(R.id.Llista);	    
		//Indiquem sobre quina xarxa estem treballant
        tvNetInfo = (TextView)findViewById(R.id.tvNetInfo);
        
        Bundle bundle = getIntent().getExtras();		
        session = bundle.getString("session");
        idXarxa = bundle.getString("idXarxa");
        nomXarxa = bundle.getString("nomXarxa");	
        
        tvNetInfo.setText(nomXarxa);	    
	    
	    // Server Connection and convert response to string 
        sc = new ServerConnection(this);		
		String params = getResources().getString(R.string.llistatSensors) + "?session=" + session + "&IdXarxa=" + idXarxa;
		String result = sc.getDataConnection(params);
	    	    
		//parse json data	     
	    try{
	    	JSONArray jArray = new JSONArray(result);
	        Sensor = new ArrayList<HashMap<String, Object>>();
	        HashMap<String, Object> hm;
	       
	        for(int i = 0; i < jArray.length(); i++){	        	
	               JSONObject json_data = jArray.getJSONObject(i);
	               hm = new HashMap<String, Object>();
	               hm.put(IdKEY, json_data.getString("id"));
	               hm.put(NomKEY, json_data.getString("sensor"));
	               hm.put(ChanelKEY, " ch." + json_data.getString("canal"));
	               hm.put(TipusKEY, R.drawable.bulo); 
	               hm.put(DescripcioKEY, json_data.getString("Descripcio"));
	               Sensor.add(hm);
	               //http://p-xr.com/android-tutorial-how-to-parse-read-json-data-into-a-android-listview/
              
	              //Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
	         }       	        
	        
	     // Define SimpleAdapter and Map the values with Row view R.layout.listbox
	        SimpleAdapter adapter = new SimpleAdapter(
	        		this, 
	        		Sensor, 
	        		R.layout.listsensorbox, 
	         		new String[]{IdKEY, NomKEY, ChanelKEY ,TipusKEY, DescripcioKEY}, 
	         		new int[]{R.id.idSensor, R.id.nomSensor,R.id.Chanel, R.id.IMG, R.id.descSensor}
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
                Intent intent = new Intent(SensorList.this, SensorInfo.class);	
              	Bundle bundle = new Bundle();
              	bundle.putString("session", session);
              	txt =(TextView)parent.getChildAt(position).findViewById(R.id.nomSensor);
				bundle.putString("sensor", txt.getText().toString());
				bundle.putString("activity", "SensorList");
				bundle.putString("idXarxa", idXarxa);
			    bundle.putString("nomXarxa", nomXarxa);	
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
            }
          };
          listView.setOnItemClickListener(listener);		
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
				Intent intent = new Intent(SensorList.this, Home.class);							
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
	       
	    	Intent intent = new Intent(SensorList.this, NetList.class);							
			Bundle bundle = new Bundle();
			bundle.putString("session", session);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
