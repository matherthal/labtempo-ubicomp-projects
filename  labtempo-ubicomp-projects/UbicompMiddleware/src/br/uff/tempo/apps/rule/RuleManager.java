package br.uff.tempo.apps.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;

public class RuleManager extends Activity {
	private IResourceDiscovery discovery;

	public List<IRuleInterpreter> riList = new ArrayList<IRuleInterpreter>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_manager);
		
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		List<ResourceData> rules = discovery.search(ResourceData.TYPE, ResourceAgent.type(RuleInterpreter.class));
		if (rules.size() != 0)
			for (ResourceData r : rules)
				riList.add(new RuleInterpreterStub(r.getRans()));
		else {
			Toast.makeText(this, "Nenhuma regra encontrada", Toast.LENGTH_SHORT).show();
			return;
		}

		ListView lvRules = (ListView) findViewById(R.id.lv_rules);

		OnItemClickListener itemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lv, View item, int position, long id) {
				ListView lView = (ListView) lv;
				SimpleAdapter adapter = (SimpleAdapter) lView.getAdapter();
				HashMap<String, Object> hm = (HashMap) adapter.getItem(position);
				/** The clicked Item in the ListView */
				RelativeLayout rLayout = (RelativeLayout) item;
				/** Getting the toggle button corresponding to the clicked item */
				ToggleButton tgl = (ToggleButton) rLayout.getChildAt(1);

				String strStatus = "";
				if (tgl.isChecked()) {
					tgl.setChecked(false);
					strStatus = "Off";
					riList.get(position).stop();
				} else {
					tgl.setChecked(true);
					strStatus = "On";
					riList.get(position).start();
				}
				Toast.makeText(getBaseContext(), (String) hm.get("txt") + " : " + strStatus,
						Toast.LENGTH_SHORT).show();
			}
		};

		lvRules.setOnItemClickListener(itemClickListener);
		// Each row in the list stores country name and its status
		List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
		List<IRuleInterpreter> ruleList = riList.size() > 9 ? riList.subList(0, 9) : riList;
		Iterator<IRuleInterpreter> it = ruleList.iterator();
		while (it.hasNext()) {
			IRuleInterpreter ri = it.next();
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("txt", ri.getName());
			hm.put("stat", ri.isStarted());
			aList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { "txt", "stat" };
		// Ids of views in listview_layout
		int[] to = { R.id.tv_item, R.id.tgl_status };
		// Instantiating an adapter to store each items
		// R.layout.rule_manager_list_item defines the layout of each item
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.rule_manager_list_item, from, to);
		lvRules.setAdapter(adapter);
	}

	/**
	 * Saving the current state of the activity for configuration changes [
	 * Portrait <=> Landscape ]
	 */
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putBooleanArray("status", status);
//
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rule_manager, menu);
		return true;
	}
}
