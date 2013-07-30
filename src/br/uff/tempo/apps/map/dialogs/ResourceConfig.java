package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.objects.persistence.RegistryData;
import br.uff.tempo.middleware.e.SmartAndroidException;

public class ResourceConfig extends MapDialog {

	private IDialogFinishHandler handler;
	private String name = "";
	private float posX;
	private float posY;

	public ResourceConfig(final Activity act, final IDialogFinishHandler handler) {

		super(act, R.layout.resource_config, R.string.title_config_res);

		this.handler = handler;
		// Register
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
	
	public ResourceConfig(final Activity act) {
		this(act, null);
	}

	// When OK button is clicked
	public void btnOkClick(View v) {

		EditText etName = (EditText) dialog.findViewById(R.id.etName);
		EditText etPosX = (EditText) dialog.findViewById(R.id.etPosX);
		EditText etPosY = (EditText) dialog.findViewById(R.id.etPosY);

		this.name = etName.getText().toString();
		String pX = etPosX.getText().toString();
		String pY = etPosY.getText().toString();

		try {
			if (name.equals("") || pX.equals("") || pY.equals(""))
				throw new SmartAndroidException("Empty fields not allowed!");

			try {
				this.posX = Float.parseFloat(pX);
				this.posY = Float.parseFloat(pY);
			} catch (NumberFormatException e) {
				throw new SmartAndroidException(
						"Invalid floating-point number!", e);
			}

			dialog.cancel();
			
			if (handler != null) {
				handler.onDialogFinished(this.dialog);
			}

		} catch (SmartAndroidException e) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(activity, "Invalid values!",
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	// When Cancel button is clicked
	public void btnCancelClick(View v) {
		dialog.cancel();
	}

	public RegistryData getData() {
		
		RegistryData data = new RegistryData();
		data.setPositionX(posX);
		data.setPositionY(posY);
		data.setResourceName(name);
		
		return data;
	}
	
	public String getName() {

		return this.name;
	}

	public float getPositionX() {
		return this.posX;
	}

	public float getPositionY() {
		return this.posY;
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
