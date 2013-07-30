package br.uff.tempo.apps.simulators.utils;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

@SuppressWarnings("rawtypes")
public class ResourceWrapper {

	String id;
	IResourceAgent agent;
	IResourceAgent stub;
	Class simulator;
	
	public IResourceAgent getAgent() {
		return agent;
	}
	public void setAgent(IResourceAgent agent) {
		this.agent = agent;
	}
	public Class getSimulator() {
		return simulator;
	}
	public void setSimulator(Class simulator) {
		this.simulator = simulator;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public IResourceAgent getStub() {
		return stub;
	}
	public void setStub(IResourceAgent stub) {
		this.stub = stub;
	}
}
