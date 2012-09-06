package br.uff.tempo.middleware.management.utils;

public class Position {

	int x;
	int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
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
	
	// distancia der AB = raiz de ( Xa - Xb )^2 + ( Ya - Yb )^2
	public double getDistance(Position position) {
		return Math.sqrt(Math.pow(position.x-x, 2)+Math.pow(position.y-y, 2));
	}
	
	//proposital don't override toString
	public String print() {
		return "{x="+x+", y="+y+"}";
	}
}
