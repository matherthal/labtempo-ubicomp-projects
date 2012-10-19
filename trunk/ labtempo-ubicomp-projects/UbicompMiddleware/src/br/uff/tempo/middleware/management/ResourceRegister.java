package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRegister extends ResourceAgent implements IResourceRegister {
	
	private static final long serialVersionUID = 1L;

	private static ResourceRegister instance;

	private ResourceRegister() {
		setId(1);
		setName("ResourceRegister");
		setType("management");
		setRAI("br.uff.tempo.middleware.management.ResourceRegister");

		setRAI(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceRegister", "ResourceRegister"));
	}

	public static ResourceRegister getInstance() {
		if (instance == null)
			instance = new ResourceRegister();
		return instance;
	}
	
	@Override
	public boolean identify() {
		String ip = ResourceAgentIdentifier.getLocalIpAddress();
		int prefix = ResourceAgentIdentifier.getLocalPrefix();
		
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.getRAI(), ip, prefix));
		ResourceRepository.getInstance().add(this.getRAI(), ip, prefix, this.getRAI());
		
		return true;
	}
	
	@Override
	public boolean register(String rans, String ip, int prefix, String url, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix, url);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		//ResourceDirectory.getInstance().create(resourceData);
		
		notifyStakeholders("register", url);
		return true;
	}
	
	@Override
	public boolean registerLocation(String rans, String ip, int prefix, String url, Position position, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix, url);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		ResourceLocation.getInstance().registerInPlace(url,position);
		
		//ResourceDirectory.getInstance().create(resourceData);
		return true;
	}

	public boolean unregister(String url) {
		ResourceRepository.getInstance().remove(url);
		ResourceNSContainer.getInstance().remove(url);
		return true;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean registerInPlace(String rans, String ip, int prefix, String url, String placeName, Position position, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix, url);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		ResourceLocation rL = ResourceLocation.getInstance();
		
		//ResourceDirectory.getInstance().create(resourceData);
		
		if (position == null){
			rL.registerInPlaceMiddlePos(url, rL.getPlace(placeName));
		} else {
			rL.registerInPlaceRelative(url,rL.getPlace(placeName), position);
		}
		return true;
	}
}
