package br.uff.tempo.middleware.management;

import static br.uff.tempo.middleware.management.interfaces.IResourceRegister.rans;
import android.util.Log;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;

/**
 * Resource Register Service class.
 * 
 * It's the interface to add and remove ResourceData from ResourceRepository instance in a SmartServer
 */
public class ResourceRegister extends ResourceAgent implements IResourceRegister {
	
	private static final long serialVersionUID = 1L;

	private static ResourceRegister instance;

	private ResourceRegister() {
		super("ResourceRegister", ResourceRegister.class.getName(), IResourceRegister.rans);
	}

	/**
	 * Singleton instance that only SmartServer components can use
	 * @return reference to ResourceRegister
	 */
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
		
		try {
			ResourceNSContainer.getInstance().add(resourceData.getResourceAgentNS());
			ResourceRepository.getInstance().add(resourceData);

			notifyStakeholders("register", rans);
			return true;
		} catch (SmartAndroidRuntimeException e) {
			
			Log.e("SmartAndroid", "[ResourceRegister] Error in register method: " + e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean registerLocation(Position position, ResourceData resourceData) {
		
		try {
			ResourceNSContainer.getInstance().add(resourceData.getResourceAgentNS());

			ResourceLocation.getInstance().registerInPlace(rans, position);
			ResourceRepository.getInstance().add(resourceData);
			return true;
		} catch (SmartAndroidRuntimeException e) {
			Log.e("SmartAndroid", "[ResourceRegister] Error in registerLocation method: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Remove ResourceData of referred rans
	 * @param rans
	 * @return true if it is concluded
	 */
	public boolean unregister(String rans) {
		ResourceRepository.getInstance().remove(rans);
		ResourceNSContainer.getInstance().remove(rans);
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
