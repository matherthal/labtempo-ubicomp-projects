package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class Television extends ResourceAgent implements ITelevision {
	private static final String TAG = "Television";
	private static final long serialVersionUID = 1L;

	private int channel;
	private int volume;
	private boolean on;
	
	private String message = "";

	public Television(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.Television", rans);
	}
	
	public Television(String name, String rans, Position position) {
		super(name, "br.uff.tempo.middleware.resources.Television", rans, position);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showMessage(String text) {
		Log.i(TAG, this.getName() + " - show message: " + text);
		this.message = text;
		notifyStakeholders("showMessage", text);
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	// begin - tv attribute
	@Override
	public int getChannel() {
		return channel;
	}

	@Override
	public void setChannel(int channel) {
		Log.i(TAG, this.getName() + " - set channel: " + channel);
		this.channel = channel;
		notifyStakeholders("getChannel", channel);
	}

	@Override
	public boolean isOn() {
		return on;
	}

	@Override
	public void setOn(boolean on) {
		Log.i(TAG, this.getName() + " - turned on");
		this.on = on;
		notifyStakeholders("isOn", on);
	}

	@Override
	public int getVolume() {
		return volume;
	}

	@Override
	public void setVolume(int volume) {
		Log.i(TAG, this.getName() + " - set volume: " + volume);
		this.volume = volume;
		notifyStakeholders("getVolume", volume);
	}

	@Override
	public void incChannel() {
		this.channel++;
		Log.i(TAG, this.getName() + " - channel changed to: " + channel);
		notifyStakeholders("getChannel", channel);
	}

	@Override
	public void decChannel() {
		this.channel--;
		Log.i(TAG, this.getName() + " - channel changed to: " + channel);
		notifyStakeholders("getChannel", channel);
	}

	@Override
	public void incVolume(int inc) {
		this.volume++;
		Log.i(TAG, this.getName() + " - volume changed to: " + volume);
		notifyStakeholders("getVolume", volume);
	}

	@Override
	public void decVolume(int dec) {
		this.volume--;
		Log.i(TAG, this.getName() + " - volume changed to: " + volume);
		notifyStakeholders("getVolume", volume);
	}
	// end - tv attribute
}
