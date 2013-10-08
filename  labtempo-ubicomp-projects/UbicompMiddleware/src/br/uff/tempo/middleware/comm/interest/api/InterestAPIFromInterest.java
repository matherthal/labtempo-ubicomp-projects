package br.uff.tempo.middleware.comm.interest.api;

import java.util.List;

import br.uff.tempo.middleware.comm.common.Callable;

public interface InterestAPIFromInterest {
	
	public int getMyAddress() throws Exception;
	
	public void sendMessage(String interest, String value) throws Exception;
	
	public void sendMessageTo(int address, String interest, String value) throws Exception;
	
	public void addInterest(String interest, Callable callback) throws Exception;
	
	public void removeInterest(String interest) throws Exception;
	
	public void removeInterestCallback(String interest, Callable callback) throws Exception;
	
	public void removeAllInterests() throws Exception;
	
	public List<String> getListInterests() throws Exception;
	
	public List<Integer> getListNodes() throws Exception;
	
}
