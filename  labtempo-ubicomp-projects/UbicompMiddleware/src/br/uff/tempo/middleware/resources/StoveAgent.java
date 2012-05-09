package br.uff.tempo.middleware.resources;

import br.uff.tempo.*;

import java.util.List;

import br.uff.tempo.apps.MediaService;
import br.uff.tempo.middleware.management.ResourceAgent;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class StoveAgent extends ResourceAgent {
	private static final String TAG = "StoveAgent";
	
	private List<Object> components;
	private static final int SOUND_ID_STOVE = R.raw.stove;
	private static final int SOUND_ID_OVEN_OPEN = R.raw.ovenopen;
	private static final int SOUND_ID_OVEN_CLOSE = R.raw.ovenclose;
	
	public StoveAgent()
	{
		super("fogao",5);
		setType("stove");
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//register();
	}

    /*
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class StoveBinder extends Binder {
    	StoveAgent getService() {
            return StoveAgent.this;
    	}
    }
	
    public StoveBinder mBinder = new StoveBinder();
    public IBinder onBind()
    {
    	return mBinder;
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

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

}
