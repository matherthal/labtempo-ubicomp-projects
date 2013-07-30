package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.DayLightSensor;
import br.uff.tempo.middleware.resources.interfaces.IDayLightSensor;

public class DayLightSensorStub extends ResourceAgentStub implements IDayLightSensor {
	
	private static final long serialVersionUID = 1L;

	public DayLightSensorStub(String rai) {
		super(rai);
	}

	@Override
	@ContextVariable(name = "É dia", type = "IsDay")
	public boolean isDay() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall(DayLightSensor.CV_ISDAY, params, Boolean.class);
	}

	@Override
	@ContextVariable(name = "Quantidade de luz", type = "LightAmout")
	public float lightAmount() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Float) makeCall(DayLightSensor.CV_LIGHTAMOUNT, params, float.class);
	}

	@Override
	@Service(name = "Definir se é dia", type = "SetIsDay")
	public void setDay(boolean d) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), d));
		makeCall("setDay", params, void.class);
	}

	@Override
	@Service(name = "Definir quantidade de luz", type = "SetLightAmout")
	public void setLightAmount(float l) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Float.class.getName(), l));
		makeCall("setLightAmount", params, void.class);
	}

}
