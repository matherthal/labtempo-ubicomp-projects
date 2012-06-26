package br.uff.tempo.apps.map.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.uff.tempo.R;

public class ChooseExternalResource extends MapDialog {
	
	private ListView list;
	private ArrayAdapter<String> lvAdapter;
	
	public ChooseExternalResource(final Activity act) {
	
		super(act, R.layout.registered_resource_list, R.string.title_config_res);
		
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
			
				// Get the reference to the list view
				list = (ListView) dialog.findViewById(R.id.list_registered);
				
				//Cancel the dialog if you touch outside its area.
				dialog.setCanceledOnTouchOutside(true);
			}
		});
	}

	public void showDialog(List<String> resList) {

		lvAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, formatList(resList));
		
		list.setAdapter(lvAdapter);
		
		super.showDialog();
	}
	
	private List<String> formatList(List<String> resList) {
		
		List<String> list = new ArrayList<String>();
		
		for (String s : resList) {
			
			if (!s.contains("management")) {
				
				String[] names = s.split(":");
				list.add(names[2]);
			}
		}
		
		return list;
	}

	@Override
	public void onClick(View v) {
		
		
	}

}
