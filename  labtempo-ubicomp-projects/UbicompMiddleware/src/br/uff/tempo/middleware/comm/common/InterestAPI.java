package br.uff.tempo.middleware.comm.common;


public interface InterestAPI {
	
	
	// 1st step
	
	
	public Object fetchContextVariable(String contextVariable, String rai) throws Exception;
	
	public Object callService(String serviceName, String rai) throws Exception;
	
	// registerStakeholders
	public void registerInterest(String contextVariable, String rai, Callable callback) throws Exception;
	
	// notifyStakeholders
	public void sendMessageTo(String rai, String contextVariable, String value) throws Exception;

	// removeStakeholders
	public void removeInterest(String contextVariable, String rai) throws Exception;
	
	
	// 2nd step

	
	public Object fetchContextVariable(String contextVariable) throws Exception;
	
	public void sendMessage(String contextVariable, String value) throws Exception;
	
	// registerStakeholders, callback = notificationHandler
	// (1 contextVariable could have N callbacks)
	public void registerInterest(String contextVariable, Callable callback) throws Exception;

	// removeStakeholders
	public void removeInterest(String contextVariable) throws Exception;

	// removeNotificationHandlers
	public void removeInterestCallback(String contextVariable, Callable callback) throws Exception;
	
	
	// 3rd step
	
	
//	public int getMyAddress() throws Exception;
	
//	public void removeAllInterests() throws Exception;
	
//	public List<String> getListContextVariables() throws Exception;
	
//	public List<Integer> getListAgents() throws Exception;
	
}
