package com.uoc.lsviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class ServerConnection {
	
	Context context;
	Resources res;
	String session;
	InputStream is;	
	int callType;
	
	
	public ServerConnection(Context cont, int call) {		
		context = cont;
		res = context.getResources();
		callType = call;
				
	}
	
	public String getDataConnection (String aParams[]) {
		
		String servidor = res.getString(R.string.Servidor);		
		String con = "";	
		String server = "";
		String params = "";
		
		switch (callType) {
		case 0:
			con = context.getResources().getString(R.string.llistatXarxes);
			params = "?session=" + aParams[0];
			break;
		case 1:
			con = context.getResources().getString(R.string.llistatSensors);
			params = "?session=" + aParams[0] + "&IdXarxa=" + aParams[1];
			break;
		case 2:
			con = context.getResources().getString(R.string.llistaImatges);
			break;
		case 3:
			con = context.getResources().getString(R.string.llistaSensorsImatges);
			break;
		case 4:
			con = context.getResources().getString(R.string.sensorInfo);
			params = "?session=" + aParams[0] + "&sensor=" + aParams[1];
			break;
		case 5:
			con = context.getResources().getString(R.string.valorsGrafic);
			break;
		default:
			break;
		}
		server = servidor + con + params;
				
		// Server Connection
		try {
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost(server);            
             HttpResponse response = httpclient.execute(httppost); 
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             Log.e("log_tag", "connection success ");
            // Toast.makeText(context.getApplicationContext(), "pass1", Toast.LENGTH_SHORT).show();
		} catch(Exception e) {
             Log.e("log_tag", "Error in http connection "+e.toString());
             Toast.makeText(context.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
		}		
		
		String result = responseToString();
		return result;
	}
	
	public String responseToString(){
		
		String result = "";		
		//convert response to string
		try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
            	 sb.append(line + "\n");
                 //Toast.makeText(context.getApplicationContext(), "pass2", Toast.LENGTH_SHORT).show();
             }
             is.close();

             result = sb.toString();
		}catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
           // Toast.makeText(context.getApplicationContext(), "fail2", Toast.LENGTH_SHORT).show();
		}
		
		return result;
	}
}
