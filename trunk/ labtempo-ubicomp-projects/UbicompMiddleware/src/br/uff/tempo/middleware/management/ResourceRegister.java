package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRegister extends ResourceAgent implements IResourceRegister {
	
	private static final long serialVersionUID = 1L;

	ResourceRepository rR;
	ResourceLocation rL;

	private static ResourceRegister instance;

	private ResourceRegister() {
		setId(1);
		setName("ResourceRegister");
		setType("management");
		setRAI("br.uff.tempo.middleware.management.ResourceRegister");

		setRAI(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceRegister", "ResourceRegister"));

		ResourceContainer.getInstance().add(this);
	}

	public static ResourceRegister getInstance() {
		if (instance == null)
			instance = new ResourceRegister();
		return instance;
	}

	public boolean register(String rans, String ip, int prefix, String url) {
		rR = ResourceRepository.getInstance();
		rR.add(rans, ip, prefix, url);
		
		notifyStakeholders("register", url);
		return true;
	}
	
	public boolean registerLocation(String rans, String ip, int prefix, String url, Position position) {
		rR = ResourceRepository.getInstance();
		rR.add(rans, ip, prefix, url);
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
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean registerInPlace(String rans, String ip, int prefix, String url, String placeName, Position position) {
		rR = ResourceRepository.getInstance();
		rR.add(rans, ip, prefix, url);
		rL = ResourceLocation.getInstance();
		if (position == null){
			rL.registerInPlaceMiddlePos(url, rL.getPlace(placeName));
		} else {
			rL.registerInPlaceRelative(url,rL.getPlace(placeName), position);
		}
		return true;
	}
}
