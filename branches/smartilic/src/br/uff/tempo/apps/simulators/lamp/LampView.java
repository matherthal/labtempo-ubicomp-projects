package br.uff.tempo.apps.simulators.lamp;

import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Lamp;

public class LampView extends AbstractView {
	
	@Override
	public void createView(Bundle savedInstanceState) {
		setContentView(R.layout.lamp);
	}

	@Override
	public IResourceAgent createNewResourceAgent() {
		String name = "GeneralLamp" + getNextID();
		return new Lamp(name, name);
	}
	
	public AbstractPanel getPanel() {
		return (AbstractPanel) findViewById(R.id.lampPanel);
	}
}