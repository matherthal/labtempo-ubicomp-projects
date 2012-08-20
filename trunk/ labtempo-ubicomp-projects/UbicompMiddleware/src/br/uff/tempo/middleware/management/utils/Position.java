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
}
