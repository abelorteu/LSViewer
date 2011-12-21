package com.uoc.lsviewer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.DrawableStateSet;
import greendroid.graphics.drawable.MapPinDrawable;

public class MapsList extends GDMapActivity {
	
	private TextView tvName;
	private MapView mapView;
	private MapController controlMap = null;
	ServerConnection sc;	
	BasicItemizedOverlay itemizedOverlay;
	String session;
		
	private static final int[] PRESSED_STATE = {
	        android.R.attr.state_pressed
	};	 
		
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setActionBarContentView(R.layout.mapslist);
	        
	        Bundle bundle = getIntent().getExtras();
	        session = bundle.getString("session");	
	        
	        tvName = (TextView)findViewById(R.id.tvNom);
	        mapView = (MapView) findViewById(R.id.mapview);
	        mapView.setBuiltInZoomControls(true);
	        
	        controlMap = mapView.getController();
	        	        
	        tvName.setText("Net List");	        
	        
	        // Server Connection and convert response to string 
			sc = new ServerConnection(this, 0);		
			String aParams[] = {session};
			String result = sc.getDataConnection(aParams);		
	        
			// Recogemos los puntos			
			//parse json data	     
		    try{
		    	
		    	//[{"Poblacio":"Barcelona","Nom":"Sagrada Fam\u00c3\u00adlia","IdXarxa":"000","Sensors":"4","Lat":"41.403413","Lon":"2.173941"},{"Poblacio":"Barcelona","Nom":"Torre Agbar","IdXarxa":"001","Sensors":"16","Lat":"41.403702","Lon":"2.189503"},{"Poblacio":"Val\u00c3\u00a8ncia","Nom":"Passarel\u00c2\u00b7la de l'Exposici\u00c3\u00b3","IdXarxa":"002","Sensors":"18","Lat":"39,47322","Lon":"-0,36575"}]
		    	
		    	OverlayItem itemNet;
		    	JSONArray jArray = new JSONArray(result);
		    		        
		    	prepareDraw();	    	
		    	
		        for(int i = 0; i < jArray.length(); i++){	        	
		               JSONObject json_data = jArray.getJSONObject(i);
		               		               
		               itemNet = new OverlayItem(new GeoPoint((int)(json_data.getDouble("Lat") * 1E6), (int)(json_data.getDouble("Lon") * 1E6)), json_data.getString("Nom"), null);
		               itemizedOverlay.addOverlay(itemNet);
	            
		              Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
		         }	       
		     
		        mapView.getOverlays().clear();
		        mapView.getOverlays().add(itemizedOverlay);
		        
		        // Recogemos el centro de los puntos y lo centramos
		        GeoPoint point =  itemizedOverlay.getCenter();
		        controlMap.animateTo(point); 
		       
		    }catch(JSONException e){
		    	Log.e("log_tag", "Error parsing data "+e.toString());
		        Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
		    }			
	        
	        controlMap.setZoom(8);              
                        
	 }
	
	@Override
	protected boolean isRouteDisplayed() {	
		return false;
	}
	
	private void prepareDraw(){
		
		 // Pintamos los puntos
        final Resources r = getResources();	        
        
        // pinCsl
        int[][] states = new int[2][];
        int[] colors = new int[2];	        	        
        states[0] = PRESSED_STATE;
        colors[0] = Color.rgb(209, 208, 208);
        states[1] = DrawableStateSet.EMPTY_STATE_SET;
        colors[1] = Color.rgb(79, 76, 76);
       	        
        ColorStateList pinCsl = new ColorStateList(states, colors);
        ColorStateList dotCsl = new ColorStateList(states, colors);
        itemizedOverlay = new BasicItemizedOverlay(new MapPinDrawable(r, pinCsl, dotCsl));
               		
	}
	
	private class BasicItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public BasicItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        public void addOverlay(OverlayItem overlay) {
            mOverlays.add(overlay);
            populate();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return mOverlays.get(i);
        }

        @Override
        public int size() {
            return mOverlays.size();
        }

        @Override
        protected boolean onTap(int index) {
        	
       		Intent intent = new Intent(MapsList.this, ImagesNet.class);
       		Bundle bundle = new Bundle();
       		bundle.putString("session", session);
       		bundle.putInt("index", index);
       		intent.putExtras(bundle);
       		startActivity(intent);
        		
            return true;
        }
    }
}
