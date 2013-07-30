package br.uff.tempo.middleware.resources;

import java.util.Random;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.ITeapot;

public class Teapot extends ResourceAgent implements ITeapot {
	
	private static final long serialVersionUID = 1L;
	
	public float temperature = 0.0f;
	
	public Teapot(String name, String type, String rans) {
		super(name, type, rans);
	}

	@Override
	public float getTemperature() {
		Random rand = new Random();
		return rand.nextFloat() * 10.0f + 80.0f; // FIXME: it generates random
													// numbers between 80 and
													// 100 to the temperature
	}

	@Override
	public void setTemperature(float temp) {
		this.temperature = temp;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
