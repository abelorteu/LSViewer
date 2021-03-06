package com.uoc.lsviewer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

public class ImagesNet extends GDActivity implements OnItemClickListener, OnClickListener {
		
	private URLImageAdapter adapter;
	private ServerConnection sc;
	private String[] imageURLs;
	private String[] idIMG;
	private String session;
	private Integer idXarxa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.imagesnet);
		initActionBar();
		
		Bundle bundle = getIntent().getExtras();
        session = bundle.getString("session");
        idXarxa = bundle.getInt("idXarxa");
		
		 // Server Connection and convert response to string     
        sc = new ServerConnection(this);		
		String params = getResources().getString(R.string.llistaImatges) + "?session=" + session + "&IdXarxa=" + idXarxa.toString();
		String result = sc.getDataConnection(params);
		
		ArrayList<String> images = new ArrayList<String>();
		ArrayList<String> idImages = new ArrayList<String>();
					
		//parse json data	     
	    try{
	    	JSONArray jArray = new JSONArray(result);
	    	String server = getResources().getString(R.string.Servidor);
	    	
	        for(int i = 0; i < jArray.length(); i++){	        	
	               JSONObject json_data = jArray.getJSONObject(i);
	               images.add(server + "Imatges/" + json_data.getString("imatge"));
	               idImages.add(json_data.getString("IdImatge"));
	         }   
	        imageURLs = images.toArray(new String[images.size()]);
	        idIMG = idImages.toArray(new String[idImages.size()]);	     
	       
	    }catch(JSONException e){
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	    }		
		// get data generated before a config change, if it exists
		final Object data = getLastNonConfigurationInstance();
		
		// instantiate our adapter, sending in any data
		adapter = new URLImageAdapter(this, data, imageURLs);
		
		// attach to the grid layout in the UI
		GridView grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
	}

	/**
	 * Open new activity to show the selected image
	 * full-screen in a new Activity.
	 */
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String urlIMG = (String) adapter.getItem(position);
		Intent intent = new Intent(ImagesNet.this, ImageSensors.class);
		Bundle bundle = new Bundle();
		bundle.putString("session", session);
		bundle.putInt("idXarxa", idXarxa);
		bundle.putString("idIMG", idIMG[position]);
		bundle.putString("url", urlIMG);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	/**
	 * Open a webpage when the textview is clicked.
	 */
	@Override
	public void onClick(View v) {		
	}	
	
	/**
	 * Preserve adapter data between orientation changes
	 * See: http://developer.android.com/reference/android/app/Activity.html#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter.getData();
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
				Intent intent = new Intent(ImagesNet.this, Home.class);							
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
	        //moveTaskToBack(true);
	    	Intent intent = new Intent(ImagesNet.this, MapsList.class);							
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
