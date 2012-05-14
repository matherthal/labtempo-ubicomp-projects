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
	
	public void register(ResourceAgent rA) {
		rR = ResourceRepository.getInstance();
		rR.add(rA);
	}
	
	public void unregister(ResourceAgent rA) {
		//rR = ResourceRepository.getInstance(); //already instantiated
		rR.remove(rA.getURL());
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}
	
	

}
