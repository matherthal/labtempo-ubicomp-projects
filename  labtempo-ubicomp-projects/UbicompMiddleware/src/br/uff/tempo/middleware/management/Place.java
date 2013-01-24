package br.uff.tempo.middleware.management;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.uff.tempo.middleware.management.interfaces.IPlace;
import br.uff.tempo.middleware.management.utils.Position;

public class Place implements IPlace,Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;

	Map<String, ResourceData> raDir;
	
	Position lower;// left lower vertex
	Position upper;// right upper vertex

	public Place(String name, Position lower, Position upper)// It defines a rectangle from
												// the vertex
	{
		//super(name, "br.uff.tempo.middleware.management.Place", 0);
		this.lower = lower;
		this.upper = upper;
		this.name = name;
		raDir = new HashMap<String, ResourceData>();
	}
	
	public void enter(ResourceData ra) {
		raDir.put(ra.getRai(), ra);
	}
	
	public void exit(ResourceData ra) {
		raDir.remove(ra.getRai());
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

	public boolean equalCorners(Place local) {
		return this.upper.equals(local.getLower()) || this.getLower().equals(local.getUpper());
	}

	public boolean equalSide(Place local) {
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

	public boolean equalHeight(Place local) {
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
	
}
