package com.uoc.lsviewer;

import java.io.InputStream;

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
	
	public ServerConnection(Context cont, String idSession, int call) {		
		context = cont;
		res = context.getResources();
		session = idSession;		
		callType = call;
	}
	
	public InputStream getConnection () {
		
		String servidor = res.getString(R.string.Servidor);
		
		String con = "";
		String params = "";	
		String server = "";
		switch (callType) {
		case 0:
			con = context.getResources().getString(R.string.llistatXarxes);
			params = "?session=" + session;
			server = servidor + con + params;
			break;
		case 1:
			con = context.getResources().getString(R.string.llistatSensors);
			
			break;
		case 2:
			con = context.getResources().getString(R.string.llistaImatges);
			break;
		case 3:
			con = context.getResources().getString(R.string.llistaSensorsImatges);
			break;
		case 4:
			con = context.getResources().getString(R.string.sensorInfo);
			break;
		case 5:
			con = context.getResources().getString(R.string.valorsGrafic);
			break;
		default:
			break;
		}
				
		// Server Connection
		try {
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost(server);            
             HttpResponse response = httpclient.execute(httppost); 
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             Log.e("log_tag", "connection success ");
             Toast.makeText(context.getApplicationContext(), "pass1", Toast.LENGTH_SHORT).show();
		} catch(Exception e) {
             Log.e("log_tag", "Error in http connection "+e.toString());
             Toast.makeText(context.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
		}			
		
		return is;
	}

}
