package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Binder;
import android.os.IBinder;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class Stove extends ResourceAgent implements IStove {
	
	private static final long serialVersionUID = 1L;
	
	//Constants
	public static final int BURNERS_NUMBER = 4;
	
	//Context Elements
	public static final String CV_ISON = "isOn";
	public static final String CV_BURNER_ON_1 = "isBurnerOn1";
	public static final String CV_BURNER_ON_2 = "isBurnerOn2";
	public static final String CV_BURNER_ON_3 = "isBurnerOn3";
	public static final String CV_BURNER_ON_4 = "isBurnerOn4";
	private static final String BURNER_ON = "isBurnerOn"; //used in a concat operation
	public static final String CV_OVEN_ON = "isOvenOn";
	public static final String CV_OVEN_TEMPERATURE = "ovenTemperature";

	// Gas Leak
	private float gasLeak = 0.0f; // Measure of natural gas leaking
	
	//Oven
	private float ovenTemp = 0.0f;
	private boolean ovenOn = false;
	
	private boolean stoveOn = false;

	//Stove Burners
	private List<Float> burners;

	public Stove() {
		this("");
	}

	public Stove(String name) {
		// FIXME: get correct id
		this(name, new Position(0, 0));
	}
	
	public Stove(String name, Position position) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Stove", 5, position);
		initBurners();
	}

	private void initBurners() {
		
		burners = new ArrayList<Float>(BURNERS_NUMBER);
		for (int i = 0; i < BURNERS_NUMBER; i++)
			burners.add(0.0f);
	}

	/*
	 * GAS LEAK
	 */
	/*
	 * @ContextVariable(name="Vazamento de gás", description="", type =
	 * CVType.On) public boolean gasLeak() { Random r = new Random(); return
	 * r.nextBoolean(); }
	 */
	@Override
	@ContextVariable(name = "Vazamento de gás", description = "", type = CVType.On)
	public float getGasLeak() {
		Random r = new Random();
		this.gasLeak = r.nextFloat();
		return this.gasLeak;
	}

	// These methods are from IStove. We have to talk about the interface
	// methods

	@Override
	public boolean isOn() {
		
		boolean ret = true;
		for (float value : burners) {
			if (value == 0f) {
				ret = false;
			}
		}
		
		//all elements from the stove must be on (burners and oven)
		ret = ret && ovenOn;
		
		notifyStakeholders(CV_ISON, ret);
		return ret;
	}

	@Override
	public void turnOnBurner(int burnerIndex) {
		burners.set(burnerIndex, 1.0f);
		notifyStakeholders(BURNER_ON + burnerIndex, true);
	}

	@Override
	public void turnOffBurner(int burnerIndex) {
		burners.set(burnerIndex, 0.0f);
		notifyStakeholders(BURNER_ON + burnerIndex, false);
	}

	@Override
	public boolean isOnBurner(int burnerIndex) {
		return burners.get(burnerIndex) > 0f;
	}

	@Override
	public void setOvenTemperature(float newTemperature) {
		this.ovenTemp = newTemperature;
		this.ovenOn = true;
		
		notifyStakeholders(CV_OVEN_TEMPERATURE, newTemperature);
	}

	@Override
	public float getOvenTemperature() {

		return this.ovenTemp;
	}

	@Override
	public void turnOffOven() {
		
		this.ovenTemp = 0f;
		
		notifyStakeholders(CV_OVEN_ON, false);
	}

	@Override
	public float getBurnerTemperature(int burnerIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isOvenOn() {
		return this.ovenOn;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
