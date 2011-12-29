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
import android.view.KeyEvent;
import android.widget.TextView;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.graphics.drawable.DrawableStateSet;
import greendroid.graphics.drawable.MapPinDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

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
	        initActionBar();
	        
	        Bundle bundle = getIntent().getExtras();
	        session = bundle.getString("session");	
	        
	        tvName = (TextView)findViewById(R.id.tvNom);
	        mapView = (MapView) findViewById(R.id.mapview);
	        mapView.setBuiltInZoomControls(true);
	        
	        controlMap = mapView.getController();
	        	        
	        tvName.setText(getResources().getString(R.string.netList));	        
	        
	        // Server Connection and convert response to string 	
			sc = new ServerConnection(this);		
			String params = getResources().getString(R.string.llistatXarxes) + "?session=" + session;
			String result = sc.getDataConnection(params);
	        
			// Recogemos los puntos			
			//parse json data	     
		    try{		    	
		    	OverlayItem itemNet;
		    	JSONArray jArray = new JSONArray(result);
		    		        
		    	prepareDraw();	    	
		    	
		        for(int i = 0; i < jArray.length(); i++){	        	
		               JSONObject json_data = jArray.getJSONObject(i);
		               		               
		               itemNet = new OverlayItem(new GeoPoint((int)(json_data.getDouble("Lat") * 1E6), (int)(json_data.getDouble("Lon") * 1E6)), json_data.getString("Nom"), null);
		               itemizedOverlay.addOverlay(itemNet);
	            
		              //Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
		         }	       
		     
		        mapView.getOverlays().clear();
		        mapView.getOverlays().add(itemizedOverlay);
		        
		        // Recogemos el centro de los puntos y lo centramos
		        GeoPoint point =  itemizedOverlay.getCenter();
		        controlMap.animateTo(point); 
		       
		    }catch(JSONException e){
		    	Log.e("log_tag", "Error parsing data "+e.toString());
		        //Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
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
       		bundle.putInt("idXarxa", index);
       		intent.putExtras(bundle);
       		startActivity(intent);
        	finish();
            return true;
        }
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(this.getClass().getName(), "back button pressed");
	    	Intent intent = new Intent(MapsList.this, Home.class);							
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
				.setDrawable(new ActionBarDrawable(this, R.drawable.ic_menu_home)), R.id.action_bar_view_home);
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
			
			case R.id.action_bar_view_home:
				Intent intent = new Intent(MapsList.this, Home.class);							
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
