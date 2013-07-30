package br.uff.tempo.apps.simulators.bed;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Bed;

public class BedView extends AbstractView {
	
	@Override
	public void createView(Bundle savedInstance) {
		setContentView(R.layout.bed);
	}

	@Override
	public IResourceAgent createNewResourceAgent() {
		
		String name = "GeneralBed" + getNextID();
		return new Bed(name, name);
	}

	public void onRegisterClick(View target) {
		Toast.makeText(BedView.this, "Register Called", Toast.LENGTH_LONG).show();
	}

	@Override
	public AbstractPanel getPanel() {
		return (AbstractPanel) findViewById(R.id.bedPanel);
	}
}