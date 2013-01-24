package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceData;

public interface IPlace {
	
	public void enter(ResourceData raData);
	
	public void exit(ResourceData raData);

}
