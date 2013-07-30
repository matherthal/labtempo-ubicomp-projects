package br.uff.tempo.middleware.comm.interest.api;

import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.Dispatcher;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.management.ResourceAgentNS;

public class JSONRPCCallback implements Callable {
	
	@Override
	public String call(ResourceAgentNS raNS, String interest, String message) {
		String[] msgTokens = message.split(SocketService.BUFFER_END);
		
		String raiFrom = msgTokens[0];
		String jsonRPCString = msgTokens[1];
		
		String response = null;
		
		Log.d("SmartAndroid", String.format("  DISPATCH SND %s to %s", jsonRPCString, raNS));
		response = Dispatcher.getInstance().dispatch(raiFrom, jsonRPCString) + SocketService.BUFFER_END;
		Log.d("SmartAndroid", String.format("  DISPATCH RST %s from %s", response, raNS));

		return response;
	}
}
