package com.uoc.lsviewer;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.DrawableStateSet;
import greendroid.graphics.drawable.MapPinDrawable;

public class MapsList extends GDMapActivity {
	
	private MapView mapView;
	private MapController controlMap = null;
	private int typeMap = 0;
	private Context context;
	
	private static final int[] PRESSED_STATE = {
	        android.R.attr.state_pressed
	    };	 
		
	private static final OverlayItem[] sFrance = {
        new OverlayItem(new GeoPoint(48635600, -1510600), "Mont Saint Michel", null),
        new OverlayItem(new GeoPoint(48856700, 2351000), "Paris", null),
        new OverlayItem(new GeoPoint(44837400, -576100), "Bordeaux", null),
        new OverlayItem(new GeoPoint(48593100, -647500), "Domfront", null)
    };
	
	private static final OverlayItem[] sEurope = {
		new OverlayItem(new GeoPoint(55755800, 37617600), "Moscow", null),
	    new OverlayItem(new GeoPoint(59332800, 18064500), "Stockholm", null),
	    new OverlayItem(new GeoPoint(59939000, 30315800), "Saint Petersburg", null),
	    new OverlayItem(new GeoPoint(60169800, 24938200), "Helsinki", null),
	    new OverlayItem(new GeoPoint(60451400, 22268700), "Turku", null),
	    new OverlayItem(new GeoPoint(65584200, 22154700), "Lulek", null),
	    new OverlayItem(new GeoPoint(59438900, 24754500), "Talinn", null),
	    new OverlayItem(new GeoPoint(66498700, 25721100), "Rovaniemi", null)
	};
	
	private static final OverlayItem[][] sAreas = {
        sFrance, sEurope
    };

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setActionBarContentView(R.layout.mapslist);
	        
	        mapView = (MapView) findViewById(R.id.mapview);
	        mapView.setBuiltInZoomControls(true);
	        
	        controlMap = mapView.getController();
	        	        
	        // Recogemos los puntos
	        final OverlayItem[] sNet = getNet();	        
	        drawPoints(sNet);
	        controlMap.setZoom(13);        
	        
                        
	 }
	
	@Override
	protected boolean isRouteDisplayed() {	
		return false;
	}
	
	private void drawPoints(OverlayItem[] items){
		
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
        BasicItemizedOverlay itemizedOverlay = new BasicItemizedOverlay(new MapPinDrawable(r, pinCsl, dotCsl));
                
        for (int j = 0; j < items.length; j++) {
            itemizedOverlay.addOverlay(items[j]);
        }        
        
        mapView.getOverlays().clear();
        mapView.getOverlays().add(itemizedOverlay); 
        
        // Recogemos el centro de los puntos y lo centramos
        GeoPoint point =  itemizedOverlay.getCenter();
        controlMap.animateTo(point);       

	}
	
		
	private OverlayItem[] getNet() {
		
		OverlayItem[] itemsNet = {				
				new OverlayItem(new GeoPoint((int)(41.403531 * 1E6), (int)(2.174027 * 1E6)), "Sagrada Familia", null),
				new OverlayItem(new GeoPoint((int)(41.375802 * 1E6), (int)(2.177782 * 1E6)), "Estatua de Colon", null),
				new OverlayItem(new GeoPoint((int)(41.363991 * 1E6), (int)(2.157912 * 1E6)), "Montjuic" , null)
		};		
		
		return itemsNet;
	}
	
	private OverlayItem[] getSensors(int index) {
		
		OverlayItem[] itemsSensors = {				
				new OverlayItem(new GeoPoint((int)(41.404135 * 1E6), (int)(2.174504 * 1E6)), "Point 1", null),
				new OverlayItem(new GeoPoint((int)(41.40335 * 1E6), (int)(2.174805 * 1E6)), "Point 2", null),
				new OverlayItem(new GeoPoint((int)(41.403274 * 1E6), (int)(2.174145 * 1E6)), "Point 3" , null)
		};		
		
		return itemsSensors;
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
        	
        	if (typeMap == 0) {        		
        		final OverlayItem[] sSensors = getSensors(index);
        		drawPoints(sSensors);
        		controlMap.setZoom(16);
        		typeMap = 1;
        		
        	} else {
        		Intent intent = new Intent(MapsList.this, SensorInfo.class);
        		Bundle bundle = new Bundle();
        		bundle.putInt("index", index);
        		intent.putExtras(bundle);
        		startActivity(intent);
        	}

            return true;
        }


    }

}
