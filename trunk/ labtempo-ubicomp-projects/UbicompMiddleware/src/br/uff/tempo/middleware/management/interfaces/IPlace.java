package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.utils.Position;

public interface IPlace {

	public String getName();

	public void setName(String name);

	public Position getLower();

	public void setLower(Position lower);

	public Position getUpper();

	public void setUpper(Position upper);

	public boolean equalCorners(IPlace local);

	public boolean equalSide(IPlace local);

	public boolean equalHeight(IPlace local);

	public boolean contains(Position position);

}
