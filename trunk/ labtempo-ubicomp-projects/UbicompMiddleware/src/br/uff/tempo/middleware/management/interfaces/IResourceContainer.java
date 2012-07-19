package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceAgent;

public interface IResourceContainer {
	void add(ResourceAgent rA);

	void remove(String url);

	ResourceAgent get(String url);
}
