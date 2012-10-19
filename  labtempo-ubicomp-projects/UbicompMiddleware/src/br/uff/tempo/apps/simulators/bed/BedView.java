package br.uff.tempo.apps.simulators.bed;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Bed;

public class BedView extends AbstractView {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.bed);
	}

	@Override
	public IResourceAgent createNewResourceAgent(RegistryData data) {
		
		return new Bed("bed" + getNextID() + ".ra");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onRegisterClick(View target) {
		Toast.makeText(BedView.this, "Register Called", Toast.LENGTH_LONG).show();
	}

	@Override
	public AbstractPanel getPanel() {
		return (AbstractPanel) findViewById(R.id.bedPanel);
	}

}