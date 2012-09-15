package br.uff.tempo.apps.simulators;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public abstract class AbstractView extends FragmentActivity {

	private IResourceAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

		// else, creates a new one
		} else {
			createNewResourceAgent();
			agent.identify();
		}
	}

	public abstract void createNewResourceAgent();

	public IResourceAgent getAgent() {
		return this.agent;
	}

	public void setAgent(IResourceAgent agent) {
		this.agent = agent;
	}
}
