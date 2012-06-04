package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IPerson;

public class Person extends ResourceAgent implements IPerson{

	@Override
	public void notificationHandler(String change){
		// TODO Auto-generated method stub
		
	}

	public Date getAge() {
		return null;
	}
}
