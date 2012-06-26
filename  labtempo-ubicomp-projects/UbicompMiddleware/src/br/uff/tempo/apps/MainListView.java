package br.uff.tempo.apps;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.baseview.BaseView;
import br.uff.tempo.apps.map.MapActivity;
import br.uff.tempo.apps.rule.RuleActivity;
import br.uff.tempo.apps.stove.StoveView;
import br.uff.tempo.middleware.comm.Dispatcher;

public class MainListView extends ListActivity {
	
	//@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		Dispatcher d = Dispatcher.getInstance();
		
		if (!d.isAlive())
			d.start();//start listener
		
		//Print devices list
		String[] devices = getResources().getStringArray(R.array.devices_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, devices));
		
		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				//String dev = ((TextView) view).getText().toString();
				//Toast.makeText(getApplicationContext(), dev, Toast.LENGTH_SHORT).show();
				
				String item = lv.getItemAtPosition(position).toString();
				//Toast.makeText(MainListView.this, "Item = " + id + " --> position: " + position + " id: ", Toast.LENGTH_SHORT).show();
				//Toast.makeText(MainListView.this, R.array.devices_array);
				
				//TODO: Change name to ID. There's a problem with some characters
				
				// Calling appropriate activity
				if (item.equals("Regra")) {
					Intent intent = new Intent(MainListView.this,
							RuleActivity.class);
					startActivity(intent);
				} else if (item.equals("Fogao")) {
					Intent intent = new Intent(MainListView.this,
							StoveView.class);
					startActivity(intent);
				} else if(item.equals("Repositorio de Recursos"))
				{
					Intent intent = new Intent(MainListView.this,
							BaseView.class);
					startActivity(intent);
				} else if (item.equals("Mapa da casa"))
				{
					Intent intent = new Intent(MainListView.this,
							MapActivity.class);
					startActivity(intent);
				} else
				{
					Toast.makeText(MainListView.this, "Não existe agente para este item: " + item.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
