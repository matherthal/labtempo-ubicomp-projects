package br.uff.tempo.middleware.resources;

import java.util.Date;

import br.uff.tempo.middleware.management.Aggregator;
import br.uff.tempo.middleware.management.Interpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.resources.interfaces.IPerson;

public class Person extends Aggregator implements IPerson {
	
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String lastName = "";
	private Date birthday = null;
	private boolean isSleeping = false;
	private boolean isResting = false;
	private boolean isEating = false;
	private boolean isWalking = false;
	private boolean isRunning = false;

	public Person() {
		// Discover ContextVariables to update this aggregator status
		// Add them with the aggregator's support
		IResourceDiscovery iRDS = getRDS();
		//IBed bed = new BedStub(iRDS.search("bed").get(0));
		//bed.registerStakeholder("inUse", this.getURL());

		//this.addContextVariable(bed, "inUse");
		// ...
		// Add interpreters
		// ...
	}

	@ContextVariable(name = "Nome")
	public String getName() {
		return this.name;
	}

	@ContextVariable(name = "Dormindo")
	public boolean getIsSleepting() {
		return this.isSleeping;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// Update status of this aggregator
	}

	@Override
	public boolean addContextVariable(Interpreter interpreter) {
		// TODO Auto-generated method stub
		return false;
	}
}
