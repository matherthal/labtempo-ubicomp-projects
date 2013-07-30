package br.uff.tempo.apps.prenda;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import android.app.LauncherActivity.IconResizer;

public class PrendaAgent extends ResourceAgent implements IPrenda {

	public PrendaAgent(String name, String type, String rans, Position position) {
		super(name, type, rans, position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
		
	}
	
	

}
