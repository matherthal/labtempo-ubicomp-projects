package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class BedStub extends ResourceAgentStub implements IBed {

	public BedStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasSomeoneIn() {
		
		List<Tuple> params = new ArrayList<Tuple>();
		//params.add(new Tuple<String, Object>("text", text));
		
		return (Boolean) makeCall("hasSomeoneIn", params);
	}

	@Override
	public void setSomeoneIn(boolean someone) {
	
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("someone", someone));
		
		makeCall("setSomeoneIn", params);
	}

}
