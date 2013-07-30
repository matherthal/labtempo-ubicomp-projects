//
// Settings.java
//

package br.uff.tempo.apps.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import br.uff.tempo.R;

public class Settings extends PreferenceActivity
  implements OnSharedPreferenceChangeListener {

  //
  // PreferenceActivity interface
  //

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.addPreferencesFromResource(R.xml.settings);
    PreferenceScreen screen = this.getPreferenceScreen();
    Context context = screen.getContext();
    Settings.typePlayerXKey = context.getString(R.string.type_player_x_key);
    Settings.typePlayerOKey = context.getString(R.string.type_player_o_key);
    this.typePlayerX =
      (ListPreference)screen.findPreference(Settings.typePlayerXKey);
    this.typePlayerO =
      (ListPreference)screen.findPreference(Settings.typePlayerOKey);
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.typePlayerX.setSummary(this.typePlayerX.getEntry().toString());
    this.typePlayerO.setSummary(this.typePlayerO.getEntry().toString());
    this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  //
  // OnSharedPreferenceChangeListener interface
  //

  //@Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                        String key) {
    if (key.equals(Settings.typePlayerXKey)) {
      this.typePlayerX.setSummary(this.typePlayerX.getEntry().toString()); 
    }
    else if (key.equals(Settings.typePlayerOKey)) {
      this.typePlayerO.setSummary(this.typePlayerO.getEntry().toString()); 
    }
  }

  //
  // Additional methods
  //

  public static Player.Type getPlayerType(Context context, Player.Name name) {
    String key;
    String def;
    switch (name) {
      case X:
        key = Settings.typePlayerXKey;
        def = context.getString(R.string.type_player_x_default);
        break;
      case O:
      default:
        key = Settings.typePlayerOKey;
        def = context.getString(R.string.type_player_o_default);
        break;
    }
    SharedPreferences prefs =
      PreferenceManager.getDefaultSharedPreferences(context);
    String type = prefs.getString(key, def);
    if (type.equals(Player.Type.Human.toString())) {
      return Player.Type.Human;
    }
    else {
      return Player.Type.Computer;
    }
  }

  //
  // Attributes
  //

  private static String typePlayerXKey;

  private static String typePlayerOKey;

  private ListPreference typePlayerX;

  private ListPreference typePlayerO;

}
