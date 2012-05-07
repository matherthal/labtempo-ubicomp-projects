package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceLocation {
	
	public ArrayList<Local> search(ResourceAgent rA);//return sorted list by proximity
	public Local get(ResourceAgent rA);
	
}
