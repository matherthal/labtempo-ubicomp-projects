package br.uff.tempo.apps.map.dialogs;

import br.uff.tempo.R;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;

public class ChooseExternalResource extends MapDialog {
	
	public ChooseExternalResource(final Activity act) {
	
		super(act, R.layout.registered_resource_list, R.string.title_config_res);
		
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
			
				ListView list = (ListView) dialog.findViewById(R.id.list_registered);			
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		
	}

}
