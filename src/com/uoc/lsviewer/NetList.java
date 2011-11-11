package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NetList extends GDActivity {
	private String Xarxes[]={"Sagrada Familia", "Estatua de Colon", "Montjuic"};	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.netlist);
        ListView Llista=(ListView) findViewById(R.id.Llista);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1,Xarxes);
        Llista.setAdapter(adaptador);

        Llista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> padre, View v, int posicion,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(),padre.getItemAtPosition(posicion).toString() , Toast.LENGTH_SHORT).show();				
			}
		});		
    }
}