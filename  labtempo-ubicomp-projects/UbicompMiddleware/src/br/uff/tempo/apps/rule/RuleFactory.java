package br.uff.tempo.apps.rule;

import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.apps.rule.actuators.OvenForgotActuator;
import br.uff.tempo.middleware.management.Actuator;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.RuleComposer;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IActuator;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.IBed;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class RuleFactory extends Service {
	private static final String TAG = "RuleFactory";
	
	private static final String ruleOvenForgotFile = "interpreter_ovenforgot.json";
	private static final String ruleOvenForgot2File = "interpreter_ovenforgot2.json";
	private static final String ruleOvenForgot3File = "interpreter_ovenforgot_actuator.json";
	private static final String ruleWatchSoccerFile = "interpreter_inicio_futebol.json";
	private static final String ruleSomebodyAtDoorFile = "interpreter_somebody_at_door.json";

	private static String ruleOvenForgotName = "";
	private static String ruleOvenForgot2Name = "";
	private static String ruleOvenForgot3Name = "";
	private static String ruleWatchSoccerName = "";
	private static String ruleSomebodyAtDoorName = "";

	public static boolean started = false;

	// Simulated Resources
	private IStove stv1;
	private IBed bed1;
	private IBed bed2;
	private ITelevision tv1;

	// Rules' Names

	@Override
	public void onCreate() {
		if (!started) {
			// Create simulated resources
			stv1 = new Stove("stv1", "stv1");
			bed1 = new Bed("bed1", "bed1");
			bed2 = new Bed("bed2", "bed2");
			tv1 = new Television("tv1", "tv1");
			stv1.identify();
			bed1.identify();
			bed2.identify();
			tv1.identify();

			// Default states
			stv1.turnOnOven();
			stv1.setOvenTemperature(100);
			bed1.lieDown();
			bed2.getOut();

			// Start rules
			ruleStarter();

			// Start actuators
			actuatorStarter();
			
			// Test composer
			testRuleComposer1();

			// Start composed rules
			composedRulesStarter();
			
			started = true;
		}
	}

	private void ruleStarter() {
		RuleInterpreter ri;
		try {
			ri = new RuleInterpreter("r1", "r1");
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgotFile));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS());
			ri.setExpression(expr);
			ri.identify();
			ruleOvenForgotName = ri.getRANS();
			Log.i(TAG, "Created rule: " + ruleOvenForgotName);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter("r2", "r2");
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgot2File));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_SOLT_", bed1.getRANS()).replace("_BED_CASAL_", bed2.getRANS());
			ri.setExpression(expr);
			ri.identify();
			ruleOvenForgot2Name = ri.getRANS();
			Log.i(TAG, "Created rule: " + ruleOvenForgot2Name);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter("r3", "r3");
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgot3File));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS()).replace("_TV_", tv1.getRANS());
			ri.setExpression(expr);
			ri.identify();
			ruleOvenForgot3Name = ri.getRANS();
			Log.i(TAG, "Created rule: " + ruleOvenForgot3Name);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter("r4", "r4");
			String expr = ri.inputStreamToString(this.getAssets().open(ruleSomebodyAtDoorFile));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS());
			ri.setExpression(expr);
			ri.identify();
			ruleSomebodyAtDoorName = ri.getRANS();
			Log.i(TAG, "Created rule: " + ruleSomebodyAtDoorName);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter("r5", "r5");
			String expr = ri.inputStreamToString(this.getAssets().open(ruleWatchSoccerFile));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS());
			ri.setExpression(expr);
			ri.getName();
			ri.identify();
			ruleWatchSoccerName = ri.getRANS();
			Log.i(TAG, "Created rule: " + ruleWatchSoccerName);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}
	}

	private void actuatorStarter() {
		OvenForgotActuator act1 = null;
		try {
			// Start OvenForgot actuator
			act1 = new OvenForgotActuator();
			act1.setStove(stv1);
			act1.setTV(tv1);
			act1.identify();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in creating actuator OvenForgotActuator");
		}
		if (act1 != null) {
			try {
				// Bind OvenForgot actuator to rule
				IRuleInterpreter ri = new RuleInterpreterStub(ruleOvenForgotName);
				ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, act1.getRANS());
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Error in binding rule " + ruleOvenForgotName + " to actuator OvenForgotActuator");
			}
			try {
				// Bind OvenForgot actuator to rule
				IRuleInterpreter ri = new RuleInterpreterStub(ruleOvenForgot2Name);
				ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, act1.getRANS());
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Error in binding rule " + ruleOvenForgot2Name + " to actuator OvenForgotActuator");
			}
		}
		
		try {
			Actuator act2 = new Actuator("OvenTurnOffActuator", "OvenTurnOffActuator");
			String expr = act2.inputStreamToString(this.getAssets().open(ruleOvenForgot3File));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS()).replace("_TV_", tv1.getRANS());
			act2.setExpression(expr);
			act2.identify();
			
			// Bind actuator to rule
			IRuleInterpreter ri = new RuleInterpreterStub(ruleOvenForgot3Name);
			ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, act2.getRANS());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "Error in reading " + ruleOvenForgot3File);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in actuator OvenTurnOffActuator");
		}
	}

	private void composedRulesStarter() {
		String name = "composed_r";
		String nameAct = "composed_act";
		int num = 1;
		for (String fname : fileList()) {
			FileInputStream stream = null;
			try {
				stream = openFileInput(fname);
				RuleInterpreter ri = new RuleInterpreter(name + num, name + num);
				ri.setExpression(stream);
				ri.identify();
				
				try {
					stream = openFileInput(fname);
					Actuator act = new Actuator(nameAct + num, nameAct + num);
					act.setExpression(stream);
					act.identify();
					
					// Bind actuator to rule
					ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, act.getRANS());
				} catch (Exception e) {
					Log.i(TAG, "Actuator not found in rule");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			num++;
		}
	}
	
	public void testRuleComposer1() {
		RuleComposer composer = new RuleComposer();
		try {
			// Rule name
			composer.setRuleName("Regra Teste Composicao");
			// Rule timer
			//composer.addExpressionTimer(10);
			// Rule expression
			composer.addConditionComp(stv1.getRANS(), "getOvenTemperature", new Object[]{}, Operator.GreaterThanOrEqual, 100.0, 0);
			composer.addAndClause();
			composer.addNotClause();
			composer.addOpenBracket();
			composer.addCondition(tv1.getRANS(), "isOn", new Object[]{}, 0);
			composer.addOrClause();
			composer.addConditionComp(bed1.getRANS(), "occupied", null, Operator.Equal, bed2.getRANS(), "occupied", null, 0);
			composer.addCloseBracket();
			// Actuator name
			composer.setActuatorName("Atuador teste composicao");
			// Actuator's actions
			composer.addAction(stv1.getRANS(), "setOvenTemperature", new Object[]{0.0});
			composer.addAction(tv1.getRANS(), "showMessage", new Object[]{"Teste_composicao_regra"});
			composer.finish(this.getBaseContext());
		} catch (Exception e) {
			Log.e(TAG, "Erro na criação de regra de contexto através do composer");
			e.printStackTrace();
		}
	}
	
	public void removeRuleInterpreter(IRuleInterpreter ri) {
		
	}
	
	public void removeActuator(IActuator act) {
		
	}
	
	public void removeRule(IRuleInterpreter ri, IActuator act) {
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		started = false;
	}
}
