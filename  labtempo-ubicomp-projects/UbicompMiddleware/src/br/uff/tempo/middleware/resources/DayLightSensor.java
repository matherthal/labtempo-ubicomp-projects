package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IDayLightSensor;

public class DayLightSensor extends ResourceAgent implements IDayLightSensor {
	private static final String TAG = "DayLightSensor";
	private boolean day = false;
	private float light = 0.0f;
	public static final String CV_ISDAY = "isDay";
	public static final String CV_LIGHTAMOUNT = "lightAmount";
	
	public DayLightSensor(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.DayLightSensor", 17);
	}

	@Override
	@ContextVariable(name = "É dia")
	public boolean isDay() {
		return day;
	}

	@Override
	@ContextVariable(name = "Quantidade de luz")
	public float lightAmount() {
		return light;
	}

	@Override
	@Service(name = "Definir se é dia")
	public void setDay(boolean d) {
		Log.i(TAG, "Toggle day light to " + d);
		day = d;
		
		notifyStakeholders(CV_ISDAY, day);
	}

	@Override
	@Service(name = "Definir quantidade de luz")
	public void setLightAmount(float l) {
		Log.i(TAG, "Set light amount to " + l);
		light = l;
		
		notifyStakeholders(CV_LIGHTAMOUNT, light);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
