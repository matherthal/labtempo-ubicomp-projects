package br.uff.tempo.apps.map.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.ChosenData;
import br.uff.tempo.apps.map.dialogs.IChooser;
import br.uff.tempo.apps.map.dialogs.IListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public class MapSettings extends PreferenceActivity implements IListGetter, IChooser {
	
	public static int OP_UNREG = 0;
	public static int OP_SETUP = 1;
	public static int OP_STAKEHOLDER = 2;
	
	private Preference resPref;
	private Preference stakeholderPref;
	private ChooseResource chooseResourceDialog;
	private ChooseResource stakeholderDialog;
	private List<ResourceData> list;
	private static String rdsAddress;
	private int op;
	private List<Stakeholder> stakeholders;
	private ResourceAgentStub current; 
	private Map<String, String> stakeholderNames;
	
	@Override
	@SuppressWarnings("all")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.map_prefs);
		
		// Get the references to all registered resources
		new MiddlewareOperation(this, this, "//", IResourceDiscovery.rans).execute(null);
		
		chooseResourceDialog = new ChooseResource(this);
		stakeholderDialog = new ChooseResource(this);
		
		stakeholderNames = new HashMap<String, String>();
		
		resPref = findPreference("regResources");
		resPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				return unregisterResourceClick();
			}
		});
		
		stakeholderPref = findPreference("resStakeholders");
		stakeholderPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				return setupStakeholdersClick();
			}
		});
	}

	public boolean unregisterResourceClick() {
		
		op = OP_UNREG;
		chooseResourceDialog.showDialog(list);
		return false;
	}
	
	public boolean setupStakeholdersClick() {
		
		op = OP_SETUP;
		chooseResourceDialog.showDialog(list);
		return false;
	}
	
	@Override
	@SuppressWarnings("all")
	public void onResourceChosen(ChosenData choosedData) {
		
		ResourceData resource = choosedData.getData();
		
		// Unregister an Agent
		if (op == OP_UNREG) {
			
			new ResourceAgentStub(resource.getRans()).unregister();
			new MiddlewareOperation(this, this, "//", IResourceDiscovery.rans).execute(null);
			
		// Setup the stakeholders from an Agent
		} else if (op == OP_SETUP){
		
			current = new ResourceAgentStub(resource.getRans());
			stakeholders = current.getStakeholders();
			List<ResourceData> shData = new ArrayList<ResourceData>();
			
			IResourceDiscovery resData = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			
			for (Stakeholder s : stakeholders) {
				ResourceData d = resData.search(ResourceData.RANS, s.getRANS()).get(0);
				d.setTag(d.getName() + " wants " + s.getContextVariable());
				
				shData.add(d);
				stakeholderNames.put(d.getName(), s.getContextVariable());
			}
			
			op = OP_STAKEHOLDER;
			chooseResourceDialog.dismiss();
			stakeholderDialog.showDialog(shData);
		
		// A stakeholder was selected
		} else if (op == OP_STAKEHOLDER) {
			
			current.removeStakeholder(stakeholderNames.get(resource.getName()), resource.getRans());
			stakeholderNames.clear();
		}
	}
	
	// Called when a middleware operation finishes
	// returning the list of resources from ResitryService
	@Override
	public void onGetList(List<ResourceData> result) {
		
		list = result;
	}

	public static void setAddress(String address) {
		rdsAddress = address;
	}
}
