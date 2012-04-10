package br.uff.tempo.testeRPC;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StoveAgent extends AgentBase {
	private static final String TAG = "StoveAgent";
	
	private List<Object> components;
	private static final int SOUND_ID_STOVE = R.raw.stove;
	private static final int SOUND_ID_OVEN_OPEN = R.raw.ovenopen;
	private static final int SOUND_ID_OVEN_CLOSE = R.raw.ovenclose;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	public void addComponent(Object comp)
	{
		if (testComponent(comp))
			components.add(comp);
	}
	
	public void removeComponent(Object comp)
	{
		if (testComponent(comp))
			components.remove(comp);
	}
	
	private boolean testComponent(Object comp)
	{
		if (comp instanceof CooktopElement || comp instanceof Oven)
			return true;
		else
			Log.i(TAG, "Device component " + comp.getClass().toString() + " doesn't belongs to Stove.");
		return false;
	}

	public class CooktopElement {
		private Boolean enabled = false;
		private float temperatureC = 50.0f; 

		public Boolean isEnabled() {
			return enabled;
		}

		public void enable(Boolean enabled) {
			//Send destination of stove's sound to be played
			Intent i = new Intent(StoveAgent.this, MediaService.class);
			Bundle b = new Bundle();
			b.putInt(MediaService.SOUND_ID, SOUND_ID_STOVE);
			i.putExtras(b);
			//Starting media player service
			startService(i);
			
			//Set cooktop element as enabled
			this.enabled = enabled;
		}

		public void disable(Boolean enabled) {
			stopService(new Intent(StoveAgent.this, MediaService.class));
			this.enabled = enabled;
		}

		public float getTemperatureC() {
			return temperatureC;
		}

		public void setTemperatureC(float temperatureC) {
			this.temperatureC = temperatureC;
		}
	}

	public class Oven {
		private Boolean enabled = false;
		private float temperatureC = 50.0f; 

		public Boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public float getTemperatureC() {
			return temperatureC;
		}

		public void setTemperatureC(float temperatureC) {
			this.temperatureC = temperatureC;
		}
	}
}
