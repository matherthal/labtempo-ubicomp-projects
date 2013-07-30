package br.uff.tempo.apps.map.dialogs;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceData;

public class ChooseResource extends MapDialog implements
		AdapterView.OnItemClickListener {

	private ListView listView;
	private ArrayAdapter<String> lvAdapter;
	private List<ResourceData> dataFromResouces;
	private String[] listNames;
	private IChooser chooser;

	public ChooseResource(final Activity act, final IChooser chooser) {
		super(act, R.layout.registered_resource_list, R.string.title_config_res);

		this.chooser = chooser;
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				// Get the reference to the list view
				listView = (ListView) dialog.findViewById(R.id.list_registered);
				listView.setOnItemClickListener(ChooseResource.this);
				// Cancel the dialog if you touch outside its area.
				dialog.setCanceledOnTouchOutside(true);
			}
		});
	}
	
	public ChooseResource(final Activity act) {
		this(act, (IChooser) act);
	}

	public void showDialog(String[] list) {
		listNames = list;
		lvAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, list);

		listView.setAdapter(lvAdapter);
		super.showDialog();
	}

	public void showDialog(List<ResourceData> list) {
		dataFromResouces = formatList(list);
		String[] names = new String[dataFromResouces.size()];
		
		for (int i = 0; i < dataFromResouces.size(); i++) {
			ResourceData data = dataFromResouces.get(i);
			// Show in the list a custom tag, if it exits. Show the name otherwise
			names[i] = data.getTag() != null ? data.getTag() : data.getName();
		}
		showDialog(names);
	}

	public List<ResourceData> formatList(List<ResourceData> resList) {

		List<ResourceData> list = new ArrayList<ResourceData>();

		for (ResourceData data : resList) {
			if (!data.getName().contains("Resource")) {
				list.add(data);
			}
		}
		return list;
	}

	@Override
	public void onClick(View v) {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ChosenData data = new ChosenData();

		if (dataFromResouces != null) {
			data.setData(dataFromResouces.get(position));
		} else {
			data.setTag(listNames[position]);
		}
		
		chooser.onResourceChosen(data);
		dialog.cancel();
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		dataFromResouces = null;
		listNames = null;
	}
}