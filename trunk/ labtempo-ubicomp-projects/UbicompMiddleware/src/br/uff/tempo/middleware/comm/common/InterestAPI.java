package br.uff.tempo.middleware.comm.common;

import java.util.List;

public interface InterestAPI {
	
	public int getMyAddress() throws Exception;
	
	public void sendMessage(String contextVariable, String value) throws Exception;

	// notifyStakeholders
	public void sendMessageTo(String address, String contextVariable, String value) throws Exception;

	// registerStakeholders, callback = notificationHandler
	// (1contextvar..Ncallback)
	public void addInterest(String contextVariable, Callable callback) throws Exception;

	// removeStakeholders
	public void removeInterest(String contextVariable) throws Exception;

	// removeNotificationHandlers
	public void removeInterestCallback(String contextVariable, Callable callback) throws Exception;
	
	public void removeAllInterests() throws Exception;
	
	public List<String> getListContextVariables() throws Exception;
	
	public List<Integer> getListAgents() throws Exception;
	
}