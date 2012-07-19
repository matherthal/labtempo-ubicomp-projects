package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Local;

public interface IResourceLocation {

	public ArrayList<Local> search(ResourceAgent rA);// return sorted list by
														// proximity

	public Local get(ResourceAgent rA);

}
