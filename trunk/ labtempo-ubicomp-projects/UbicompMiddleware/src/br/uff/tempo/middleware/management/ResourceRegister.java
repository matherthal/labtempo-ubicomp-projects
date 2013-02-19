package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;

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
		String ip = SmartAndroid.getLocalIpAddress();
		int prefix = SmartAndroid.getLocalPrefix();
		
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.getRANS(), ip, prefix));
		ResourceRepository.getInstance().add(new ResourceData(this.getRANS(), this.getName(), this.getType(), null, null, new ResourceAgentNS(this.getRANS(), ip, prefix)));
		
		return true;
	}
	
	@Override
	public boolean register(ResourceData resourceData) {
		ResourceNSContainer.getInstance().add(resourceData.getResourceAgentNS());
		ResourceRepository.getInstance().add(resourceData);
				
		notifyStakeholders("register", rans);
		return true;
	}
	
	@Override
	public boolean registerLocation(Position position, ResourceData resourceData) {
		
		ResourceNSContainer.getInstance().add(resourceData.getResourceAgentNS());
		
		ResourceLocation.getInstance().registerInPlace(rans, position);
		ResourceRepository.getInstance().add(resourceData);
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
	public boolean registerInPlace(String placeName, Position position, ResourceData resourceData) {
		
		ResourceNSContainer.getInstance().add(resourceData.getResourceAgentNS());
		
		ResourceLocation rL = ResourceLocation.getInstance();
		
		ResourceRepository.getInstance().add(resourceData);
		
		if (position == null){
			rL.registerInPlaceMiddlePos(rans, rL.getPlace(placeName));
		} else {
			rL.registerInPlace(rans, position);
		}
		return true;
	}
}
