package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveStub extends ResourceAgentStub implements IStove{
	public StoveStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getIsOn() {
		// TODO Auto-generated method stub
		return false;
	}

}
