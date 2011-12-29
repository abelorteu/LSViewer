package com.uoc.lsviewer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

public class ImageSensors extends GDActivity{

	private InternalView myView;	// Internal class to draw
	private ServerConnection sc;	// Connection to server
	private String url;
	private String idIMG;
	private String session;
	private Integer idXarxa;
	private Context context;
	private int selX;		// X index of selection
	private int selY;		// Y index of selection
	private ArrayList<Dot> al;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		// Get variables
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");
		idIMG = bundle.getString("idIMG");
		idXarxa = bundle.getInt("idXarxa");
		url = bundle.getString("url");

		// Create view (canvas)
		myView = new InternalView(this);
		setActionBarContentView(myView);
		initActionBar();
		
	}

	private class InternalView extends View{
		public InternalView(Context context){
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			// Get image from server
			URL url_value;
			Bitmap mIcon1 = null; 							
			try {
				url_value = new URL(url);
				mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
			} catch (IOException e1) {
				Log.e("log_tag", "Error in http connection "+e1.toString());
			}			 

			Paint background = new Paint();
			background.setColor(getResources().getColor(R.color.greyWeak));
			canvas.drawRect(0, 0, getWidth(), getHeight(), background);

			// Resize image
			Bitmap resized = null;

			int imgHeight = mIcon1.getHeight();
			int imgWidth = mIcon1.getWidth();
			double escalaX = (double) getWidth() / (double)imgWidth;
			double escalaY = (double) getHeight() / (double)imgHeight;

			// Get mininum scale
			double fEscala = Math.min(escalaX, escalaY);
			if(fEscala > 1) {
				fEscala = 1;
			}	
			imgHeight = (int) (imgHeight * fEscala);
			imgWidth = (int) (imgWidth * fEscala);

			resized = Bitmap.createScaledBitmap(mIcon1, imgWidth, imgHeight, false); 			

			Paint paint = new Paint();  

			// paint image
			canvas.drawBitmap(resized, 0, 0, paint);

			// Get sensors points
			// Server Connection and convert response to string 			
			sc = new ServerConnection(context);		
			String params = getResources().getString(R.string.llistaSensorsImatge) + "?session=" + session + "&IdImatge=" + idIMG;
			String result = sc.getDataConnection(params);

			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);

				// Get dot
				Bitmap dot = BitmapFactory.decodeResource(getResources(), R.drawable.red_dot);
				// Calcule the point of the dot
				int xDot = dot.getHeight() / 2;
				int yDot = dot.getWidth();

				al = new ArrayList<Dot>();

				// Get data
				for(int i = 0; i < jArray.length(); i++){	        	
					JSONObject json_data = jArray.getJSONObject(i);
										
					int xP = (int) (json_data.getInt("x") * fEscala);
					int yP = (int) (json_data.getInt("y") * fEscala);
					Rect dotRect = new Rect(xP - 11, yP - 32, xP + 11, yP - 5);
				
					// Save Dot class
					al.add(new Dot(json_data.getString("sensor"), xP, yP, dotRect));

					float x =  (float) xP - xDot;
					float y =  (float) yP - yDot; 

					// Paint sensor
					canvas.drawBitmap(dot, x, y, paint);
				}   

			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				//Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() != MotionEvent.ACTION_DOWN)
				return super.onTouchEvent(event);

			selX = (int) event.getX();
			selY = (int) event.getY();
			boolean in = false;
			String idSensor = null;
			for(int i = 0; i < al.size(); i++) {
				Dot d = al.get(i);
				Rect rect = d.getDotRect();
				
				if(rect.contains(selX, selY)) {
					//Toast.makeText(getApplicationContext(), "YES! onTouchEvent: x " + event.getX() + ", y " + event.getY(), Toast.LENGTH_SHORT).show();
					in = true;
					idSensor = d.getId();
				}
			}
			
			if (in){
				Intent intent = new Intent(ImageSensors.this, SensorInfo.class);
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				bundle.putString("sensor", idSensor);
				bundle.putString("activity", "ImageSensors");
				
				bundle.putString("idIMG", idIMG);
				bundle.putInt("idXarxa", idXarxa);
				bundle.putString("url", url);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
			return in;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(this.getClass().getName(), "back button pressed");
	        
	    	Intent intent = new Intent(ImageSensors.this, ImagesNet.class);
       		Bundle bundle = new Bundle();
       		bundle.putString("session", session);
       		bundle.putInt("idXarxa", idXarxa);
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
				Intent intent = new Intent(ImageSensors.this, Home.class);							
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

	class Dot
	{
		String id;
		int x;
		int y;
		Rect dotRect;

		Dot(String id, int x, int y, Rect dotRect) { 
			this.id = id; 
			this.x = x;
			this.y = y;
			this.dotRect = dotRect;
		}

		public String getId() { return id; }
		public int getX() { return x; }
		public int getY() { return y; }
		public Rect getDotRect() { return dotRect; }

	}
}
