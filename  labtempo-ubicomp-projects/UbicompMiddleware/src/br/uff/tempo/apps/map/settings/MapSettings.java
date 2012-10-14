package br.uff.tempo.apps.map.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.apps.map.dialogs.IResourceListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public class MapSettings extends PreferenceActivity implements IResourceListGetter, IResourceChooser {
	
	public static int OP_UNREG = 0;
	public static int OP_SETUP = 1;
	public static int OP_STAKEHOLDER = 2;
	
	private Preference resPref;
	private Preference stakeholderPref;
	private ChooseResource choose;
	private ChooseResource sHolder;
	private List<String> list;
	private static String rdsAddress;
	private int op;
	private String raiRegister;
	private IResourceRegister reg = null;
	private List<Stakeholder> stakeholders;
	private ResourceAgentStub current; 
	private Map<String, String> sHolderNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.map_prefs);
		
		// Get the references to all registered resources
		new MiddlewareOperation(this, "", getRDSAddress()).execute(null);
		
		choose = new ChooseResource(this);
		sHolder = new ChooseResource(this);
		
		sHolderNames = new HashMap<String, String>();
		
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
		choose.showDialog(list);
		return false;
	}
	
	public boolean setupStakeholdersClick() {
		
		op = OP_SETUP;
		choose.showDialog(list);
		return false;
	}
	
	@Override
	public void onRegisteredResourceChoosed(String resource) {
		
		// Unregister an Agent
		if (op == OP_UNREG) {
			
			if (reg == null) {
				reg = new ResourceRegisterStub(raiRegister);
			}
			
			reg.unregister(resource);
			new MiddlewareOperation(this, "", getRDSAddress()).execute(null);
			
		// Setup the stakeholders from an Agent
		} else if (op == OP_SETUP){
		
			current = new ResourceAgentStub(resource);
			stakeholders = current.getStakeholders();
			List<String> shStrings = new ArrayList<String>();
			
			for (Stakeholder s : stakeholders) {
				shStrings.add(s.toString());
				sHolderNames.put(s.getName(), s.getRAI());
			}
			
			op = OP_STAKEHOLDER;
			choose.dismiss();
			sHolder.showDialog(shStrings, false);
		
		// A stakeholder was selected
		} else if (op == OP_STAKEHOLDER) {
			
			String[] parsed = resource.split(" wants: ");
			current.removeStakeholder(parsed[1], sHolderNames.get(parsed[0]));
		}
	}

	@Override
	public void onDialogFinished(Dialog dialog) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onGetResourceList(List<String> result) {
		list = result;
		
		raiRegister = null;
		
		// Save the ResourceRegister RAI to avoid calling a remote method
		for (String rai : list) {
			if (rai.contains("ResourceRegister")) {
				raiRegister = rai;
				break;
			}
		}
	}

	public static void setAddress(String address) {
		rdsAddress = address;
	}
	
	public static String getRDSAddress() {
		return "rai:" + rdsAddress + "//" + IResourceDiscovery.RDS_NAME;
	}
}
