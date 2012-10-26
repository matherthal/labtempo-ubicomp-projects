package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Map;

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
		super("ResourceRepository", ResourceRepository.class.getName(), IResourceRepository.rans);
		
		directory = ResourceDirectory.getInstance();
	}

	public synchronized static ResourceRepository getInstance() {
		if (instance == null) {
			instance = new ResourceRepository();
		}
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

	public String get(String url) {
		for (ResourceAgentDescription rad : repository) {
			if (rad.getRai().contains(url)) {
				return rad.getRai(); 
			}
		}
		return null;
	}
	
	public ResourceAgentNS getRANS(String rans) {
		for (ResourceAgentDescription rad : repository) {
			if (rad.getRaNS().getRans().equals(rans)) {
				return rad.getRaNS(); 
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

	public boolean add(String rans, String ip, int prefix) {
		return repository.add(new ResourceAgentDescription(new ResourceAgentNS(rans, ip, prefix), rans));
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
