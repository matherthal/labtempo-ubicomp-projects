package br.uff.tempo.apps.rule.actuators;

import java.util.Iterator;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;
import br.uff.tempo.middleware.management.utils.Stakeholder;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.stubs.StoveStub;

public class OvenTurnOffActuator extends Generic {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "OvenTurnOffActuator";
	private static final String ruleName = "OvenForgot";
	private IStove stove = null;

	public OvenTurnOffActuator() {
		super("OvenTurnOffActuator", "OvenTurnOffActuator");
		this.name = "OvenTurnOffActuator";

		super.identify();
		IRuleInterpreter ri = new RuleInterpreterStub(ruleName);
		ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, this.getRANS());
		
		// Obtain stove to turn off
		// Iterator<Stakeholder> it = ri.getStakeholders().iterator();
		// while (it.hasNext()) {
		// Stakeholder ra = it.next();
		// if (IStove.class.equals(ra.getClass())) {
		// stove = new StoveStub(ra.getRANS());
		// break;
		// }
		// }
	}

	public void setStove(IStove stove) {
		this.stove = stove;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.i(TAG, "Notification received");
		if (stove != null) {
			stove.turnOffOven();
			Log.i(TAG, "Stove turned off");
		} else {
			Log.e(TAG, "Stove not found in rule");
		}
	}
	
	@Override
	public boolean identify() {
		return super.isRegistered() ? true : super.identify();
	}
}
