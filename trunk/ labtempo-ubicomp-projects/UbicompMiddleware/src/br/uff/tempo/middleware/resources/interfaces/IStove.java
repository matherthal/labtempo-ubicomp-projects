package br.uff.tempo.middleware.resources.interfaces;

public interface IStove {
	public float cooktopElem1Temp = 0.0f;
	public boolean cooktopElem1IsOn = false;
	public float cooktopElem2Temp = 0.0f;
	public boolean cooktopElem2IsOn = false;
	public float cooktopElem3Temp = 0.0f;
	public boolean cooktopElem3IsOn = false;
	public float cooktopElem4Temp = 0.0f;
	public boolean cooktopElem4IsOn = false;
	public float cooktopElem5Temp = 0.0f;
	public boolean cooktopElem5IsOn = false;
	public float cooktopElem6Temp = 0.0f;
	public boolean cooktopElem6IsOn = false;
	public float stoveTemp = 0.0f;
	public boolean stoveIsOn = false;
	
	public boolean getIsOn();
}
