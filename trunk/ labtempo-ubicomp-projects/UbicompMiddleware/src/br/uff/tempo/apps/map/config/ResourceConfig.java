package br.uff.tempo.apps.map.config;

import android.app.Activity;
import android.app.Dialog;
import android.widget.EditText;
import br.uff.tempo.R;

public class ResourceConfig {

	private Dialog dialog;
	private Activity activity;

	public ResourceConfig(final Activity act) {

		this.activity = act;
		
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
			
				ResourceConfig.this.dialog = new Dialog(act);

				ResourceConfig.this.dialog.setContentView(R.layout.resource_config);
				ResourceConfig.this.dialog.setTitle(R.string.title_config_res);
			}
		});
	}
	
	public void showPopup() {
		
		this.dialog.show();
	}
	
	public String getName() {
		
		EditText edit = (EditText) activity.findViewById(R.id.etName);
		
		return edit.getText().toString();
	}

}
