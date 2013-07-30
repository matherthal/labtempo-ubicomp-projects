package br.uff.tempo.apps.simulators.creator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.utils.Creator;

public class SimulCreatorActivity extends Activity {

	private Button btnChoose;
	private Button btnCreate;
	private Creator creator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simul_creator);

		btnChoose = (Button) findViewById(R.id.btnChooseResource);
		btnCreate = (Button) findViewById(R.id.btnAddResource);

		creator = new Creator(this);

		btnChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { creator.chooseResource(); }
		});

		btnCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { creator.createResource(); }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.simul_creator, menu);
		return true;
	}
}
