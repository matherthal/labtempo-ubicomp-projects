package br.uff.tempo.apps.simulators;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public abstract class AbstractView extends FragmentActivity implements
		IResourceChooser {

	private IResourceAgent agent;
	private boolean fromMap = false;

	// Dialog to get resource information
	private ResourceConfig resConf;
	
	private AbstractPanel panel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Get the intent that called this activity
		Intent intent = getIntent();
		// Get the data that came with the intent
		Bundle fromExternal = intent.getExtras();

		// if there is data to read from intent
		// (probably the agent) use it
		if (fromExternal != null) {
			agent = (IResourceAgent) fromExternal.getSerializable("agent");
			this.fromMap = true;

		// else, creates a new one
		} else {

			// Asks for the user about the Resource Informations
			// such as Name and Position
			callDialog();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (fromMap) {
			getPanel().setupInterest();
		}
	}

	public void onRegisteredResourceChoosed(String resourceRAI) {}

	public void onDialogFinished(Dialog dialog) {
		
		agent = createNewResourceAgent(resConf.getData());

		if (agent != null) {
			agent.identify();
			
			AbstractPanel p = getPanel();
			
			p.setAgent(agent);
			p.thereIsAnAgent();
			
			p.setupInterest();
			
			p.invalidate();
			
		} else {
			Log.e("SmartAndroid", "AbstractView: Agent is NULL!");
		}
	}

	private final void callDialog() {

		resConf = new ResourceConfig(this);
		resConf.showDialog();
	}

	public abstract IResourceAgent createNewResourceAgent(RegistryData data);
	
	public abstract AbstractPanel getPanel();

	public IResourceAgent getAgent() {
		return this.agent;
	}

	public void setAgent(IResourceAgent agent) {
		this.agent = agent;
	}
}
