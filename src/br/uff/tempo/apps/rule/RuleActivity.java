package br.uff.tempo.apps.rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.utils.datastructure.AbstractVisitor;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Condition;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.Rule;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.IBed;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;
import br.uff.tempo.middleware.resources.stubs.BedStub;
import br.uff.tempo.middleware.resources.stubs.StoveStub;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class RuleActivity extends Activity {
	private static final String TAG = "RuleActivity";
	private Rule rule;
	private IResourceDiscovery discovery;
	private ArrayList<ResourceAgent> ras;
	private ArrayList<Condition> conds = new ArrayList<Condition>();
	private ArrayAdapter<String> lvAdapter;
	private ArrayList<String> listItems = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Toast.makeText(this, "Regra Inicializada", Toast.LENGTH_SHORT).show();
			
			//Teste subscrição
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			String raiB = discovery.search(ResourceData.TYPE, ResourceAgent.type(Bed.class)).get(0).getRans();
			IResourceAgent ra = new ResourceAgentStub(raiB);
			new Generic("SubscTest", "SubscTest", ra, "occupied") {
				private static final String TAG = "SubscTest";
				
				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.i(this.TAG, "SUBSCRIÇÃO FUNCIONOU!!!");
				}
			};

			RuleInterpreter ri = new RuleInterpreter("RegraFogaoEsquecido", "RegraFogaoEsquecido");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(this.getAssets().open("interpreter.json")));

				String line;
				StringBuilder buffer = new StringBuilder();
				while ((line = in.readLine()) != null)
					buffer.append(line).append('\n');

				ri.setExpression(buffer.toString());
				ri.identify();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			new Generic("Stove Urgency Action", "Stove Urgency Action", (IResourceAgent) ri, RuleInterpreter.RULE_TRIGGERED) {
				private static final long serialVersionUID = 1L;
				private static final String TAG = "Action Stove";
				private int counter = 0;

////				String tvRai = discovery.search("Television").get(0);
//				String tvRai = discovery.searchForAttribute(ResourceData.TYPE, ResourceAgent.type(Television.class)).get(0).getRai();
////				String tvRai = discovery.searchForAttribute(ResourceData.TYPE, ResourceAgent.type(Television.class)).get(0).getName();
//				private ITelevision tv = new TelevisionStub(tvRai);

				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.i(TAG, "Mostrou mensagem na televisão: Temp fogão > 50");
					Log.i(TAG, "CHANGE: " + rai + " " + method + " " + value);
//					Message msg = mHandler.obtainMessage();
//					msg.obj = counter++;
//					mHandler.sendMessage(msg);
//					mHandler.post(mUpdateResults);
//
//					RuleActivity.this.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							tv.showMessage("Temp fogao > 50");
//						}
//					});
				}
			};
			
			// FIXME
			if (1 != 2)
				return;

			// discovery = new
			// ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
			// ArrayList<String> stoves = discovery.search("Stove");
			// String raiStove = stoves.get(0);
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			List<ResourceData> stoves = discovery.search(ResourceData.TYPE, ResourceAgent.type(Stove.class));
			String raiStove = stoves.get(0).getRans();
//			String raiStove = discovery.searchForAttribute(ResourceData.TYPE, ResourceAgent.type(Stove.class)).get(0).getRai();

			Toast.makeText(RuleActivity.this, "Fogão encontrado", Toast.LENGTH_SHORT).show();
			// this.runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// Toast.makeText(RuleActivity.this, "Fogão encontrado",
			// Toast.LENGTH_SHORT).show();
			// }
			// });

			// discovery = new
			// ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
			// String raiBed = discovery.search("Bed").get(0);
			discovery = new ResourceDiscoveryStub(
					IResourceDiscovery.rans);
			String raiBed = discovery.search(ResourceData.TYPE, ResourceAgent.type(Bed.class)).get(0).getRans();
//			String raiBed = discovery.searchForAttribute(ResourceData.TYPE, "br.uff.tempo.middleware.resources.Bed").get(0).getRai();

			Toast.makeText(RuleActivity.this, "Cama encontrada", Toast.LENGTH_SHORT).show();
			// this.runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// Toast.makeText(RuleActivity.this, "Cama encontrada",
			// Toast.LENGTH_SHORT).show();
			// }
			// });

			RuleInterpreter rule = new RuleInterpreter("RegradoFogao", "RegradoFogao");
			try {
//				rule.setCondition(raiStove, "getOvenTemperature", null, Operator.GreaterThan, 50.0f);
//				rule.setCondition(raiBed, "occupied", null, Operator.Equal, true);
//				int time = 10;
//				Log.i(TAG, "Timeout " + time + "seg");
//				rule.setTimeout(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rule.identify();
			Toast.makeText(this, "Regra registrada", Toast.LENGTH_SHORT).show();

			// RuleInterpreterTest test = new RuleInterpreterTest(null);
			// // rule.registerStakeholder("Regra disparada", test.getRAI());
			// test.identify();
			// Toast.makeText(this, "Aplicação inicializada e registrada",
			// Toast.LENGTH_SHORT).show();
			//
			// // FIXME: DEBUG
			// test.context = this;

//			new Generic("Stove Urgency Action", "Stove Urgency Action", rule,
//					RuleInterpreter.RULE_TRIGGERED) {
//				private static final long serialVersionUID = 1L;
//
//				private int counter = 0;
//
////				String tvRai = discovery.search("Television").get(0);
//				String tvRai = discovery.searchForAttribute(ResourceData.TYPE, ResourceAgent.type(Television.class)).get(0).getRai();
////				String tvRai = discovery.searchForAttribute(ResourceData.TYPE, ResourceAgent.type(Television.class)).get(0).getName();
//				private ITelevision tv = new TelevisionStub(tvRai);
//
//				@Override
//				public void notificationHandler(String rai, String method, Object value) {
//					Log.i(TAG, "CHANGE: " + rai + " " + method + " " + value);
//					Message msg = mHandler.obtainMessage();
//					msg.obj = counter++;
//					mHandler.sendMessage(msg);
//					mHandler.post(mUpdateResults);
//
//					RuleActivity.this.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							tv.showMessage("Alert!");
//						}
//					});
//				}
//			};

			// FIXME: DEBUG
			// Change Stove state
			IStove stove = new StoveStub(raiStove);
			stove.setOvenTemperature(76.0f);

			// change bed's value
			// int delay = 5000; // delay for 5 sec.
			// int period = 1000; // repeat every sec.
			// Timer timer = new Timer();
			// timer.scheduleAtFixedRate(new TimerTask() {
			// public void run() {
			// System.out.println("done");
			// IBed bed = new BedStub(raiBed);
			// bed.(76.0f);
			// }
			// }, delay, period);

			try {
				Log.i(TAG, "Finalizing...");
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); //>>>setContentView(R.layout.rule);
	 * 
	 * //Initialize Condition's ListView //ListView lv = (ListView)
	 * findViewById(R.id.listViewConds); //listItems.add("Nenhuma condição");
	 * //lvAdapter = new ArrayAdapter<String>(this, //
	 * android.R.layout.simple_list_item_1, listItems);
	 * //lv.setAdapter(lvAdapter);
	 * 
	 * //Binding RuleAgent //Intent intent = new Intent(this, Rule.class);
	 * //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	 * 
	 * //Get Discovery Service //if (rule != null) // discovery = rule.getRDS();
	 * 
	 * / Teste
	 * 
	 * // Log.d(TAG, "Teste inicio"); Toast.makeText(this, "Teste inicio",
	 * Toast.LENGTH_SHORT).show(); //Exemplo de agregação, interpretação e
	 * criação //intent = new Intent(this, Stove.class); //bindService(intent,
	 * mConnection, Context.BIND_AUTO_CREATE); Stove s = new
	 * Stove("Fogao Querido"); //IResourceDiscovery discover =
	 * ResourceAgent.getRDS(); //IResourceDiscovery discover = new
	 * ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS); // Log.d(TAG,
	 * "search"); // String rai = discover.search("Stove").get(0); String rai =
	 * String.valueOf(s.getId()); // Log.d(TAG, "rai = " + rai); // IStove stove
	 * = new StoveStub(rai); // Log.d(TAG, "stove = " + stove.getId());
	 * 
	 * int nextStep = 0; // this variable will not be reached from within the
	 * onClick-methods
	 * 
	 * // DialogInterface.OnClickListener dialogClickListener = new
	 * DialogInterface.OnClickListener() { // @Override // public void
	 * onClick(DialogInterface dialog, int which) { // // } // }; //
	 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * 
	 * Interpreter intp = new Interpreter(); Log.d(TAG, "interpretador = " +
	 * intp.getId()); intp.setName("Perigo Vazamento de Gás");
	 * Toast.makeText(this, "Regra: Perigo Vazamento de Gás",
	 * Toast.LENGTH_SHORT).show(); //
	 * builder.setMessage("Regra: Perigo Vazamento de Gás"
	 * ).setNeutralButton("ok", dialogClickListener).show();
	 * 
	 * //Set Context Variable String cvName = "Vazamento de gás";
	 * intp.setContextVariable(s, cvName); String str =
	 * "Condição\nVariável de Contexto: " + cvName;
	 * 
	 * //Set conditional results float cte = 0.7f; String res = "Emergência!";
	 * intp.setConditionalResult(Operator.GreaterThanOrEqual, cte, res); str +=
	 * "\n  " + res + ", se " + cvName + " >= " + cte;
	 * 
	 * cte = 0.4f; res = "Alerta";
	 * intp.setConditionalResult(Operator.GreaterThanOrEqual, cte, res); str +=
	 * "\n  " + res + ", se " + cvName + " >= " + cte;
	 * 
	 * cte = 0.4f; res = "Normal"; intp.setConditionalResult(Operator.LessThan,
	 * cte, res); str += "\n  " + res + ", se " + cvName + " < " + cte;
	 * 
	 * Toast.makeText(this, str, Toast.LENGTH_LONG).show(); //
	 * builder.setMessage(str).setNeutralButton("ok",
	 * dialogClickListener).show();
	 * 
	 * 
	 * //Interpretation res = intp.interpretToString(); Log.d(TAG,
	 * "Interpretation: " + res); Toast.makeText(this, "Interpretação: " + res,
	 * Toast.LENGTH_LONG).show(); // builder.setMessage("Interpretation: " +
	 * res).setNeutralButton("ok", dialogClickListener).show(); }
	 */

	/*
	 * Button call Condition List
	 */
	public void buttonConds_Clicked(View view) {
		Intent intent = new Intent(RuleActivity.this, RuleCondListActivity.class);
		startActivityForResult(intent, 1);
	}

	/*
	 * Button call Action List
	 */
	public void buttonActions_Clicked(View view) {
		Intent intent = new Intent(RuleActivity.this, RuleActionListActivity.class);
		startActivityForResult(intent, 1);
	}

	/*
	 * onActivityResult
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) Receives the result of activities called. For
	 * getting the conditions and actions, in case.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// Condition cond =
				// (Condition)data.getSerializableExtra("COND");
				// String[] condStr = data.getStringArrayExtra("COND_LIST");

				// >>>>ArrayList<Condition> conds = (ArrayList<Condition>)
				// Serialization.deserializeObject(data.getByteArrayExtra("COND_LIST"));
				// Condition cond = (Condition)
				// Serialization.deserializeObject(data.getByteArrayExtra("COND"));
				// //DEBUG

				// createCond(condStr[0], condStr[1], condStr[2], condStr[3]);
				Toast.makeText(this, "Novas Condições", Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	/*
	 * Creates a condition and adds to the list. Then a rule could be created
	 * after.
	 */
	public void createCond(String raID, String attrib, Operator operator, String value) {
		// ResourceAgent ra =
		// (ResourceAgent)discovery.search(s.getSelectedItem().toString()).get(0);
		// ResourceAgent ra = new Stove();// FIXME: get it out of here, just for
		// debug. The correct is above

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
		String mtd = "get" + attrib;

		Condition cond;
		try {
			// Initialize Condition
			// cond = new Condition(ra.getRAI(), mtd, null, operator, value, 0,
			// this);
		} catch (Exception e) {
			Toast.makeText(this, "Erro ao criar a condição", Toast.LENGTH_LONG);
			return;
		}

		// Add condition to list of conditions
		// conds.add(cond);
		// Add condition to list in the view
		// listItems.add(cond.toString());
		// lvAdapter.notifyDataSetChanged();
	}

	public void buttonCreateRule_Clicked(View view) {
		try {
			EditText et = (EditText) view.findViewById(R.id.editTextCreateRule);
			// FIXME: it's not this way that we create rules
			// rule.expression = et.getText().toString();

			boolean result = false;
			// result = rule.runScript(et.getText().toString());
			// result = rule.runScript("true||false&&true");
			TextView tv = (TextView) view.findViewById(R.id.textViewLog);
			tv.setText(result ? "ação disparada" : " ação não disparada");
			tv.append("\nRegra <" + rule.expression + "> " + (result ? "ação disparada" : " ação não disparada"));

			if (rule != null)
				Toast.makeText(RuleActivity.this, "Regra sobreescrita", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(RuleActivity.this, "Erro ao criar a regra", Toast.LENGTH_SHORT).show();
		}

	}

	public void buttonRunScript_Clicked(View view) {
		if (rule == null)
			Toast.makeText(RuleActivity.this, "É necessário criar a regra primeiro", Toast.LENGTH_SHORT).show();
		else {
			boolean result = false;
			try {
				// FIXME: it's not this way that we run rules
				// result = rule.runScript(rule.expression);
				// } catch (EvalError e) {
				// Toast.makeText(RuleActivity.this, "Erro ao Executar a regra",
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(RuleActivity.this, "Erro ao Executar a regra", Toast.LENGTH_SHORT).show();
			}

			try {
				TextView tv = (TextView) view.findViewById(R.id.textViewLog);
				tv.append("\nRegra <" + rule.expression + "> " + (result ? "ação disparada" : " ação não disparada"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private Activity a = this;
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			Message msg = mHandler.obtainMessage();
			String str = "";
			if (msg.obj != null)
				str = " Contador em " + msg.obj.toString();
			Toast.makeText(a, "Regra disparada!" + str, Toast.LENGTH_LONG).show();
			// Toast.makeText(a, "Regra disparada! Contador em ",
			// Toast.LENGTH_LONG).show();
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			ResourceBinder binder = (ResourceBinder) service;
			rule = (Rule) binder.getService();

			// Tell the user about this for our demo.
			Toast.makeText(RuleActivity.this, "Agente de Regras Conectado", Toast.LENGTH_SHORT).show();
		}

		public void onServiceDisconnected(ComponentName className) {
			rule = null;
			Toast.makeText(RuleActivity.this, "Agente de Regras Desconectado", Toast.LENGTH_SHORT).show();
		}
	};
}
