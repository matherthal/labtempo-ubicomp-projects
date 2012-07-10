package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;

import android.os.Binder;
import android.os.IBinder;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class Stove extends ResourceAgent implements IStove {
	private static final String TAG = "Stove";

	// Gas Leak
	private float gasLeak = 0.0f; // Measure of natural gas leaking

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

	@Override
	public void notificationHandler(String change) {
		
	}

	/*
	 * GAS LEAK
	 */
	/*@ContextVariable(name="Vazamento de gás", description="", type = CVType.On)
	public boolean gasLeak() {
		Random r = new Random();
		return r.nextBoolean();
	}*/
	@Override
	@ContextVariable(name="Vazamento de gás", description="", type = CVType.On)
	public float getGasLeak() {
		Random r = new Random();
		this.gasLeak = r.nextFloat();
		return this.gasLeak;
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
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "getOvenTemperature", newTemperature));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public float getOvenTemperature() {
//		Random r = new Random();
//		this.tempOven1 = 80 + r.nextFloat() * 20;
//		return this.tempOven1;
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public void turnOffOven() {
		// TODO Auto-generated method stub

	}

	@Override
	public float getBurnerTemperature(int burnerIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isOnOven() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOvenOn() {
		// TODO Auto-generated method stub
		return false;
	}
}
