package br.uff.tempo.middleware.management;

public class ResourceRegister extends ResourceAgent implements IResourceRegister{

	ResourceRepository rR;
	
	public void register(ResourceAgent rA) {
		rR.add(rA);
	}
	
	

}
