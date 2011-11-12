package com.uoc.lsviewer;

import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SensorList extends GDActivity{

	private TextView tvNetInfo;
    //Fem la matriu d'informació sobre aquesta xarxa
	// I use HashMap arraList which takes objects
	private ArrayList <HashMap<String, Object>> Sensor;
	private static final String IdKEY = "SensorID";
	private static final String NomKEY = "SensorNom"; 
	private static final String TipusKEY = "SensorTipus";
	private static final String DescripcioKEY = "SensorDesc";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.sensorlist);
		//Indiquem sobre quina xarxa estem treballant
        tvNetInfo = (TextView)findViewById(R.id.tvNetInfo);	        
        Bundle bundle = getIntent().getExtras();			
	    tvNetInfo.setText(bundle.getString("etXarxa"));
	    
	    ListView listView = (ListView)findViewById(R.id.Llista);
	    Sensor = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> hm;       
       
        //Afegim les dades
        hm = new HashMap<String, Object>();
        hm.put(IdKEY, "1");
        hm.put(NomKEY, "2003-ch.1");
        hm.put(TipusKEY, R.drawable.bulo); 
        hm.put(DescripcioKEY, "2003 sensors de tensió, canal 1"); 
        Sensor.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(IdKEY, "2");
        hm.put(NomKEY, "2005-ch.1");
        hm.put(TipusKEY, R.drawable.bulo); 
        hm.put(DescripcioKEY, "2005 sensors de tensió, canal 1"); 
        Sensor.add(hm);
        
        hm = new HashMap<String, Object>();
        hm.put(IdKEY, "3");
        hm.put(NomKEY, "2007-ch.1");
        hm.put(TipusKEY, R.drawable.bulo); 
        hm.put(DescripcioKEY, "2007 sensors de tensió, canal 1"); 
        Sensor.add(hm);
        
        hm = new HashMap<String, Object>();
        hm.put(IdKEY, "4");
        hm.put(NomKEY, "2009-ch.1");
        hm.put(TipusKEY, R.drawable.bulo); 
        hm.put(DescripcioKEY, "2009 sensors de tensió, canal 1"); 
        Sensor.add(hm);
        
        hm = new HashMap<String, Object>();
        hm.put(IdKEY, "5");
        hm.put(NomKEY, "2011-ch.1");
        hm.put(TipusKEY, R.drawable.bulo); 
        hm.put(DescripcioKEY, "2011 sensors de tensió, canal 1"); 
        Sensor.add(hm);
        
        // Define SimpleAdapter and Map the values with Row view R.layout.listbox
        SimpleAdapter adapter = new SimpleAdapter(this, Sensor, R.layout.listsensorbox, 
         		new String[]{IdKEY, NomKEY, TipusKEY, DescripcioKEY}, new int[]{R.id.text1, R.id.text2, R.id.IMG, R.id.text4});
        		
         listView.setAdapter(adapter);
         listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

}
