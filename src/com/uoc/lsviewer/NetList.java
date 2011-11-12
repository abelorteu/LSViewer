package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//http://devblogs.net/2011/01/04/custom-listview-with-image-using-simpleadapter/
public class NetList extends GDActivity {
	// I use HashMap arraList which takes objects
		private ArrayList <HashMap<String, Object>> Xarxa;
		private static final String XarxaKEY = "NomXarxa";
		private static final String NumKEY = "NumSensors";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.netlist);
		ListView listView = (ListView)findViewById(R.id.Llista);
		Xarxa = new ArrayList<HashMap<String,Object>>();
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
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       
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