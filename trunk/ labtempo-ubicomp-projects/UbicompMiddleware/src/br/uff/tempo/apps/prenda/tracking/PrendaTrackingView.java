package br.uff.tempo.apps.prenda.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import br.uff.tempo.R;

public class PrendaTrackingView extends Activity {
	public static final String TAG = "PrendaTrackingView"; 
	
	public static final int NEW_PERSON = 0;
	public static final int TRACK = NEW_PERSON + 1;
	
	private static int i = 0;
	
	private PrendaTrackingPanel panel;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.prenda_tracking);
		panel = (PrendaTrackingPanel) findViewById(R.id.trackingPanel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, NEW_PERSON, Menu.NONE, "Add a Person");
		menu.add(Menu.NONE, TRACK, Menu.NONE, "Define a Track");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case NEW_PERSON:
			
			panel.addPerson("User" + (++i));
			
			break;
			
		case TRACK:
			
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}