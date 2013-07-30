package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IPlace;

public class PlaceStub extends Stub implements IPlace{

	public PlaceStub(String rans) {
		super(rans);
	}

	@Override
	public void enter(ResourceData raData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), raData));

		makeCall("enter", params, void.class);
	}

	@Override
	public void exit(ResourceData raData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), raData));

		makeCall("exit", params, void.class);		
	}

}
