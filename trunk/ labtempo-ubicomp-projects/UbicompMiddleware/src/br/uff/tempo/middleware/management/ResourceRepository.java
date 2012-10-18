package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Map;

import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	private static final long serialVersionUID = 1L;
	//FIXME swapped by directory
	private ArrayList<ResourceAgentDescription> repository = new ArrayList<ResourceAgentDescription>();;
	
	Map<Integer,ResourceData> resourceTable;
	ResourceDirectory directory;
	private static ResourceRepository instance;

	private ResourceRepository() {
		setId(0);

		directory = ResourceDirectory.getInstance();
		setRAI(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), ResourceRepository.class.getName(), "ResourceRepository"));

		setName("ResourceRepository");
		setType("management");
		
		ResourceRepository rR = this;
		ResourceDiscovery rDS = ResourceDiscovery.getInstance();
		ResourceRegister rRS = ResourceRegister.getInstance();
		ResourceLocation rLS = ResourceLocation.getInstance();
		
		ResourceContainer.getInstance().addAll(rR, rDS, rRS, rLS);

		String ip = ResourceAgentIdentifier.getLocalIpAddress();
		int prefix = ResourceAgentIdentifier.getLocalPrefix();
		
		add(IResourceRepository.rans, ip, prefix, rR.getRAI());
		add(IResourceDiscovery.rans, ip, prefix, rDS.getRAI());
		add(IResourceRegister.rans, ip, prefix, rRS.getRAI());
		add(IResourceLocation.rans, ip, prefix, rLS.getRAI());
	}

	public synchronized static ResourceRepository getInstance() {
		if (instance == null) {
			instance = new ResourceRepository();
		}
		return instance;
	}

	public String get(String url) {
		for (ResourceAgentDescription rad : repository) {
			if (rad.getRai().contains(url)) {
				return rad.getRai(); 
			}
		}
		return null;
	}

	public ArrayList<String> getList() {
		ArrayList<String> list = new ArrayList<String>();
		for (ResourceAgentDescription ra : repository) {
			list.add(ra.getRai());
		}
		return list;
	}

	public boolean add(String rans, String ip, int prefix, String rai) {
		return repository.add(new ResourceAgentDescription(new ResourceAgentNS(rans, ip, prefix), rai));
	}
	
	public boolean remove(String url) {
	
		for (ResourceAgentDescription des : repository) {
			
			if (des.getRai().equals(url)) {
				repository.remove(des);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}

	public ArrayList<String> getSubList(String url) {
		ArrayList<String> result = new ArrayList<String>();
		for (ResourceAgentDescription rad : repository) {
			if (rad.getRai().contains(url)) {
				result.add(rad.getRai()); 
			}
		}
		
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
}
