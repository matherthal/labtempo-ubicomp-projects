package br.uff.tempo.apps.talk2String.server;

import br.uff.tempo.apps.talk2String.ITalk;
import br.uff.tempo.middleware.management.ResourceAgent;

public class TalkAgent extends ResourceAgent implements ITalk {
	
	private static final long serialVersionUID = 8330168721606310147L;
	private String message;

	public TalkAgent(String name, String rans) {
		super(name, "br.uff.tempo.apps.talk2String.server.TalkAgent", rans);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		notifyStakeholders("setMessage", message);
	}
	
	public String getURLStream(){
		return message;
	}
	
	public void setURLStream(String URL) {
		this.message = URL;
		notifyStakeholders("setURLStream", message);
	}

}
