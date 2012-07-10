package br.uff.tempo.middleware.management.interfaces;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.uff.tempo.middleware.management.Interpreter;

public interface IAggregator {
	/*
	 * Add CV into Aggregator
	 */
	public boolean addContextVariable(Interpreter interpreter);
	public boolean addContextVariable(IResourceAgent ra, String method);
}
