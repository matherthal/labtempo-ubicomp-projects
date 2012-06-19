package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.MapActivity;

public class ResourceConfig implements OnClickListener, OnCancelListener {

	private Dialog dialog;
	private MapActivity activity;
	private String name = "";
	private boolean finished;

	public ResourceConfig(final Activity act) {

		this.activity = (MapActivity) act;

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				dialog = new Dialog(act);

				dialog.setContentView(R.layout.resource_config);
				dialog.setTitle(R.string.title_config_res);

				Button ok = (Button) dialog.findViewById(R.id.btnOk);
				Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

				ok.setOnClickListener(ResourceConfig.this);
				cancel.setOnClickListener(ResourceConfig.this);
				
				dialog.setOnCancelListener(ResourceConfig.this);
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
	
	public boolean isFinished() {
		
		return this.finished;
	}

	// When Cancel button is clicked
	public void btnCancelClick(View v) {
		
		dialog.cancel();
	}

	public void showPopup() {
		
		this.dialog.show();
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

	@Override
	public void onCancel(DialogInterface dialog) {
		
		finished = true;
	}

}
