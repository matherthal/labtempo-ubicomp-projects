package br.uff.tempo.apps.map.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.MapActivity;

public class ChooseExternalResource extends MapDialog implements AdapterView.OnItemClickListener {

	private ListView list;
	private ArrayAdapter<String> lvAdapter;
	private List<String> resourcesRAI;

	public ChooseExternalResource(final Activity act) {

		super(act, R.layout.registered_resource_list, R.string.title_config_res);

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				// Get the reference to the list view
				list = (ListView) dialog.findViewById(R.id.list_registered);
				list.setOnItemClickListener(ChooseExternalResource.this);

				// Cancel the dialog if you touch outside its area.
				dialog.setCanceledOnTouchOutside(true);
			}
		});
	}

	public void showDialog(List<String> resList) {

		resourcesRAI = resList;

		lvAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, formatList(resList));

		list.setAdapter(lvAdapter);

		super.showDialog();
	}

	public List<String> formatList(List<String> resList) {

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		String resourceRAI = resourcesRAI.get(position);
		
		IResourceChooser chooser = (IResourceChooser) activity;

		chooser.onRegisteredResourceChoosed(resourceRAI);

		dialog.cancel();

	}

}
