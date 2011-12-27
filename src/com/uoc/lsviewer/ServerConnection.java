package com.uoc.lsviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	
	public ServerConnection(Context cont) {		
		context = cont;
		res = context.getResources();
	}


	public String getDataConnection (String params) {

		String server = res.getString(R.string.Servidor) + params;

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
