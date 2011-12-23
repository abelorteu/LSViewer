package com.uoc.lsviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import greendroid.app.GDActivity;

public class ImageSensors extends GDActivity{

	 InternalView myView;
	 String url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.imagesensors);
		
		Bundle bundle = getIntent().getExtras();
        String session = bundle.getString("session");
        Integer idXarxa = bundle.getInt("index");
        String url = bundle.getString("url");             
        
        ImageView imgView =(ImageView)findViewById(R.id.imgView);
        
        //http://www.androidpeople.com/android-load-image-url-example
        
        Drawable drawable = LoadImageFromWebOperations(url);
        imgView.setBackgroundDrawable(drawable);*/
        
                
        //Canvas
        //http://www.droidnova.com/playing-with-graphics-in-android-part-i,147.html
		//http://www.androidda.com/2011/03/usar-canvas-drawing-en-android/
		//http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
        
        
        super.onCreate(savedInstanceState);
        myView = new InternalView(this);
       

        Bundle bundle = getIntent().getExtras();
        String session = bundle.getString("session");
        Integer idXarxa = bundle.getInt("index");
        url = bundle.getString("url");   
        
        //Drawable drawable = LoadImageFromWebOperations(url);
        //myView.setBackgroundDrawable(drawable);
       // myView.setBackgroundColor(R.color.greyWeak);
        
        
        
        setActionBarContentView(myView);
        
	}

    private Drawable LoadImageFromWebOperations(String url) {
    	try {
    		InputStream is = (InputStream) new URL(url).getContent();
    		Drawable d = Drawable.createFromStream(is, "src name");
		    return d;
    	}catch (Exception e) {
	      	System.out.println("Exc="+e);
	       	return null;
	    }
    }
    
    private class InternalView extends View{
        public InternalView(Context context){
            super(context);
        }
 
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            
            URL url_value;
        	Bitmap mIcon1 = null; 
			try {
				url_value = new URL(url);
				mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}          
            
			
			Bitmap resized = null;
			//try {                   
                 
                Display display = getWindowManager().getDefaultDisplay();                 
                int screenHeight = display.getHeight();
                int screenWidth = display.getWidth();  
                               
                int imgHeight = mIcon1.getHeight();
                int imgWidth = mIcon1.getWidth();
                                
                double escalaX = (double)screenWidth / (double)imgWidth;
        		double escalaY = (double)screenHeight / (double)imgHeight;
         
        		// Tomo como referencia el minimo de las escalas
        		double fEscala = Math.min(escalaX, escalaY);
        		if(fEscala > 1) {
        			fEscala = 1;
        		}	
        			imgHeight = (int) (imgHeight * fEscala);
        			imgWidth = (int) (imgWidth * fEscala);
        		
                                
                resized = Bitmap.createScaledBitmap(mIcon1, imgWidth, imgHeight, false);  
			//} catch (Exception e) {
				// TODO: handle exception
			//}
			
            Paint paint = new Paint();  
            //paint.setStyle(Paint.Style.FILL);
            //paint.setColor(R.color.greyWeak);
            canvas.drawColor(R.color.greyWeak);
            //canvas.drawBitmap(mIcon1, null, paint);
            //Escalar imagen 
            //http://www.anddev.org/resize_and_rotate_image_-_example-t621.html
            //http://www.anddev.org/android-2d-3d-graphics-opengl-problems-f55/resizing-a-bitmap-t14882.html
            canvas.drawBitmap(resized, 0, 0, paint);
            //canvas.setBitmap(mIcon1);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_dot), (float) (151 * fEscala), (float) (200 * fEscala), paint);
            
 
        }
    }
}
