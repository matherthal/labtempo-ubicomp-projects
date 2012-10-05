package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;

public class Position implements Serializable {

	float x;
	float y;

	public Position(float x, float y) {
		
		this.x = roundTo2(x);
		this.y = roundTo2(y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	//return quadrant number (in clockwise direction: 1: ++; 2: -+; 3: --; 4: +-;)
	public int compareTo(Position position) {
		int result = -1;
		if (x>=position.x){ 
			if (y>=position.y){
				result = 1;
			} else {
				result = 4;
			}
		} else
		{
			if (y>=position.y){
				result = 2;
			} else {
				result = 3;
			}				
		}
		return result;
	}
	
	private static float roundTo(float value, long factor) {
		
		value = value * factor;
	    long tmp = Math.round(value);
	    
	    return (float) tmp / factor;
	}
	
	public static float roundTo(float value, int places) {

		if (places < 0) {
			throw new IllegalArgumentException("Number of places can't be < 0");
		}
		
	    long factor = (places == 1) ? 10 : (long) Math.pow(10, places);
	    
	    return roundTo(value, factor);
	}
	
	public static float roundTo2(float value) {
		
		return roundTo(value, 100L);
	}
	
	// distancia der AB = raiz de ( Xa - Xb )^2 + ( Ya - Yb )^2
	public double getDistance(Position position) {
		
		float a = position.x - x;
		float b = position.y - y;
		
		return roundTo2((float) Math.sqrt(a * a + b * b));
	}
	
	//proposital don't override toString
	public String print() {
		return "{x="+x+", y="+y+"}";
	}
	
	public String toString() {
		return print();
	}
}
