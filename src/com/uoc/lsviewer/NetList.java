package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//http://devblogs.net/2011/01/04/custom-listview-with-image-using-simpleadapter/
public class NetList extends GDActivity {
	// I use HashMap arraList which takes objects
		private ArrayList <HashMap<String, Object>> Xarxa;
		private static final String idKEY = "IdXarxa";
		private static final String nomKEY = "NomXarxa";
		private static final String numKEY = "NumSensors";	
		
		InputStream is;		
		String session;
		ServerConnection sc;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.netlist);
		
		ListView listView = (ListView)findViewById(R.id.Llista);
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");		
		
		sc = new ServerConnection(this, session, 0);		
		is = sc.getConnection();
		
		// Server Connection
		String result = "";		
		/*try{
             HttpClient httpclient = new DefaultHttpClient();
             
             //String Servidor = this.getResources().getString(R.string.Servidor);
           String server = getResources().getString(R.string.Servidor) + "getLlistatXarxes.php" + "?session=" + session;
            // String server = Servidor + "getLlistatXarxes.php" + "?session=1323903600";
             HttpPost httppost = new HttpPost(server);
            
             HttpResponse response = httpclient.execute(httppost); 
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             Log.e("log_tag", "connection success ");
             Toast.makeText(getApplicationContext(), "pass1", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
             Log.e("log_tag", "Error in http connection "+e.toString());
             Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
		}*/
     
		//convert response to string
		try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
            	 sb.append(line + "\n");
                 Toast.makeText(getApplicationContext(), "pass2", Toast.LENGTH_SHORT).show();
             }
             is.close();

             result = sb.toString();
		}catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
            Toast.makeText(getApplicationContext(), "fail2", Toast.LENGTH_SHORT).show();
		}

		//parse json data	     
	    try{
	    	JSONArray jArray = new JSONArray(result);
	        Xarxa = new ArrayList<HashMap<String, Object>>();
	        HashMap<String, Object> hm;
	        for(int i = 0; i < jArray.length(); i++){	        	
	               JSONObject json_data = jArray.getJSONObject(i);
	               hm = new HashMap<String, Object>();
	               hm.put(idKEY, json_data.getString("IdXarxa"));
	               hm.put(nomKEY, json_data.getString("Nom"));
	               hm.put(numKEY, json_data.getString("Sensors"));
	               Xarxa.add(hm);
	               //http://p-xr.com/android-tutorial-how-to-parse-read-json-data-into-a-android-listview/
              
	              Toast.makeText(getApplicationContext(), "pass3", Toast.LENGTH_SHORT).show();
	         }
		     // Define SimpleAdapter and Map the values with Row view R.layout.listbox
		      SimpleAdapter adapter = new SimpleAdapter(
		    		  this, 
		    		  Xarxa, 
		    		  R.layout.listbox, 
		    		  new String[]{nomKEY, numKEY}, 
		    		  new int[]{R.id.text1, R.id.text2}
		      );	        		
		      listView.setAdapter(adapter);
		      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	       
	        }catch(JSONException e){
	                Log.e("log_tag", "Error parsing data "+e.toString());
	                Toast.makeText(getApplicationContext(), "fail3", Toast.LENGTH_SHORT).show();
	        }
		
		
	/*	Xarxa = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> hm;       
       
        //Afegim les dades
        hm = new HashMap<String, Object>();
        hm.put(XarxaKEY, "Sagrada Familia");
        hm.put(NumKEY, "22 sensors");     
        Xarxa.add(hm);
        
        hm = new HashMap<String, Object>();
        hm.put(XarxaKEY, "Estatua de Colon");
        hm.put(NumKEY, "10 sensors");        
        Xarxa.add(hm);
        
        hm = new HashMap<String, Object>();
        hm.put(XarxaKEY, "Montjuic");
        hm.put(NumKEY, "14 sensors");        
        Xarxa.add(hm);

      // Define SimpleAdapter and Map the values with Row view R.layout.listbox
       SimpleAdapter adapter = new SimpleAdapter(this, Xarxa, R.layout.listbox, 
        		new String[]{XarxaKEY,NumKEY}, new int[]{R.id.text1, R.id.text2});
       		
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);*/
       
        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            	TextView txt =(TextView)parent.getChildAt(position).findViewById(R.id.text1);
            	//setTitle(parent.getItemAtPosition(position).toString());
                Intent intent = new Intent(NetList.this, SensorList.class);	
              	Bundle bundle = new Bundle();
				bundle.putString("etXarxa", txt.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
            }
          };
          listView.setOnItemClickListener(listener);
			
    }
}