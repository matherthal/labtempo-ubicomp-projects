package br.uff.tempo.apps.rule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import br.uff.tempo.R;

public class RuleActionActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_action);
	}

	public void buttonCreateAction_Clicked(View view) {
		finish();
	}
}
