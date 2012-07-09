package br.uff.tempo.apps.rule;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Condition;
import br.uff.tempo.middleware.resources.Rule;
import br.uff.tempo.middleware.resources.Stove;

public class RuleActivity extends Activity {
	private static final String TAG = "RuleActivity";
	private Rule rule;
	private final String serverIP = "192.168.1.70";
	private IResourceDiscovery discovery;
	private ArrayList<ResourceAgent> ras;
	private ArrayList<Condition> conds = new ArrayList<Condition>();
	private ArrayAdapter<String> lvAdapter;
	private ArrayList<String> listItems = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule);
        
		//Binding RuleAgent
		Intent intent = new Intent(this, Rule.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		//Get Discovery Service
		if (rule != null) {
			discovery = rule.getRDS();
		}
    }

	/*
	 * Button call Condition List
	 */
	public void buttonConds_Clicked(View view) {
		Intent intent = new Intent(RuleActivity.this,
				RuleCondListActivity.class);
		startActivityForResult(intent, 1);
	}

	/*
	 * Button call Action List
	 */
	public void buttonActions_Clicked(View view) {
		Intent intent = new Intent(RuleActivity.this,
				RuleActionListActivity.class);
		startActivityForResult(intent, 1);
	}

	/*
	 * onActivityResult
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * Receives the result of activities called. For getting the conditions and actions, in case. 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// Condition cond =
				// (Condition)data.getSerializableExtra("COND");
				//String[] condStr = data.getStringArrayExtra("COND_LIST");
				
				//>>>>ArrayList<Condition> conds = (ArrayList<Condition>) Serialization.deserializeObject(data.getByteArrayExtra("COND_LIST"));
				//Condition cond = (Condition) Serialization.deserializeObject(data.getByteArrayExtra("COND")); //DEBUG
				
				//createCond(condStr[0], condStr[1], condStr[2], condStr[3]);
				Toast.makeText(this, "Novas Condi��es", Toast.LENGTH_SHORT)
						.show();
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	/*
	 * Creates a condition and adds to the list. Then a rule could be created after. 
	 */
	public void createCond(String raID, String attrib, String operation, String value) {
		//ResourceAgent ra = (ResourceAgent)discovery.search(s.getSelectedItem().toString()).get(0);
		ResourceAgent ra = new Stove();//FIXME: get it out of here, just for debug. The correct is above
    	
    	//Get attribute's acess method
		//Method mtd = null;
		//try {
			//mtd = ra.getClass().getMethod("get" + attrib);
			//mtd = ra.getClass().getMethod("getIsOn");
		//} catch (SecurityException e) {
		//	Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
		//	return;
		//} catch (NoSuchMethodException e) {
		//	Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
		//	return;
		//}
		String mtd = "get" + attrib;
		
    	Condition cond;
    	try {
    		//Initialize Condition
			cond = new Condition(ra, mtd, operation, value);
		} catch (Exception e) {
			Toast.makeText(this, "Erro ao criar a condição", Toast.LENGTH_LONG);
			return;
		} 
    	
    	//Add condition to list of conditions
    	conds.add(cond);
    	//Add condition to list in the view
    	listItems.add(cond.toString());
		lvAdapter.notifyDataSetChanged();
	}

	public void buttonCreateRule_Clicked(View view) {
		try {
			EditText et = (EditText) view.findViewById(R.id.editTextCreateRule);
			// FIXME: it's not this way that we create rules
			// rule.expression = et.getText().toString();

			boolean result = false;
			// result = rule.runScript(et.getText().toString());
			//result = rule.runScript("true||false&&true");
			TextView tv = (TextView) view.findViewById(R.id.textViewLog);
			tv.setText(result ? "ação disparada" : " ação não disparada");
			tv.append("\nRegra <" + rule.expression + "> "
					+ (result ? "ação disparada" : " ação não disparada"));

			if (rule != null)
				Toast.makeText(RuleActivity.this, "Regra sobreescrita",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(RuleActivity.this, "Erro ao criar a regra",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void buttonRunScript_Clicked(View view) {
		if (rule == null)
			Toast.makeText(RuleActivity.this,
					"É necessário criar a regra primeiro", Toast.LENGTH_SHORT)
					.show();
		else {
			boolean result = false;
			try {
				// FIXME: it's not this way that we run rules
				//result = rule.runScript(rule.expression);
				// } catch (EvalError e) {
				// Toast.makeText(RuleActivity.this, "Erro ao Executar a regra",
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(RuleActivity.this, "Erro ao Executar a regra",
						Toast.LENGTH_SHORT).show();
			}

			try {
				TextView tv = (TextView) view.findViewById(R.id.textViewLog);
				tv.append("\nRegra <" + rule.expression + "> "
						+ (result ? "ação disparada" : " ação não disparada"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName className, IBinder service) {
			ResourceBinder binder = (ResourceBinder) service;
			rule = (Rule) binder.getService();

			// Tell the user about this for our demo.
			Toast.makeText(RuleActivity.this, "Agente de Regras Conectado",
					Toast.LENGTH_SHORT).show();
		}

		
		public void onServiceDisconnected(ComponentName className) {
			rule = null;
			Toast.makeText(RuleActivity.this, "Agente de Regras Desconectado",
					Toast.LENGTH_SHORT).show();
		}
	};
}
