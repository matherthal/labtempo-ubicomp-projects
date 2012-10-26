package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRegister extends ResourceAgent implements IResourceRegister {
	
	private static final long serialVersionUID = 1L;

	private static ResourceRegister instance;

	private ResourceRegister() {
		super("ResourceRegister", ResourceRegister.class.getName(), IResourceRegister.rans);
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
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.getRANS(), ip, prefix));
		ResourceRepository.getInstance().add(this.getRANS(), ip, prefix);
		ResourceDirectory.getInstance().create(new ResourceData(this.getRANS(), this.getName(), this.getType(), null, null));
		
		return true;
	}
	
	@Override
	public boolean register(String rans, String ip, int prefix, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		ResourceDirectory.getInstance().create(resourceData);
		
		notifyStakeholders("register", rans);
		return true;
	}
	
	@Override
	public boolean registerLocation(String rans, String ip, int prefix, Position position, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		ResourceLocation.getInstance().registerInPlace(rans, position);
		
		ResourceDirectory.getInstance().create(resourceData);
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
	public boolean registerInPlace(String rans, String ip, int prefix, String placeName, Position position, ResourceData resourceData) {
		ResourceRepository.getInstance().add(rans, ip, prefix);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(rans, ip, prefix));
		
		ResourceLocation rL = ResourceLocation.getInstance();
		
		//ResourceDirectory.getInstance().create(resourceData);
		
		if (position == null){
			rL.registerInPlaceMiddlePos(rans, rL.getPlace(placeName));
		} else {
			rL.registerInPlaceRelative(rans, rL.getPlace(placeName), position);
		}
		return true;
	}
}
