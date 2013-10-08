package br.uff.tempo.middleware.comm.current.api;

import java.io.Serializable;

import android.util.Log;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.comm.common.Dispatcher;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceNSContainer;

public class Caller implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ResourceAgentNS raNS;

	public Caller(String rans) {
		this.raNS = ResourceNSContainer.getInstance().get(rans); // Works like DNS
		
		if (this.raNS == null) {
			throw new SmartAndroidRuntimeException("You are trying to use a Stub with an agent that does not exist yet. Check if you has called agent.identify() before calling this Stub.");
		}
	}

	public String sendMessage(String jsonString) {
		try {
			String result = "";

			if (SmartAndroid.interestAPIEnable) {
				Log.d("SmartAndroid", "   ");
				Log.d("SmartAndroid", String.format("SENDING to %s \"REMOTE\" %s", raNS.getRans(), jsonString));
				String message = raNS.getRans() + SocketService.BUFFER_END + jsonString + SocketService.BUFFER_END;
				String resultTemp = InterestAPIImpl.getInstance().sendMessage(raNS, "jsonrpc", message);
				result = resultTemp.substring(0, resultTemp.lastIndexOf(SocketService.BUFFER_END));
				Log.d("SmartAndroid", String.format("RECEIVE from %s \"REMOTE\" %s", raNS.getRans(), result));
				Log.d("SmartAndroid", "   ");
			} else {
				if (raNS.getIp().equals(SmartAndroid.getLocalIpAddress())) {
					Log.d("SmartAndroid", String.format("DISPATCH SND LOCAL %s to %s", jsonString, raNS.getRans()));
					result = Dispatcher.getInstance().dispatch(raNS.getRans(), jsonString);
					Log.d("SmartAndroid", String.format("DISPATCH RST LOCAL %s to %s", result, raNS.getRans()));
				} else {
					Log.d("SmartAndroid", String.format("SENDING to %s REMOTE %s", raNS.getRans(), jsonString));
					result = SocketService.sendReceive(raNS.getIp(), raNS.getRans() + SocketService.BUFFER_END + jsonString + SocketService.BUFFER_END);
					Log.d("SmartAndroid", String.format("RECEIVE from %s REMOTE %s", raNS.getRans(), result));
				}				
			}
			
			return result;
		} catch (IllegalArgumentException e) {
			Log.e("SmartAndroid", String.format("Exception in Caller sendMessage: %s", e));
		} catch (Exception e) {
			Log.e("SmartAndroid", String.format("Exception in Caller sendMessage: %s", e));
		}
		return "error";
	}
}
