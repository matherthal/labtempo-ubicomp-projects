package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceAgent;

public interface IResourceContainer {
	void add(ResourceAgent rA);
	
	void addAll(ResourceAgent... rAs);
		
	void remove(String rai);

	ResourceAgent get(String rai);
}
