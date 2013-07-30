package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

public class SmartObject extends ResourceAgent{
	
	private static final long serialVersionUID = 1L;
	
	public SmartObject() {
		super("SmartObject", SmartObject.class.getName(), "SmartObject");
	}

	Place local;
	Position position;

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
