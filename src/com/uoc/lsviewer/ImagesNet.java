package com.uoc.lsviewer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import greendroid.app.GDActivity;

public class ImagesNet extends GDActivity implements OnItemClickListener, OnClickListener {
	
	//http://androideity.com/2011/08/28/controles-de-seleccion-en-android-gridview/
	//http://pablithiu-blog.blogspot.com/2010/01/importando-imagenes-en-android.html
	//http://cdn.cs76.net/2011/spring/lectures/6/src6-dan/Threads03/src/net/cs76/lectures/Threads03/
	
	private URLImageAdapter adapter;
	ServerConnection sc;
	private String[] imageURLs;
	private String[] idIMG;
	private String session;
	private Integer idXarxa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.imagesnet);
		
		Bundle bundle = getIntent().getExtras();
        session = bundle.getString("session");
        idXarxa = bundle.getInt("index");
		
		 // Server Connection and convert response to string 
		/*sc = new ServerConnection(this, 2);		
		String aParams[] = {session, idXarxa.toString()};
		String result = sc.getDataConnection(aParams);*/
    
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
	        Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
	        imageURLs = images.toArray(new String[images.size()]);
	        idIMG = idImages.toArray(new String[idImages.size()]);
	     
	       
	    }catch(JSONException e){
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	        Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
	    }		
		
		// get data generated before a config change, if it exists
		final Object data = getLastNonConfigurationInstance();
		
		// instantiate our adapter, sending in any data
		adapter = new URLImageAdapter(this, data, imageURLs);
		
		// attach to the grid layout in the UI
		GridView grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);

		// enable the little textview at the bottom to open a link
		//TextView copy = (TextView) findViewById(R.id.copynotice);
		//copy.setOnClickListener(this);	
	}

	/**
	 * Open new activity to show the selected image
	 * full-screen in a new Activity.
	 */
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		//openPage((String) adapter.getItem(position));
		
		String urlIMG = (String) adapter.getItem(position);
		Intent intent = new Intent(ImagesNet.this, ImageSensors.class);
		Bundle bundle = new Bundle();
		bundle.putString("session", session);
		bundle.putString("index", idIMG[position]);
		bundle.putString("url", urlIMG);
		intent.putExtras(bundle);
		startActivity(intent);
	}


	/**
	 * Open a webpage when the textview is clicked.
	 */
	@Override
	public void onClick(View v) {
//		openPage("http://danallan.net");		
	}


	/**
	 * Start a browser activity to display the url passed.
	 */
	public void openPage(String url) {
		// create an intent
		Intent data = new Intent();
		
		// specify the intent's action and url
		data.setAction(Intent.ACTION_VIEW);
		data.setData(Uri.parse(url));
		
    	try {
    		startActivity(data); 
    	} catch (ActivityNotFoundException e) {
    		Log.e("Threads03", "Cannot find an activity to start URL " + url);
    	}
	}
	
	/**
	 * Preserve adapter data between orientation changes
	 * See: http://developer.android.com/reference/android/app/Activity.html#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter.getData();
	}
	
}
