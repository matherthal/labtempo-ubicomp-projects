package br.uff.tempo.apps;

import br.uff.tempo.*;
import br.uff.tempo.apps.stove.StoveView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainListView extends ListActivity {
	
	//@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Print devices list
		String[] devices = getResources().getStringArray(R.array.devices_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, devices));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				//String dev = ((TextView) view).getText().toString();
				//Toast.makeText(getApplicationContext(), dev, Toast.LENGTH_SHORT).show();
				
				//Calling appropriate activity
				Intent intent = new Intent(MainListView.this, StoveView.class);
		        startActivity(intent);
			}
		});
	}
}
