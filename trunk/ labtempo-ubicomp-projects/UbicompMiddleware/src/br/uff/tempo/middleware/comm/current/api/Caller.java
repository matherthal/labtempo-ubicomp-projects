package br.uff.tempo.middleware.comm.current.api;

import java.io.Serializable;

import android.util.Log;
import br.uff.tempo.middleware.comm.interest.api.NewDispatcher;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceNSContainer;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class Caller implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ResourceAgentIdentifier calleeAgent;

	public Caller(String calleeAgent) {
		this.calleeAgent = new ResourceAgentIdentifier(calleeAgent);
	}

	public String sendMessage(String jsonString) {
		try {
			String result = "";
			if (calleeAgent.getPath().equals(ResourceAgentIdentifier.getLocalIpAddress())) {
				Log.d("SmartAndroid", String.format("Sending LOCAL msg %s to %s", jsonString, calleeAgent.getRai()));
				result = NewDispatcher.getInstance().dispatch(calleeAgent.getRai(), jsonString);
				Log.d("SmartAndroid", String.format("Receive LOCAL msg %s from %s", result, calleeAgent.getRai()));
			} else {
				ResourceAgentNS rans = ResourceNSContainer.getInstance().get(this.calleeAgent.getRai());
				
				Log.d("SmartAndroid", String.format("Sending REMOTE msg %s to %s", jsonString, calleeAgent.getRai()));
				result = SocketService.sendReceive(rans.getIp(), rans.getRans() + ";" + jsonString + ";");
				Log.d("SmartAndroid", String.format("Receive REMOTE msg %s from %s", result, calleeAgent.getRai()));
			}
			
			return result;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("Caller", "IllegalArgumentException: " + e.getMessage());
		} catch (SmartAndroidException e) {
			e.printStackTrace();
			Log.e("Caller", "Exception: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Caller", "Exception: " + e.getMessage());
		}
		return "error";
	}
}
