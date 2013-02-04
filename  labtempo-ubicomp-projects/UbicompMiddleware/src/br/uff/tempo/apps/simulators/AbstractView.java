package br.uff.tempo.apps.simulators;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public abstract class AbstractView extends FragmentActivity {

	private IResourceAgent agent;

	// Dialog to get resource information
	private ResourceConfig resConf;
	private AbstractPanel panel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		// Get the intent that called this activity
		Intent intent = getIntent();
		// Get the data that came with the intent
		Bundle fromExternal = intent.getExtras();

		// if there is data to read from intent
		// (probably the agent) use it
		if (fromExternal != null) {
		
			agent = (IResourceAgent) fromExternal.getSerializable("agent");
			
			AbstractPanel panel = getPanel();
			if (panel != null) {
				panel.setupInterest();
			} else {
				Log.e("SmartAndroid", "AbstractView: Panel is null");
			}
		} else {
			//Just to compatibility
			agent = createNewResourceAgent();
			agent.identify();
		}
	}

	public abstract IResourceAgent createNewResourceAgent();
	
	public abstract AbstractPanel getPanel();

	public IResourceAgent getAgent() {
		return this.agent;
	}
	
	public int getNextID() {
		return new Random().nextInt(100);
	}

	public void setAgent(IResourceAgent agent) {
		this.agent = agent;
	}
}
