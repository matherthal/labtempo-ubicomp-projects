package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

public class Local extends ResourceAgent{

	String name;
	
	Position lower;// left lower vertex
	Position upper;// right upper vertex

	public Local(Position lower, Position upper)// It defines a rectangle from
												// the vertex
	{
		this.lower = lower;
		this.upper = upper;
	}

	public Position getLower() {
		return lower;
	}

	public void setLower(Position lower) {
		this.lower = lower;
	}

	public Position getUpper() {
		return upper;
	}

	public void setUpper(Position upper) {
		this.upper = upper;
	}

	public boolean equalCorners(Local local) {
		return this.upper.equals(local.lower) || this.getLower().equals(local.getUpper());
	}

	public boolean equalSide(Local local) {
		boolean equalRight = this.upper.getX() == local.lower.getX();
		boolean equalLeft = this.lower.getX() == local.upper.getX();
		boolean equalUpper = false;
		boolean equalLower = false;
		if (equalRight || equalLeft) {
			equalUpper = this.upper.getY() >= local.lower.getY() && this.lower.getY() <= local.lower.getY();
			equalLower = this.upper.getY() >= local.upper.getY() && this.lower.getY() <= local.upper.getY();
		}
		equalRight = equalRight && (equalUpper || equalLower);
		equalLeft = equalLeft && (equalUpper || equalLower);

		return equalRight || equalLeft;
	}

	public boolean equalHeight(Local local) {
		boolean equalUp = this.upper.getY() == local.lower.getY();
		boolean equalDown = this.lower.getY() == local.upper.getY();
		boolean equalRight = false;
		boolean equalLeft = false;
		if (equalUp || equalDown) {
			equalRight = this.upper.getX() >= local.lower.getX() && this.lower.getX() <= local.lower.getX();
			equalLeft = this.upper.getX() >= local.upper.getX() && this.lower.getX() <= local.upper.getX();
		}
		equalUp = equalUp && (equalRight || equalLeft);
		equalDown = equalDown && (equalRight || equalLeft);

		return equalUp || equalDown;
	}

	public boolean contains(Position position)
	{
		return (position.compareTo(lower)==1 && position.compareTo(upper)==3); 
	}
	
	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

}
