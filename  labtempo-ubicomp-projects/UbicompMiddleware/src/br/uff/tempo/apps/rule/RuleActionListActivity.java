package br.uff.tempo.apps.rule;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.Action;

public class RuleActionListActivity extends Activity {
	private ArrayList<Action> actions = new ArrayList<Action>();
	private ArrayAdapter<String> lvAdapter;
	private ArrayList<String> listItems = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_action_list);

		// Initialize Action's ListView
		ListView lv = (ListView) findViewById(R.id.listViewActions);
		listItems.add("Bed.b01.setTemperature(20)");
		listItems.add("Stove.s01.turnOn()");
		lvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		lv.setAdapter(lvAdapter);
	}

	/*
	 * Button Add Condition Calls the activity for creation of conditions
	 */
	public void buttonAddAction_Clicked(View view) {
		Intent intent = new Intent(RuleActionListActivity.this, RuleActionActivity.class);
		startActivityForResult(intent, 1);
	}

	public void buttonRmAction_Clicked(View view) {

	}

	public void buttonFinish_Clicked(View view) {
		finish();
	}
}
