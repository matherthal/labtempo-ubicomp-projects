package br.uff.tempo.middleware.resources;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IPerson;

public class Person extends ResourceAgent implements IPerson{

	@Override
	public void notificationHandler(String change) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	public Date getAge() {
		return null;
	}


	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		List<Tuple<String, Method>> attribs = new ArrayList<Tuple<String,Method>>();
		attribs.add(new Tuple<String, Method>("Idade", this.getClass().getMethod("getAge")));
		return attribs;
	}
}
