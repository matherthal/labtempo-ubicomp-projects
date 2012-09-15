package br.uff.tempo.apps.simulators.bed;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class BedView extends AbstractView {

	private static final String TAG = "BedView"; 
	private IBed bed;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.bed);
	}

	@Override
	public void createNewResourceAgent() {
		
		super.setAgent(new Bed("Cama"));
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

}