package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IAggregator;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public abstract class Aggregator extends ResourceAgent implements IAggregator {
	
	private static final long serialVersionUID = 1L;
	
	// Set Interpreted Context Variables
	// private Set<Interpreter> interpretedCVSet = new HashSet<Interpreter>();
	// Set not Interpreted Context Variables
	// private Set<Tuple<IResourceAgent, Method>> rawCVSet = new
	// HashSet<Tuple<IResourceAgent, Method>>();

	// Set of Context Variables
	private Set<Tuple<IResourceAgent, Method>> rawCVSet = new HashSet<Tuple<IResourceAgent, Method>>();

	/*
	 * @Override public boolean addContextVariable(Interpreter interpreter) {
	 * this.interpretedCVSet.add(interpreter);
	 * 
	 * //IResourceDiscovery iRDS = getRDS(); //IStove iStove = new
	 * StoveStub(iRDS.search("stove").get(0));
	 * //iStove.registerStakeholder("getOvenIsOn", this.getRAI());
	 * 
	 * return false; }
	 */

	@Override
	public boolean addContextVariable(IResourceAgent ra, String method) {
		this.rawCVSet.add(new Tuple<IResourceAgent, Method>(ra, method));
		ra.registerStakeholder(method, this.getRAI());

		return false;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {

	}
}
