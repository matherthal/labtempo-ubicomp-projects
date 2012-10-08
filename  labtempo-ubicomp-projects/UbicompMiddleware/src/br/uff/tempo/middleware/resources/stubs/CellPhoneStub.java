package br.uff.tempo.middleware.resources.stubs;


import java.util.List;
import java.util.ArrayList;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ICellPhone;

public class CellPhoneStub extends ResourceAgentStub implements ICellPhone {
	public CellPhoneStub(String rai) {
		super(rai);
		// TODO Auto-generated constructor stub
	}
}
