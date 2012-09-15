package br.uff.tempo.apps.simulators.lamp;

import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class LampView extends AbstractView {

	private static final String TAG = "LampView";
	private ILamp lamp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lamp);
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

	@Override
	public void createNewResourceAgent() {
		super.setAgent(new Lamp("Lampada Quarto"));
	}

}