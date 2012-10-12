package br.uff.tempo.apps.map.settings;

import java.util.List;

import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.IResourceListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class MapSettings extends PreferenceActivity implements IResourceListGetter {
	
	private ListPreference list;
	private MiddlewareOperation operation;
	private static String rdsAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.map_prefs);
		
		operation = new MiddlewareOperation(this, "", getRDSAddress());
		operation.execute(null);
		
		list = (ListPreference) findPreference("regResources");
	}

	@Override
	public void onGetResourceList(List<String> result) {
	
		CharSequence[] seq = new CharSequence[result.size()];
		CharSequence[] values = new CharSequence[result.size()];
		
		int i = 0;
		for (String rai : result) {
			seq[i] = rai;
			values[i] = rai;
			i++;
		}
		
		list.setEntries(seq);
		list.setEntryValues(values);
	}

	public static void setAddress(String address) {
		rdsAddress = address;
	}
	
	public static String getRDSAddress() {
		return "rai:" + rdsAddress + "//" + IResourceDiscovery.RDS_NAME;
	}
}
