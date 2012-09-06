package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.DayLightSensor;
import br.uff.tempo.middleware.resources.interfaces.IDayLightSensor;

public class DayLightSensorStub extends ResourceAgentStub implements IDayLightSensor {

	public DayLightSensorStub(String url) {
		super(url);
	}

	@Override
	@ContextVariable(name = "É dia")
	public boolean isDay() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Boolean) makeCall(DayLightSensor.CV_ISDAY, params);
	}

	@Override
	@ContextVariable(name = "Quantidade de luz")
	public float lightAmount() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Float) makeCall(DayLightSensor.CV_LIGHTAMOUNT, params);
	}

	@Override
	@Service(name = "Definir se é dia")
	public void setDay(boolean d) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), d));
		makeCall("setDay", params);
	}

	@Override
	@Service(name = "Definir quantidade de luz")
	public void setLightAmount(float l) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Float.class.getName(), l));
		makeCall("setLightAmount", params);
	}

}
