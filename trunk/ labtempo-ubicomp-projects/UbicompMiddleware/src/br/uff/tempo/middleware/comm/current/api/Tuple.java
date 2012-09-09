package br.uff.tempo.middleware.comm.current.api;

import java.io.Serializable;

public class Tuple<key, value> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String key = "";
	public Object value = null;
	public Object value2 = null;

	public Tuple(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public Tuple(Object value, Object value2) {
		this.value = value;
		this.value2 = value2;
	}
}
