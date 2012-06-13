package br.uff.tempo.apps.rule;

import br.uff.tempo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class RuleActionActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_action);
	}
	
	public void buttonCreateAction_Clicked(View view) {
		finish();
	}
}
