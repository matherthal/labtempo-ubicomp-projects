package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Map;

import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	private static final long serialVersionUID = 1L;
	//FIXME swapped by directory
//	private ArrayList<ResourceAgentDescription> repository = new ArrayList<ResourceAgentDescription>();;
	
	Map<Integer,ResourceData> resourceTable;
	ResourceDirectory directory;
	private static ResourceRepository instance;

	private ResourceRepository() {
		super("ResourceRepository", ResourceRepository.class.getName(), IResourceRepository.rans);
		
		directory = ResourceDirectory.getInstance();
	}
	
	/**
	 * Singleton instance that only SmartServer components can use
	 * @return reference to ResourceRepository
	 */
	public synchronized static ResourceRepository getInstance() {
		if (instance == null) {
			instance = new ResourceRepository();
		}
		return instance;
	}
	
	@Override
	public boolean identify() {
		String ip = SmartAndroid.getLocalIpAddress();
		int prefix = SmartAndroid.getLocalPrefix();
		
		ResourceContainer.getInstance().add(this);
		ResourceAgentNS raNS = new ResourceAgentNS(this.getRANS(), ip, prefix);
		ResourceNSContainer.getInstance().add(raNS);
		ResourceRepository.getInstance().add(new ResourceData(this.getRANS(), this.getName(), this.getType(), null, null, raNS));
		return true;
	}
	
	public ResourceData get(String rans) {
		return directory.read(ResourceData.RANS, rans).get(0);
	}
	
	public ResourceAgentNS getRANS(String rans) {
		return directory.read(ResourceData.RANS, rans).get(0).getResourceAgentNS();
	}

	/**
	 * Create new resource data
	 * @param resourceData
	 * @return true if it was successful 
	 */
	public boolean add(ResourceData resourceData) {
		directory.create(resourceData);
		return true;
	}
	
	/**
	 * Remove the resource data from directory
	 * @param rans
	 * @return true if it was successful
	 */
	public boolean remove(String rans) {
		directory.delete(directory.read(ResourceData.RANS, rans).get(0));	
		return true;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		
	}

	
}
