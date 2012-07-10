package br.uff.tempo.apps.tv;

import br.uff.tempo.apps.ResourceData;

public class TvData implements ResourceData {

	private int channel;
	private boolean on;
	private int volume;
	
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	
}
