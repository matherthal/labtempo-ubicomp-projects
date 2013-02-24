package br.uff.tempo.apps.rule;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.apps.rule.actuators.OvenForgotActuator;
import br.uff.tempo.middleware.management.Actuator;
import br.uff.tempo.middleware.management.RuleInterpreter;
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
	private static final String ruleOvenForgotName = "OvenForgot";
	
	private static final String ruleOvenForgot2File = "interpreter_ovenforgot2.json";
	private static final String ruleOvenForgot2Name = "OvenForgot2";
	
	private static final String ruleOvenForgot3File = "interpreter_ovenforgot_actuator.json";
	private static final String ruleOvenForgot3Name = "OvenForgot3";

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

			// Start rules
			ruleStarter();

			// Start actuators
			actuatorStarter();

			started = true;
		}
	}

	private void ruleStarter() {
		RuleInterpreter ri;
		try {
			ri = new RuleInterpreter(ruleOvenForgotName, ruleOvenForgotName);
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgotFile));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS());
			ri.setExpression(expr);
			ri.identify();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter(ruleOvenForgot2Name, ruleOvenForgot2Name);
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgot2File));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_SOLT_", bed1.getRANS()).replace("_BED_CASAL_", bed2.getRANS());
			ri.setExpression(expr);
			ri.identify();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
		}

		try {
			ri = new RuleInterpreter(ruleOvenForgot3Name, ruleOvenForgot3Name);
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgot3File));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS()).replace("_TV_", tv1.getRANS());
			ri.setExpression(expr);
			ri.identify();
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

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		started = false;
	}
}
