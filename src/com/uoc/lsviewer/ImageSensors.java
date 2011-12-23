package com.uoc.lsviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import greendroid.app.GDActivity;

public class ImageSensors extends GDActivity{

	private InternalView myView;
	private ServerConnection sc;
	private String url;
	private String idIMG;
	private String session;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		// Get variables
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");
		idIMG = bundle.getString("index");
		url = bundle.getString("url");
		
		// Create view (canvas)
		myView = new InternalView(this);
		setActionBarContentView(myView);
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
			
			// Resize image
			Bitmap resized = null;
			Display display = getWindowManager().getDefaultDisplay();                 
		
			int imgHeight = mIcon1.getHeight();
			int imgWidth = mIcon1.getWidth();
			double escalaX = (double)display.getWidth() / (double)imgWidth;
			double escalaY = (double)display.getHeight() / (double)imgHeight;

			// Tomo como referencia el minimo de las escalas
			double fEscala = Math.min(escalaX, escalaY);
			if(fEscala > 1) {
				fEscala = 1;
			}	
			imgHeight = (int) (imgHeight * fEscala);
			imgWidth = (int) (imgWidth * fEscala);

			resized = Bitmap.createScaledBitmap(mIcon1, imgWidth, imgHeight, false); 			

			Paint paint = new Paint();  
			//canvas.drawColor(R.color.greyWeak);
						
			// paint image
			canvas.drawBitmap(resized, 0, 0, paint);

			// Get sensors points
			// Server Connection and convert response to string 
			sc = new ServerConnection(context, 3);		
			String aParams[] = {session, idIMG};
			String result = sc.getDataConnection(aParams);

			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);

				// Get dot
				Bitmap dot = BitmapFactory.decodeResource(getResources(), R.drawable.red_dot);
				// Calcule the point of the dot
				int xDot = dot.getHeight() / 2;
				int yDot = dot.getWidth();
				
				// Get data
				for(int i = 0; i < jArray.length(); i++){	        	
					JSONObject json_data = jArray.getJSONObject(i);

					float x =  (float) (json_data.getInt("x") * fEscala) - xDot;
					float y =  (float) (json_data.getInt("y") * fEscala) - yDot; 
					
					// Paint sensor
					canvas.drawBitmap(dot, x, y, paint);
					//images.add(server + "Imatges/" + json_data.getString("imatge"));
					//idImages.add(json_data.getString("IdImatge"));					
				}   
				//Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
				//imageURLs = images.toArray(new String[images.size()]);
				//idIMG = idImages.toArray(new String[idImages.size()]);

			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
			}




		}
	}
}
