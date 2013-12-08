package br.uff.tempo.apps.simulators.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.uff.tempo.apps.simulators.tracking.mode.TrackingMode;

public class TrackingView extends Activity {
	
	public static final int NEW_PERSON = 0;
	public static final int TRACK = NEW_PERSON + 1;
	public static final int MANUAL = TRACK + 1;
	public static final int APPLY = MANUAL + 1;
	public static final int PLAY = APPLY + 1;
	public static final int STEP = PLAY + 1;
	
	protected TrackingPanel panel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.tracking);
		//panel = (TrackingPanel) findViewById(R.id.trackingPanel);
		panel = new TrackingPanel(this);
		setContentView(panel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, NEW_PERSON, Menu.NONE, "Add a Person");
		menu.add(Menu.NONE, MANUAL, Menu.NONE, "Move Manually");
		menu.add(Menu.NONE, TRACK, Menu.NONE, "Define a Path");
		menu.add(Menu.NONE, APPLY, Menu.NONE, "Select User...");
		menu.add(Menu.NONE, PLAY, Menu.NONE, "Play");
		menu.add(Menu.NONE, STEP, Menu.NONE, "Step");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case NEW_PERSON:
			panel.addPerson();
			break;
			
		case MANUAL:
			panel.setMode(TrackingMode.MANUAL_MOVE);
			break;
			
		case TRACK:
			panel.setMode(TrackingMode.DEFINE_TRACK);
			break;
		case APPLY:
			panel.applyPath();
			break;
		case PLAY:
			panel.playPath();
			break;
		case STEP:
			panel.stepPath(0);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}