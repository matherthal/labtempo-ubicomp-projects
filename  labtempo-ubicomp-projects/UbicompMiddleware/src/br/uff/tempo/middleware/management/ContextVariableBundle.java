package br.uff.tempo.middleware.management;

public class ContextVariableBundle {

	private String agentRans;
	private String contextVariable;
	private Object[] parameters;
	private String cvName;
	
	public ContextVariableBundle(String agentRans, String contextVariable, String cvName) {
		this(agentRans, contextVariable, null, cvName);
	}
	
	public ContextVariableBundle(String agentRans, String contextVariable, Object[] params, String cvName) {

		this.agentRans = agentRans;
		this.contextVariable = contextVariable;
		this.parameters = params;
		this.cvName = cvName;
	}

	public String getCvName() {
		return cvName;
	}

	public void setCvName(String cvName) {
		this.cvName = cvName;
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
