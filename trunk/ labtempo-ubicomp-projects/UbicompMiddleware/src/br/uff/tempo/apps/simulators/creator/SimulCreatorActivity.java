package br.uff.tempo.apps.simulators.creator;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChoosedData;
import br.uff.tempo.apps.map.dialogs.IChooser;
import br.uff.tempo.apps.map.dialogs.IDialogFinishHandler;
import br.uff.tempo.apps.map.dialogs.IListGetter;
import br.uff.tempo.apps.simulators.utils.Creator;
import br.uff.tempo.middleware.management.ResourceData;

public class SimulCreatorActivity extends Activity implements IChooser, IListGetter, IDialogFinishHandler {

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
	public void onRegisteredResourceChoosed(ChoosedData choosedData) {
		creator.onRegisteredResourceChoosed(choosedData);
	}

	@Override
	public void onGetList(List<ResourceData> result) {
		creator.onGetList(result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.simul_creator, menu);
		return true;
	}

	@Override
	public void onDialogFinished(Dialog dialog) {
		creator.onDialogFinished(dialog);		
	}
}
