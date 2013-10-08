package br.uff.tempo.middleware.comm.common;

import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgentNS;

public interface InterestAPIWithoutComments {

	public void registerInterest(String rans) throws Exception;
	
	public void registerInterest(String rans, String interest, Callable callback) throws Exception; 
	
	public void registerInterest(String interest, Callable callback) throws Exception;

	public String sendMessage(ResourceAgentNS raNSFrom, ResourceAgentNS raNSTo, String interest, String message) throws Exception;

	public String sendMessage(ResourceAgentNS raNSFrom, Integer prefixTo, String interest, String message) throws Exception;	

	public String sendMessage(ResourceAgentNS raNSTo, String interest, String message) throws Exception;	

	public String sendMessage(Integer prefixTo, String interest, String message) throws Exception;

	public String sendMessage(String interest, String message) throws Exception;

	public void sendAsyncMessage(ResourceAgentNS raNSfrom, ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception;

	public void sendAsyncMessage(ResourceAgentNS raNSfrom, int prefixTo, String interest, String message, Callable callback) throws Exception;	
	
	public void sendAsyncMessage(ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception;	
	
	public void sendAsyncMessage(int prefixTo, String interest, String message, Callable callback) throws Exception;
	
	public List<ResourceAgentNS> getListOfResourceAgents() throws Exception;

	public List<ResourceAgentNS> getListOfResourceAgentsInterestedIn(String interest) throws Exception;
	
	public List<String> getListOfInterestedIn(String interest) throws Exception;

}
