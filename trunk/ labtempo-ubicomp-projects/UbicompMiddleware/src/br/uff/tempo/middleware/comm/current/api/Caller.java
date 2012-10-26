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
	
	private ResourceAgentNS raNS;

	public Caller(String rans) {
		this.raNS = ResourceNSContainer.getInstance().get(rans); // Works like DNS
	}

	public String sendMessage(String jsonString) {
		try {
			String result = "";

			if (raNS.getIp().equals(ResourceAgentIdentifier.getLocalIpAddress())) {
				Log.d("SmartAndroid", String.format("Sending LOCAL msg %s to %s", jsonString, raNS.getRans()));
				result = NewDispatcher.getInstance().dispatch(raNS.getRans(), jsonString);
				Log.d("SmartAndroid", String.format("Receive LOCAL msg %s from %s", result, raNS.getRans()));
			} else {
				Log.d("SmartAndroid", String.format("Sending REMOTE msg %s to %s", jsonString, raNS.getRans()));
				result = SocketService.sendReceive(raNS.getIp(), raNS.getRans() + SocketService.BUFFER_END + jsonString + SocketService.BUFFER_END);
				Log.d("SmartAndroid", String.format("Receive REMOTE msg %s from %s", result, raNS.getRans()));
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
