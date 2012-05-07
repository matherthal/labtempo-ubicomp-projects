package br.uff.tempo.apps.stove;

import android.graphics.Bitmap;

public class StoveData {

	//A list with stove's burners intensity
	private int[] burners;
	
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
}
