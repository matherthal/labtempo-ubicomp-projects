package br.uff.tempo.middleware.management;

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
	
	public boolean register(ResourceAgent rA) {
		rR = ResourceRepository.getInstance();
		rR.add(rA);
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
	
	

}
