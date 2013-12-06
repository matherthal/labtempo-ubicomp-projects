package br.uff.tempo.apps.rule.actuators;

import android.util.Log;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class OvenForgotActuator extends Generic {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "OvenForgotActuator";
	private IStove stove = null;
	private ITelevision tv = null;

	public OvenForgotActuator() {
		super("OvenTurnOffActuator", "OvenTurnOffActuator");
	}

	public void setStove(IStove stove) {
		this.stove = stove;
	}

	public void setTV(ITelevision tv) {
		this.tv = tv;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.i(TAG, "Notification received");
		if (stove != null) {
			stove.turnOffOven();
			Log.i(TAG, "Stove turned off");
			this.log("Fogão ligado");
		} else {
			Log.e(TAG, "Stove not found");
		}
		if (tv != null) {
			tv.showMessage("Forno esquecido ligado!");
			Log.i(TAG, "Show message on TV");
			this.log("Fogão esquecido ligado");
		} else {
			Log.e(TAG, "TV not found");
			this.log("TV não encontrada");
		}
	}
	
	@Override
	public boolean identify() {
		return super.isRegistered() ? true : super.identify();
	}
}
