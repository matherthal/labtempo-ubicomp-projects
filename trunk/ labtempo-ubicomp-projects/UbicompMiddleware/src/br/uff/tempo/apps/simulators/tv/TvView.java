package br.uff.tempo.apps.simulators.tv;

import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Television;

public class TvView extends AbstractView {

	@Override
	public void createView(Bundle savedInstanceState) {
		setContentView(R.layout.tv);
	}

	@Override
	public IResourceAgent createNewResourceAgent() {
		
		String name = "GeneralTV" + getNextID();
		return new Television(name, name);		
	}

	@Override
	public AbstractPanel getPanel() {
		return (AbstractPanel) findViewById(R.id.tvPanel);
	}
}