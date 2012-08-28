package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRegister extends ResourceAgent implements IResourceRegister {

	ResourceRepository rR;
	ResourceLocation rL;

	private static ResourceRegister instance;

	private ResourceRegister() {
		setId(1);
		setName("ResourceRegister");
		setType("management");
		setURL("br.uff.tempo.middleware.management.ResourceRegister");

		setURL(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceRegister", "ResourceRegister"));

		ResourceContainer.getInstance().add(this);
	}

	public static ResourceRegister getInstance() {
		if (instance == null)
			instance = new ResourceRegister();
		return instance;
	}

	public boolean register(String url) {
		rR = ResourceRepository.getInstance();
		rR.add(url);
		return true;
	}
	
	public boolean registerLocation(String url, Position position) {
		rR = ResourceRepository.getInstance();
		rR.add(url);
		rL = ResourceLocation.getInstance();
		rL.registerInPlace(url,position);
		return true;
	}

	public boolean unregister(String url) {
		// rR = ResourceRepository.getInstance(); //already instantiated
		rR.remove(url);
		return true;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}
}
