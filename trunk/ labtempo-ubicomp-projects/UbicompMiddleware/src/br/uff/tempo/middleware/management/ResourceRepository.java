package br.uff.tempo.middleware.management;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.interfaces.IResourceRepository;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	private static final long serialVersionUID = 1L;

	ArrayList<String> repository;
	private static ResourceRepository instance;

	private ResourceRepository() {
		setId(0);

		setURL(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceRepository", "ResourceRepository"));

		setName("ResourceRepository");
		setType("management");

		ResourceContainer container = ResourceContainer.getInstance();
		ResourceRepository rR = this;
		ResourceDiscovery rDS = ResourceDiscovery.getInstance();
		ResourceRegister rRS = ResourceRegister.getInstance();
		ResourceLocation rLS = ResourceLocation.getInstance();
		container.add(rR);
		container.add(rDS);
		container.add(rRS);
		container.add(rLS);

		repository = new ArrayList<String>();
		repository.add(rDS.getURL());
		repository.add(rRS.getURL());
		repository.add(rLS.getURL());
		repository.add(rR.getURL());
	}

	public static ResourceRepository getInstance() {
		if (instance == null)
			instance = new ResourceRepository();
		return instance;
	}

	public String get(String url) {
		for (int i = 0; i < repository.size(); i++)
			if (repository.get(i).contains(url))
				return repository.get(i);
		return null;
	}

	public ArrayList<String> getList() {
		return repository;
	}

	public boolean add(String url) {
		repository.add(url);
		return true;
	}

	public boolean remove(String url) {
		repository.remove(url);
		return true;
	}

	public boolean update(String url) {

		repository.add(url);
		return true;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// ResourceAgent rA = (ResourceAgent)new
		// update(rA);
	}

	public ArrayList<String> getSubList(String url) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < repository.size(); i++)
			if (repository.get(i).contains(url))
				result.add(repository.get(i));
		if (result.size() == 0)
			return null;
		else
			return result;
	}
}
