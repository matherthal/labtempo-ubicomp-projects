package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceLocation {

	public ArrayList<Place> search(ResourceAgent rA);// return sorted list by
														// proximity

	public Place get(ResourceAgent rA);
	public Place getLocal(Position position);

}
