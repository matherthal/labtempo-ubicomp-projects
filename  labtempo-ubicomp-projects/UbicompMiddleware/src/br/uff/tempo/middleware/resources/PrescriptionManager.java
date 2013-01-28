package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.apps.reminder.Prescription;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IPrescriptionManager;

public class PrescriptionManager extends ResourceAgent implements IPrescriptionManager {
	
	private static final long serialVersionUID = 8001461243364286356L;
	
	public static final String PRESCRIPTION_NEW = "prescriptionNew";
	public static final String PRESCRIPTION_REMOVED = "PrescriptionRemoved";
	public static final String PRESCRIPTION_FIRED = "removedPrescription";

	private List<Prescription> prescriptions;
	
	public PrescriptionManager(String name, String rans) {
		super(name, PrescriptionManager.class.getCanonicalName(), rans);
		prescriptions = new ArrayList<Prescription>();
	}

	@Override
	public void addPrescription(Prescription p) {
		prescriptions.add(p);
		notifyStakeholders(PRESCRIPTION_NEW, p);
	}
	
	@Override
	public Prescription getPrescription(Prescription p) {
		int index = prescriptions.indexOf(p);
		return prescriptions.get(index);
	}
	
	@Override
	public void removePrescription(Prescription p) {
		prescriptions.remove(p);
		notifyStakeholders(PRESCRIPTION_REMOVED, p);
	}
	
	@Override
	public void fire(Prescription p) {
		if (!prescriptions.contains(p)) {
			addPrescription(p);
		}
		
		notifyStakeholders(PRESCRIPTION_FIRED, p);
	}
	
	@Override
	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {}

}
