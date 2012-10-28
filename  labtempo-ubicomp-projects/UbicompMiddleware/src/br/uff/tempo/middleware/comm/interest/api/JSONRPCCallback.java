package br.uff.tempo.middleware.comm.interest.api;

import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.NewDispatcher;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.ResourceAgentNS;

public class JSONRPCCallback implements Callable {
	
	@Override
	public String call(ResourceAgentNS raNS, String interest, String message) {
		String[] msgTokens = message.split(SocketService.BUFFER_END);
		
		String raiFrom = msgTokens[0];
		String jsonRPCString = msgTokens[1];
		
		String response = null;
		try {
			response = NewDispatcher.getInstance().dispatch(raiFrom, jsonRPCString) + SocketService.BUFFER_END;
		} catch (SmartAndroidException e) {
			e.printStackTrace();
		}
		return response;
	}
}
