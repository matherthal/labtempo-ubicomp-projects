package br.uff.tempo.middleware.resources.interfaces;

import java.util.List;

import br.uff.tempo.apps.reminder.Prescription;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IPrescriptionManager extends IResourceAgent {

	void addPrescription(Prescription p);
	
	Prescription getPrescription(Prescription p);
	
	void removePrescription(Prescription p);
	
	void fire(Prescription p);
	
	List<Prescription> getPrescriptions();
}
