package br.uff.tempo.apps.simulators.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.utils.Creator;

public class TrackingView extends Activity {
	
	public static final int NEW_PERSON = 0;
	public static final int TRACK = NEW_PERSON + 1;
	public static final int MOBILE_RES = TRACK + 1;
	
	private static int i = 0;
	
	private TrackingPanel panel;
	private Creator resCreator;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tracking);
		panel = (TrackingPanel) findViewById(R.id.trackingPanel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, MOBILE_RES, Menu.NONE, "Control a new Mobile resource");
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
			
		case MOBILE_RES:
			
			break;
			
		case TRACK:
			
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}