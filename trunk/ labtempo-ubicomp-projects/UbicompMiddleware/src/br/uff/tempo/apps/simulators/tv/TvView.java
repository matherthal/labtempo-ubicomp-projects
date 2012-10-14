package br.uff.tempo.apps.simulators.tv;

import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TvView extends AbstractView {

	private static final String TAG = "TvView";
	private ITelevision agent;
	private static int id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tv);
	}

	@Override
	public IResourceAgent createNewResourceAgent() {
		return new Television("SomeTV");		
	}
}