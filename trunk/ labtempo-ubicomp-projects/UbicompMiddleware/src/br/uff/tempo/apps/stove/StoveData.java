package br.uff.tempo.apps.stove;

import br.uff.tempo.apps.ResourceData;


public class StoveData implements ResourceData {

	//A list with stove's burners intensity
	private int[] burners;
	private float ovenTemp = 0.0f;
	
	public StoveData(int burners) {
		this.burners = new int[burners];
	}
	
	public int getBurnerIntensity(int burner) {
		return this.burners[burner];
	}
	
	public int[] getBurners() {
		return this.burners;
	}

	public void setBurnerIntensity(int burner, int intensity) {
		this.burners[burner] = intensity;		
	}
	
	public void setOvenTemperature(float temp) {
		this.ovenTemp = temp;
	}
	
	public boolean isOvenOn() {
		return this.ovenTemp != 0.0f;
	}
	
	public boolean isAnyBurnerOn() {
		
		boolean ret = false;
		for (int intensity : burners) {
			if (intensity > 0)
				return true;
		}
		
		return false;
	}
	
	
}
