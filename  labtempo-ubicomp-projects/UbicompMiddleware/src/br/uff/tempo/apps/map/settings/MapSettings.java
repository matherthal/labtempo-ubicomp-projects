package br.uff.tempo.apps.map.settings;

import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.apps.map.dialogs.IResourceListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;

public class MapSettings extends PreferenceActivity implements IResourceListGetter, IResourceChooser {
	
	private Preference resPref;
	private Preference stakeholderPref;
	private ChooseResource choose;
	private List<String> list;
	private static String rdsAddress;
	private boolean unregister = false;
	private String raiRegister;
	private IResourceRegister reg = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.map_prefs);
		
		// Get the references to all registered resources
		new MiddlewareOperation(this, "", getRDSAddress()).execute(null);
		
		choose = new ChooseResource(this);
		
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
		
		unregister = true;
		choose.showDialog(list);
		return false;
	}
	
	public boolean setupStakeholdersClick() {
		
		unregister = false;
		return false;
	}
	
	@Override
	public void onRegisteredResourceChoosed(String resourceRAI) {
		
		//ResourceAgentStub res = new ResourceAgentStub(resourceRAI);
		
		if (unregister) {
			
			if (reg == null) {
				reg = new ResourceRegisterStub(raiRegister);
			}
			
			reg.unregister(resourceRAI);
			new MiddlewareOperation(this, "", getRDSAddress()).execute(null);
			
		} else {
			
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
