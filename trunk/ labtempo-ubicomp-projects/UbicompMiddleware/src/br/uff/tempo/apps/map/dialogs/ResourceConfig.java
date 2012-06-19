package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.uff.tempo.R;

public class ResourceConfig extends MapDialog {

	private String name = "";

	public ResourceConfig(final Activity act) {

		super(act, R.layout.resource_config, R.string.title_config_res);

		//Register
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				Button ok = (Button) dialog.findViewById(R.id.btnOk);
				Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

				ok.setOnClickListener(ResourceConfig.this);
				cancel.setOnClickListener(ResourceConfig.this);
			}
		});

	}

	// When OK button is clicked
	public void btnOkClick(View v) {

		EditText edit = (EditText) dialog.findViewById(R.id.etName);
		
		this.name = edit.getText().toString();
		
		if (name.equals("")) {

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(activity, "Enter a non-empty name!",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {

			dialog.cancel();
			activity.onDialogFinished(this.dialog);
		}
	}

	// When Cancel button is clicked
	public void btnCancelClick(View v) {
		
		dialog.cancel();
	}

	public String getName() {

		return this.name;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnOk:

			btnOkClick(v);
			break;

		case R.id.btnCancel:

			btnCancelClick(v);
			break;

		default:
			break;
		}
	}

}
