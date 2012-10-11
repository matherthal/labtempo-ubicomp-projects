package br.uff.tempo.apps.map.settings;

import br.uff.tempo.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MapSettings extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.map_prefs);
	}
}
