package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.apps.reminder.Prescription;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IPrescriptionManager;

public class PrescriptionManagerStub extends ResourceAgentStub implements
		IPrescriptionManager {

	private static final long serialVersionUID = 1L;

	public PrescriptionManagerStub(String rai) {
		super(rai);
	}

	@Override
	public void addPrescription(Prescription p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Prescription.class.getName(), p));

		makeCall("addPrescription", params, void.class);
	}

	@Override
	public Prescription getPrescription(Prescription p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Prescription.class.getName(), p));

		return (Prescription) makeCall("getPrescription", params,
				Prescription.class);
	}

	@Override
	public void removePrescription(Prescription p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Prescription.class.getName(), p));

		makeCall("removePrescription", params, void.class);
	}

	@Override
	public void fire(Prescription p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Prescription.class.getName(), p));

		makeCall("fire", params, void.class);
	}

	@Override
	public List<Prescription> getPrescriptions() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (List<Prescription>) makeCall("fire", params, List.class);
	}
}
