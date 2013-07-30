package br.uff.tempo.apps.rule;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Condition;
import br.uff.tempo.middleware.resources.Rule;
import br.uff.tempo.middleware.resources.Stove;

public class RuleConditionActivity extends Activity {
	private static final String TAG = "RuleActivityCond";
	private final String serverIP = "192.168.1.70";
	private ResourceDiscoveryStub discovery;
	private ArrayList<ResourceAgent> ras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_condition);
		Intent intent = new Intent(this, Rule.class);

		discovery = new ResourceDiscoveryStub(serverIP);
		populateClasses();

		final Spinner spinner = (Spinner) findViewById(R.id.spinnerClasseChoice);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String type = spinner.getSelectedItem().toString();
				// Populate other spinners after the selection
				populateRA(type);
				populateAttrib(type);
				// Enable spinners
				Spinner s = (Spinner) findViewById(R.id.spinnerRAChoice);
				s.setClickable(true);
				s = (Spinner) findViewById(R.id.spinnerRAAttribChoice);
				s.setClickable(true);
			}

			// @Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		// Initialize Operator's Spinner
		// Create adapter to fill operation spinner
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.add("=  igual");
		adapter.add("!= diferente");
		adapter.add(">  maior que");
		adapter.add("<  menor que");
		adapter.add(">= maior ou iqual a");
		adapter.add("<= menor ou iqual a");
		// Setting the adapter to the spinner
		Spinner s = (Spinner) findViewById(R.id.spinnerOperation);
		s.setAdapter(adapter);
	}

	private void populateClasses() {
		// Create adapter to spinner of RA's classes
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);

		/*
		 * FIXME: implement discovery //Get all RAs ArrayList<ResourceAgent> ras
		 * = discovery.search(""); //Iterator over the RAs to fill the spinner
		 * Iterator<ResourceAgent> itr = ras.iterator(); ResourceAgent ra; while
		 * (itr.hasNext()) { ra = itr.next(); //If the RA class already exists
		 * in the spinner, don't add this again if (!ras.contains(ra.getType()))
		 * adapter.add(ra.getType()); }
		 */

		adapter.add("Alarm");
		adapter.add("Bed");
		adapter.add("Person");
		adapter.add("Television");
		adapter.add("Stove");

		// Setting the adapter to the spinner
		Spinner s = (Spinner) findViewById(R.id.spinnerClasseChoice);
		s.setAdapter(adapter);
	}

	private void populateRA(String type) {
		/*
		 * ArrayAdapter<CharSequence> adapter = new
		 * ArrayAdapter<CharSequence>(this,
		 * android.R.layout.simple_spinner_item); //Iterator over the RAs to
		 * fill the spinner Iterator<ResourceAgent> itr = ras.iterator();
		 * ResourceAgent ra; while (itr.hasNext()) { ra = itr.next(); if
		 * (ra.getType().equals(type)) adapter.add(ra.getName()); } //Setting
		 * the adapter to the spinner of the RA's names Spinner s = (Spinner)
		 * findViewById(R.id.spinnerRAChoice); s.setAdapter(adapter);
		 */
	}

	private void populateAttrib(String type) {
		// FIXME: implement this correctly
		// TODO: must get the methods from the interface that get the CVs. These
		// methods also have to return the name of the CV
		// PLACEHOLDER:
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		if (type.equals("Stove")) {
			adapter.add("Está ligado");
			adapter.add("Temperatura da boca 1");
			adapter.add("Temperatura do forno");
		} else if (type.equals("Bed")) {
			adapter.add("Tem alguém deitado");
			adapter.add("Temperatura");
		} else if (type.equals("Alarm")) {
			adapter.add("Tem alarme cadastrado");
		} else if (type.equals("Television")) {
			adapter.add("Está ligada");
			adapter.add("Tem alguém assistindo");
		} else if (type.equals("Person")) {
			adapter.add("Dormindo");
			adapter.add("Descansando");
			adapter.add("Comendo");
			adapter.add("Andando");
			adapter.add("Fazendo atividade doméstica");
		}

		Spinner s = (Spinner) findViewById(R.id.spinnerRAAttribChoice);
		s.setAdapter(adapter);

		/*
		 * ArrayAdapter<CharSequence> adapter = new
		 * ArrayAdapter<CharSequence>(this,
		 * android.R.layout.simple_spinner_item); List<Tuple<String, Method>>
		 * attribs; try { attribs = ra.getAttribs(); Iterator itr =
		 * attribs.iterator(); while(itr.hasNext()) { Tuple<String, Method> tp =
		 * (Tuple<String, Method>) itr.next(); adapter.add(tp.key); } //Setting
		 * the adapter to the spinner of the RA's attributes Spinner s =
		 * (Spinner) findViewById(R.id.spinnerRAAttribChoice);
		 * s.setAdapter(adapter); } catch (SecurityException e) {
		 * e.printStackTrace(); //TODO: deal with it } catch
		 * (NoSuchMethodException e) { e.printStackTrace();//TODO: deal with it
		 * }
		 */
	}

	public void buttonCreateCond_Clicked(View view) {
		// Get RA
		Spinner spn = (Spinner) findViewById(R.id.spinnerRAChoice);
		// (ResourceAgent)discovery.search(s.getSelectedItem().toString()).get(0);

		// Get attribute's acess method
		spn = (Spinner) findViewById(R.id.spinnerRAAttribChoice);

		// Get attribute's acess method
		// Method mtd = null;
		// try {
		// mtd = ra.getClass().getMethod("get" + attrib);
		// mtd = ra.getClass().getMethod("getIsOn");
		// } catch (SecurityException e) {
		// Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
		// return;
		// } catch (NoSuchMethodException e) {
		// Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
		// return;
		// }
		String mtd = "getIsOn";

		// Get operation
		spn = (Spinner) findViewById(R.id.spinnerOperation);
		String op = spn.getSelectedItem().toString();
		
		// Get value to be compared
		TextView tv = (TextView) findViewById(R.id.editTextValue);
		String value = tv.getText().toString();

		Condition cond = null;
		try {
			// Initialize Condition
			// cond = new Condition(ra.getRAI(), mtd, null,
			// Operator.valueOf(op), value, 0);
		} catch (Exception e) {
			Toast.makeText(this, "Erro ao criar a condição", Toast.LENGTH_LONG);
			return;
		}

		byte[] condRaw = Serialization.serializeObject(cond);

		// Send Condition back
		Intent data = this.getIntent();
		data.putExtra("COND", condRaw);
		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
	}
}
