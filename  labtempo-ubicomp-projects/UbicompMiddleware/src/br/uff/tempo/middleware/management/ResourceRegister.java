package br.uff.tempo.middleware.management;

public class ResourceRegister extends ResourceAgent implements IResourceRegister{

	ResourceRepository rR;
	
	private static ResourceRegister instance;
	
	private ResourceRegister ()
	{
		rR = ResourceRepository.getInstance();
	}
	
	public static ResourceRegister getInstance()
	{
		if (instance == null)
			instance = new ResourceRegister();
		return instance;
	}
	
	public void register(ResourceAgent rA) {
		rR.add(rA);
	}

	public void notifyStakeholders() {
		// TODO Auto-generated method stub
		
	}

	public boolean registerStakeholder(ResourceAgent rA) {
		// TODO Auto-generated method stub
		return false;
	}

	public void notificationHandler(ResourceAgent rA) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}
	
	

}
