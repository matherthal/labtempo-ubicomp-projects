package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.interfaces.IPlace;
import br.uff.tempo.middleware.management.utils.Position;

public class Place extends ResourceAgent implements IPlace {
	
	private static final long serialVersionUID = 1L;

	String name;

	Position lower;// left lower vertex
	Position upper;// right upper vertex

	public Place(String name, Position lower, Position upper)// It defines a rectangle from
												// the vertex
	{
		//super(name, "br.uff.tempo.middleware.management.Place", 0);
		this.name = name;
		this.lower = lower;
		this.upper = upper;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean equalCorners(IPlace local) {
		return this.upper.equals(local.getLower()) || this.getLower().equals(local.getUpper());
	}

	public boolean equalSide(IPlace local) {
		boolean equalRight = this.upper.getX() == local.getLower().getX();
		boolean equalLeft = this.lower.getX() == local.getUpper().getX();
		boolean equalUpper = false;
		boolean equalLower = false;
		if (equalRight || equalLeft) {
			equalUpper = this.upper.getY() >= local.getLower().getY() && this.lower.getY() <= local.getLower().getY();
			equalLower = this.upper.getY() >= local.getUpper().getY() && this.lower.getY() <= local.getUpper().getY();
		}
		equalRight = equalRight && (equalUpper || equalLower);
		equalLeft = equalLeft && (equalUpper || equalLower);

		return equalRight || equalLeft;
	}

	public boolean equalHeight(IPlace local) {
		boolean equalUp = this.upper.getY() == local.getLower().getY();
		boolean equalDown = this.lower.getY() == local.getUpper().getY();
		boolean equalRight = false;
		boolean equalLeft = false;
		if (equalUp || equalDown) {
			equalRight = this.upper.getX() >= local.getLower().getX() && this.lower.getX() <= local.getLower().getX();
			equalLeft = this.upper.getX() >= local.getUpper().getX() && this.lower.getX() <= local.getUpper().getX();
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
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
