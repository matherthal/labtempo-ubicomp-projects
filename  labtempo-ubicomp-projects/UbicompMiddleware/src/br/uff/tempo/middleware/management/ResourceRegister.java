package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;

public class ResourceRegister extends ResourceAgent implements IResourceRegister{

	ResourceRepository rR;
	
	private static ResourceRegister instance;
	
	private ResourceRegister ()
	{
		setId(1);
		setName("ResourceRegister");
		setType("management");
	}
	
	public static ResourceRegister getInstance()
	{
		if (instance == null)
			instance = new ResourceRegister();
		return instance;
	}
	
	public boolean register(String url) {
		rR = ResourceRepository.getInstance();
		rR.add(url);
		return true;
	}
	
	public boolean unregister(String url) {
		//rR = ResourceRepository.getInstance(); //already instantiated
		rR.remove(url);
		return true;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		/*List<Tuple<String, Method>> attribs = new ArrayList<Tuple<String,Method>>();
		attribs.add(new Tuple<String, Method>("Busca Agente de Recurso", this.getClass().getMethod("register", ResourceAgent.class)));
		attribs.add(new Tuple<String, Method>("Desregistrar", this.getClass().getMethod("unregister", String.class)));
		return attribs;*/
		return null;
	}
}
