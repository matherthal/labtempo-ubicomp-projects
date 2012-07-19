package br.uff.tempo.apps.rule;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ComponentName;
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
import br.uff.tempo.middleware.resources.AlarmClock;
import br.uff.tempo.middleware.resources.Condition;
import br.uff.tempo.middleware.resources.Rule;
import br.uff.tempo.middleware.resources.Stove;

public class RuleActivity extends Activity {
	private static final String TAG = "RuleActivity";
	private Rule rule;
	private final String serverIP = "192.168.1.70";
	//private final String serverIP = "127.0.0.'1";
	private IResourceDiscovery discovery;
	private ArrayList<ResourceAgent> ras;
	private ArrayList<Condition> conds = new ArrayList<Condition>();
	private ArrayAdapter<String> lvAdapter;
	private ArrayList<String> listItems = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AlarmClock alarm = new AlarmClock(RuleActivity.this);
		Calendar start = Calendar.getInstance();
		start.add(Calendar.SECOND, 20);
		alarm.scheduleAlarm("my alarm", start);
//		Intent intent = new Intent(RuleActivity.this, AlarmClock.AlarmReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(RuleActivity.this, 001000, intent, 0);
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
		
		
//		NotificationManager manger = (NotificationManager) RuleActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.icon, "Wake up alarm", System.currentTimeMillis());
//		Intent intent = new Intent(RuleActivity.this, AlarmReceiver.class);
//		PendingIntent contentIntent = PendingIntent.getBroadcast(RuleActivity.this, 001000, intent, 0);
//		notification.setLatestEventInfo(RuleActivity.this, "Context Title", "Context text", contentIntent);
//		notification.flags = Notification.FLAG_INSISTENT;
//
//		notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
//		notification.vibrate = (long[]) intent.getExtras().get("vibrationPatern");
//
//		// The PendingIntent to launch our activity if the user selects this notification
//		manger.notify(NOTIFICATION_ID, notification);
		
		/*
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		String rai = discovery.search("Stove").get(0);
		
		RuleInterpreter rule = new RuleInterpreter();
		try {
			rule.setCondition(rai, "getOvenTemperature", null, Operator.GreaterThan, "50.0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rule.identify();
		
		RuleInterpreterTest test = new RuleInterpreterTest();
		test.identify();
		//rule.registerStakeholder("Regra disparada", test.getURL());
		
		IStove stove = new StoveStub(rai);
		stove.setOvenTemperature(76.0f);*/
	}
	
	/*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //>>>setContentView(R.layout.rule);
        
        //Initialize Condition's ListView
    	//ListView lv = (ListView) findViewById(R.id.listViewConds);
    	//listItems.add("Nenhuma condição");
		//lvAdapter = new ArrayAdapter<String>(this,
		//		android.R.layout.simple_list_item_1, listItems);
		//lv.setAdapter(lvAdapter);

		//Binding RuleAgent
		//Intent intent = new Intent(this, Rule.class);
		//bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		//Get Discovery Service
		//if (rule != null)
		//	discovery = rule.getRDS();
		
		/
		 * Teste
		 
//        Log.d(TAG, "Teste inicio");
        Toast.makeText(this, "Teste inicio", Toast.LENGTH_SHORT).show();
        //Exemplo de agregação, interpretação e criação
		//intent = new Intent(this, Stove.class);
		//bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Stove s = new Stove("Fogao Querido");
		//IResourceDiscovery discover = ResourceAgent.getRDS();
		//IResourceDiscovery discover = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
//		Log.d(TAG, "search");
//		String rai = discover.search("Stove").get(0);
		String rai = String.valueOf(s.getId());
//		Log.d(TAG, "rai = " + rai);
//		IStove stove = new StoveStub(rai);
//		Log.d(TAG, "stove = " + stove.getId());
		
		int nextStep = 0; // this variable will not be reached from within the onClick-methods

//		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//		    @Override
//		    public void onClick(DialogInterface dialog, int which) {
//		        
//		    }
//		};
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		Interpreter intp = new Interpreter();
		Log.d(TAG, "interpretador = " + intp.getId());
		intp.setName("Perigo Vazamento de Gás");
		Toast.makeText(this, "Regra: Perigo Vazamento de Gás", Toast.LENGTH_SHORT).show();
//		builder.setMessage("Regra: Perigo Vazamento de Gás").setNeutralButton("ok", dialogClickListener).show();
		
		//Set Context Variable
		String cvName = "Vazamento de gás";
		intp.setContextVariable(s, cvName);
		String str = "Condição\nVariável de Contexto: " + cvName;
		
		//Set conditional results
		float cte = 0.7f;
		String res = "Emergência!";
		intp.setConditionalResult(Operator.GreaterThanOrEqual, cte, res);
		str += "\n  " + res + ", se " + cvName + " >= " + cte;
		
		cte = 0.4f;
		res = "Alerta";
		intp.setConditionalResult(Operator.GreaterThanOrEqual, cte, res);
		str += "\n  " + res + ", se " + cvName + " >= " + cte;
		
		cte = 0.4f;
		res = "Normal";
		intp.setConditionalResult(Operator.LessThan, cte, res);
		str += "\n  " + res + ", se " + cvName + " < " + cte;
		
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
//		builder.setMessage(str).setNeutralButton("ok", dialogClickListener).show();
		
		
		//Interpretation
		res = intp.interpretToString();
		Log.d(TAG, "Interpretation: " + res);
		Toast.makeText(this, "Interpretação: " + res, Toast.LENGTH_LONG).show();
//		builder.setMessage("Interpretation: " + res).setNeutralButton("ok", dialogClickListener).show();
    }*/

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
				Toast.makeText(this, "Novas Condições", Toast.LENGTH_SHORT)
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
			cond = new Condition(ra, mtd, null, operation, value, 0);
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
