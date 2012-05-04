package br.uff.tempo.middleware.comm;

public class Tuple<key, value> {
	public final String key;
	public final Object value;

	public Tuple(String key, Object value) {
		this.key = key;
		this.value = value;
	}
}
