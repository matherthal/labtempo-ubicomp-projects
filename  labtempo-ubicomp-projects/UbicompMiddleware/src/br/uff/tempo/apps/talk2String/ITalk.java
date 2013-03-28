package br.uff.tempo.apps.talk2String;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ITalk extends IResourceAgent {
	
	String getMessage();
	void setMessage(String msg);
	String getURLStream();
	void setURLStream(String url);

}
