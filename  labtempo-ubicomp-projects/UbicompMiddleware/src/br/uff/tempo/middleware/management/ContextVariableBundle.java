package br.uff.tempo.middleware.management;

public class ContextVariableBundle {

	private String agentRans;
	private String contextVariable;
	private Object[] parameters;
	
	public ContextVariableBundle(String agentRans, String contextVariable) {
		this(agentRans, contextVariable, null);
	}
	
	public ContextVariableBundle(String agentRans, String contextVariable, Object[] params) {

		this.agentRans = agentRans;
		this.contextVariable = contextVariable;
		this.parameters = params; 
	}

	public String getAgentRans() {
		return agentRans;
	}

	public void setAgentRans(String agentRans) {
		this.agentRans = agentRans;
	}

	public String getContextVariable() {
		return contextVariable;
	}

	public void setContextVariable(String contextVariable) {
		this.contextVariable = contextVariable;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
}
