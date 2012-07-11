package br.uff.tempo.middleware.comm;

import java.io.Serializable;

import android.util.Log;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class Caller implements Serializable {
    private ResourceAgentIdentifier calleeAgent;
    
    private int port = 8080;
    SocketService socket;
    //private Handler handler = new Handler();
    
	public Caller(String calleeAgent) {
		this.calleeAgent = new ResourceAgentIdentifier(calleeAgent);
//		socket = new SocketService(this.calleeAgent.getPath(), 8080);
		// serverIP = "192.168.1.70"; //FIXME: IP shouldn't be hardcoded
	}

	public String getAgentCaller() {
		return calleeAgent.getType()+":"+calleeAgent.getName();
	}

	public String sendMessage(String jsonString) {
		//add callee + methodCaller to JSONObject

		try {
			String local = ResourceAgentIdentifier.getLocalIpAddress();
			String result = "";
			if (calleeAgent.getPath().equals(local))
				result = Dispatcher.getInstance().dispatch(calleeAgent.getRai(), jsonString);
			else
				result = SocketService.sendReceive(calleeAgent.getPath(), calleeAgent.getRai()+";"+jsonString+";");
			return result;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("Caller", "IllegalArgumentException: " + e.getMessage());
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Caller", "Exception: " + e.getMessage());
		}
		return "error";
	}	
}
