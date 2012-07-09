package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Binder;
import android.os.IBinder;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class Stove extends ResourceAgent implements IStove {
	private static final String TAG = "Stove";

	// Temperatures
	private float tempCooktop1 = 0.0f;
	private float tempCooktop2 = 0.0f;
	private float tempCooktop3 = 0.0f;
	private float tempCooktop4 = 0.0f;
	private float tempOven1 = 0.0f;

	// Gas Leak
	private float gasLeak = 0.0f; // Measure of natural gas leaking

	// ON/OFF
	private boolean onCooktop1 = false;
	private boolean onCooktop2 = false;
	private boolean onCooktop3 = false;
	private boolean onCooktop4 = false;
	private boolean onOven1 = false;

	// private List<Object> components;
	// private static final int SOUND_ID_STOVE = R.raw.stove;
	// private static final int SOUND_ID_OVEN_OPEN = R.raw.ovenopen;
	// private static final int SOUND_ID_OVEN_CLOSE = R.raw.ovenclose;

	private List<Float> burners;

	public Stove() {
		this("");
	}
	
	public Stove(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Stove", 5); 
		burners = new ArrayList<Float>(4);
		initBurners();
	}

	private void initBurners()
	{
		for (int i = 0; i<4; i++)
			burners.add(0.0f);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (burners.size()<4)
		{
			burners = new ArrayList<Float>(4);
		}
	}

	/*
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class StoveBinder extends Binder {
		Stove getService() {
			return Stove.this;
		}
	}

	public StoveBinder mBinder = new StoveBinder();

	public IBinder onBind() {
		return mBinder;
	}

	/*
	 * public void addComponent(Object comp) { if (testComponent(comp))
	 * components.add(comp); }
	 * 
	 * public void removeComponent(Object comp) { if (testComponent(comp))
	 * components.remove(comp); }
	 * 
	 * private boolean testComponent(Object comp) { if (comp instanceof
	 * CooktopElement || comp instanceof Oven) return true; else Log.i(TAG,
	 * "Device component " + comp.getClass().toString() +
	 * " doesn't belongs to Stove."); return false; }
	 * 
	 * public class CooktopElement { private Boolean enabled = false; private
	 * float temperatureC = 50.0f;
	 * 
	 * public Boolean isEnabled() { return enabled; }
	 * 
	 * public void enable(Boolean enabled) { //Send destination of stove's sound
	 * to be played Intent i = new Intent(Stove.this, MediaService.class);
	 * Bundle b = new Bundle(); b.putInt(MediaService.SOUND_ID, SOUND_ID_STOVE);
	 * i.putExtras(b); //Starting media player service startService(i);
	 * 
	 * //Set cooktop element as enabled this.enabled = enabled; }
	 * 
	 * public void disable(Boolean enabled) { stopService(new Intent(Stove.this,
	 * MediaService.class)); this.enabled = enabled; }
	 * 
	 * public float getTemperatureC() { return temperatureC; }
	 * 
	 * public void setTemperatureC(float temperatureC) { this.temperatureC =
	 * temperatureC; } }
	 * 
	 * public class Oven { private Boolean enabled = false; private float
	 * temperatureC = 50.0f;
	 * 
	 * public Boolean isEnabled() { return enabled; }
	 * 
	 * public void setEnabled(Boolean enabled) { this.enabled = enabled; }
	 * 
	 * public float getTemperatureC() { return temperatureC; }
	 * 
	 * public void setTemperatureC(float temperatureC) { this.temperatureC =
	 * temperatureC; } }
	 */

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

	/*
	 * COOKTOP ELEMENT TEMPERATURES
	 */
	@ContextVariable(name = "Temperatura boca 1", type = CVType.Temperature)
	public float getTemperatureCooktop1() {
		if (this.onCooktop1) {
			Random r = new Random();
			this.tempCooktop1 = 80 + r.nextFloat() * 20;
		} else {
			this.tempCooktop1 = 30.0f;
		}
		return this.tempCooktop1;
	}

	@ContextVariable(name = "Temperatura boca 2", type = CVType.Temperature)
	public float getTemperatureCooktop2() {
		if (this.onCooktop2) {
			Random r = new Random();
			this.tempCooktop2 = 80 + r.nextFloat() * 20;
		} else {
			this.tempCooktop2 = 30.0f;
		}
		return this.tempCooktop2;
	}

	@ContextVariable(name = "Temperatura boca 3", type = CVType.Temperature)
	public float getTemperatureCooktop3() {
		if (this.onCooktop3) {
			Random r = new Random();
			this.tempCooktop3 = 80 + r.nextFloat() * 20;
		} else {
			this.tempCooktop3 = 30.0f;
		}
		return this.tempCooktop3;
	}

	@ContextVariable(name = "Temperatura boca 4", type = CVType.Temperature)
	public float getTemperatureCooktop4() {
		if (this.onCooktop4) {
			Random r = new Random();
			this.tempCooktop4 = 80 + r.nextFloat() * 20;
		} else {
			this.tempCooktop4 = 30.0f;
		}
		return this.tempCooktop4;
	}

	/*
	 * OVEN TEMPERATURE
	 */
	@ContextVariable(name = "Temperatura forno", type = CVType.Temperature)
	public float getTemperatureOven() {
		Random r = new Random();
		this.tempOven1 = 80 + r.nextFloat() * 20;
		return this.tempOven1;
	}

	/*
	 * STOVE ON/OFF
	 */
	@ContextVariable(name = "Está Ligado", description = "", type = CVType.On)
	public boolean getisOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	/*
	 * OVEN ON/OFF
	 */
	@ContextVariable(name = "Está Ligado", description = "", type = CVType.On)
	public boolean getOvenIsOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	/*
	 * COOKTOP ELEMENT ON/OFF
	 */
	@ContextVariable(name = "Boca 1 Ligada", description = "", type = CVType.On)
	public boolean getCooktop1IsOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	@ContextVariable(name = "Boca 2 Ligada", description = "", type = CVType.On)
	public boolean getCooktop2IsOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	@ContextVariable(name = "Boca 3 Ligada", description = "", type = CVType.On)
	public boolean getCooktop3IsOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	@ContextVariable(name = "Boca 4 Ligada", description = "", type = CVType.On)
	public boolean getCooktop4IsOn() {
		Random r = new Random();
		return r.nextBoolean();
	}

	/*
	 * GAS LEAK
	 */
	@ContextVariable(name = "Vazamento de gás", description = "", type = CVType.On)
	public boolean gasLeak() {
		Random r = new Random();
		return r.nextBoolean();
	}

	// These methods are from IStove. We have to talk about the interface methods

	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void turnOnBurner(int burnerIndex) {
		burners.set(burnerIndex, 1.0f);
	}

	@Override
	public void turnOffBurner(int burnerIndex) {
		burners.set(burnerIndex, 0.0f);
	}

	@Override
	public boolean isOnBurner(int burnerIndex) {
		return burners.get(burnerIndex)>0;
	}

	@Override
	public void setOvenTemperature(float newTemperature) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getOvenTemperature() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void turnOffOven() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOvenOn() {
		// TODO Auto-generated method stub
		return false;
	}

}
