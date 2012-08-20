package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.Local;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceLocation {

	public ArrayList<Local> search(ResourceAgent rA);// return sorted list by
														// proximity

	public Local get(ResourceAgent rA);
	public Local getLocal(Position position);

}
