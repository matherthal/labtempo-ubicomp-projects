package br.uff.tempo.middleware.management;

import java.util.List;

public class Person extends ResourceAgent{

	List<SmartObject> sensors;
	List<Local> recentLocals;

	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

}
