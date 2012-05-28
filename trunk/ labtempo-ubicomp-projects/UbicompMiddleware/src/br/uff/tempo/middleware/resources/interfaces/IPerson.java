package br.uff.tempo.middleware.resources.interfaces;

import java.util.Date;

public interface IPerson {
	//Attributes
	public String name = "";
	public String lastName = "";
	public Date birthday = new Date();
	public boolean isSleeping = false;
	public boolean isResting = false;
	public boolean isEating = false;
	public boolean isWalking = false;
	public boolean isRunning = false;
	
	//Methods
	public Date getAge();
}
