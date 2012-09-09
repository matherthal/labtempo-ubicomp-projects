package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class Television extends ResourceAgent implements ITelevision {

	private int maxChannels = 10;
	private int channel;
	private int volume;
	private boolean on;

	public Television(String name) {

		super(name, "br.uff.tempo.middleware.resources.Television", 20);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showMessage(String text) {
		// TODO Auto-generated method stub

	}

	// begin - tv attribute
	@Override
	public int getChannel() {

		return channel;
	}

	@Override
	public void setChannel(int channel) {

		this.channel = channel;
	}

	@Override
	public boolean isOn() {

		return on;
	}

	@Override
	public void setOn(boolean on) {

		this.on = on;
	}

	@Override
	public int getVolume() {

		return volume;
	}

	@Override
	public void setVolume(int volume) {

		this.volume = volume;
	}

	@Override
	public void incChannel() {

		this.channel++;
	}

	@Override
	public void decChannel() {

		this.channel--;
	}

	@Override
	public void incVolume(int inc) {

		this.volume++;
	}

	@Override
	public void decVolume(int dec) {

		this.volume--;
	}
	// end - tv attribute
}
