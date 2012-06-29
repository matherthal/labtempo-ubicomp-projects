package br.uff.tempo.middleware.comm;

import android.util.Log;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class Caller {
    private ResourceAgentIdentifier calleeAgent;
    
    private int port = 8080;
    //private Handler handler = new Handler();
    
	public Caller(String calleeAgent) {
		this.calleeAgent = new ResourceAgentIdentifier(calleeAgent);
		// serverIP = "192.168.1.70"; //FIXME: IP shouldn't be hardcoded
	}

	public String getAgentCaller() {
		return calleeAgent.getType()+":"+calleeAgent.getName();
	}

	public String sendMessage(String jsonString) {
		//add callee + methodCaller to JSONObject

		try {
//			if (calleeAgent.getPath().contains("127.0.0.1"))
//				return Dispatcher.getInstance().dispatch(calleeAgent.getRai(),jsonString);
//			else
//				{
					SocketService.sendStatus(calleeAgent.getPath(),10006 ,calleeAgent.getRai()+";"+jsonString);
					return SocketService.receiveStatus(port);
//				}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Caller", "IllegalArgumentException: " + e.getMessage());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Caller", "Exception: " + e.getMessage());
		}
		return "error";
	}	
}
