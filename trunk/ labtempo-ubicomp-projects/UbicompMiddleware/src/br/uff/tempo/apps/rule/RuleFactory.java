package br.uff.tempo.apps.rule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.apps.rule.actuators.OvenTurnOffActuator;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.interfaces.IBed;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class RuleFactory extends Service {
	private static final String TAG = "RuleFactory";
	private static final String ruleOvenForgotFile = "interpreter_ovenforgot.json";
	private static final String ruleOvenForgotName = "OvenForgot";

	public static boolean started = false;

	// Simulated Resources
	private IStove stv1;
	private IBed bed1;

	@Override
	public void onCreate() {
		if (!started) {
			// Create simulated resources
			stv1 = new Stove("stv1", "stv1");
			bed1 = new Bed("bed1", "bed1");
			stv1.identify();
			bed1.identify();

			// Default states
			stv1.turnOnOven();

			// Bind rules and actions
			ovenForgot();

			started = true;
		}
	}

	private void ovenForgot() {
		RuleInterpreter ri = new RuleInterpreter(ruleOvenForgotName, ruleOvenForgotName);
		try {
			String expr = ri.inputStreamToString(this.getAssets().open(ruleOvenForgotFile));
			// Set correct RANS in rule before creating RuleInterpreter
			expr = expr.replace("_STOVE_", stv1.getRANS()).replace("_BED_", bed1.getRANS());
			ri.setExpression(expr);
			ri.identify();

			// Bind actuator to rule
			OvenTurnOffActuator act = new OvenTurnOffActuator();
			act.setStove(stv1);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
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
