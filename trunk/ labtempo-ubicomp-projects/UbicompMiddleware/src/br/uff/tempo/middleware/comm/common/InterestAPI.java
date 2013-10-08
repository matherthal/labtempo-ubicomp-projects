package br.uff.tempo.middleware.comm.common;

import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgentNS;


public interface InterestAPI {

	// AGENTE se identifica pro mundo
	public void registerInterest(String rans) throws Exception;
	
	// AGENTE recebe msg de AGENTE com JSONRPC exemplo: "registerInterest("lampadadasala.ra/jsonrpc", new JSONRPCCallback(this))
	// AGENTE recebe msg de AGENTE com REPAMESSAGE
	// AGENTE recebe msg de COISA com REPAMESSAGE
	public void registerInterest(String rans, String interest, Callable callback) throws Exception; 

	// COISA recebe msg de AGENTE com REPAMESSAGE
	// COISA recebe msg de COISA com REPAMESSAGE
	public void registerInterest(String interest, Callable callback) throws Exception;
	

	// Begin of Sync
	
	// AGENTE manda msg pra AGENTE com REPAMESSAGE
	public String sendMessage(ResourceAgentNS raNSFrom, ResourceAgentNS raNSTo, String interest, String message) throws Exception;
	
	// AGENTE manda msg pra COISA com REPAMESSAGE
	public String sendMessage(ResourceAgentNS raNSFrom, Integer prefixTo, String interest, String message) throws Exception;	
	
	// COISA manda msg pra AGENTE com REPAMESSAGE
	public String sendMessage(ResourceAgentNS raNSTo, String interest, String message) throws Exception;	
	
	// COISA manda msg pra COISA com REPAMESSAGE
	public String sendMessage(Integer prefixTo, String interest, String message) throws Exception;
	
	// COISA manda msg pra COISA com REPAMESSAGE
	public String sendMessage(String interest, String message) throws Exception;
	
	// End of Sync
	
	
	// Begin of Async

	// AGENTE manda msg Async pra AGENTE com REPAMESSAGE
	public void sendAsyncMessage(ResourceAgentNS raNSfrom, ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception;
	
	// AGENTE manda msg Async pra COISA com REPAMESSAGE
	public void sendAsyncMessage(ResourceAgentNS raNSfrom, int prefixTo, String interest, String message, Callable callback) throws Exception;	
	
	// COISA manda msg Async pra AGENTE com REPAMESSAGE
	public void sendAsyncMessage(ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception;	
	
	// COISA manda msg Async pra COISA com REPAMESSAGE
	public void sendAsyncMessage(int prefixTo, String interest, String message, Callable callback) throws Exception;
	
	// End of Async	

	
	// Begin of Listing
	public List<ResourceAgentNS> getListOfResourceAgents() throws Exception;

	public List<ResourceAgentNS> getListOfResourceAgentsInterestedIn(String interest) throws Exception;
	
	public List<String> getListOfInterestedIn(String interest) throws Exception;

	// End of Listing	
	
	
	
	
	
	
	// 1st step
	
	
//	public Object fetchContextVariable(String contextVariable, String rai) throws Exception;
	
//	public Object callService(String serviceName, String rai) throws Exception;
	
	// registerStakeholders
//	public void registerInterest(String contextVariable, String rai, Callable callback) throws Exception;
	

	
//	public String sendMessage(int prefixfrom, ResourceAgentNS raNSto, String interest, String message) throws Exception;
	
	// notifyStakeholders
//	public void sendMessageTo(String rai, String contextVariable, String value) throws Exception;

	// removeStakeholders
//	public void removeInterest(String contextVariable, String rai) throws Exception;
	
	
	// 2nd step

	
//	public Object fetchContextVariable(String contextVariable) throws Exception;
	
//	public void sendMessage(String contextVariable, String value) throws Exception;
	
	// registerStakeholders, callback = notificationHandler
	// (1 contextVariable could have N callbacks)
//	public void registerInterest(String contextVariable, Callable callback) throws Exception;

	// removeStakeholders
//	public void removeInterest(String contextVariable) throws Exception;

	// removeNotificationHandlers
//	public void removeInterestCallback(String contextVariable, Callable callback) throws Exception;

	
	
	
	// 3rd step
	
	
//	public int getMyAddress() throws Exception;
	
//	public void removeAllInterests() throws Exception;
	
//	public List<String> getListContextVariables() throws Exception;
	
//	public List<Integer> getListAgents() throws Exception;
	
}
