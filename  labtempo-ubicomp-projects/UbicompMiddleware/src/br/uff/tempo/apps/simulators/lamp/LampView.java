package br.uff.tempo.apps.simulators.lamp;

import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Lamp;

public class LampView extends AbstractView {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lamp);
	}

	@Override
	public IResourceAgent createNewResourceAgent(RegistryData data) {
		return new Lamp(data.getResourceName(), data.getResourceName());
	}
	
	public AbstractPanel getPanel() {
		return (AbstractPanel) findViewById(R.id.lampPanel);
	}

}