package br.uff.tempo.middleware.resources;

import java.util.Date;

import br.uff.tempo.middleware.management.Aggregator;
import br.uff.tempo.middleware.management.Interpreter;
import br.uff.tempo.middleware.management.utils.Position;

@Deprecated
public class Person extends Aggregator {
	
	private static final long serialVersionUID = 1L;
	
	private String firstName = "";
	private String lastName = "";
	private Date birthday = null;
	private boolean isSleeping = false;
	private boolean isResting = false;
	private boolean isEating = false;
	private boolean isWalking = false;
	private boolean isRunning = false;
	
	public Person(String name, Position position) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Person", 134, position);
	}

	@ContextVariable(name = "Nome")
	public String getName() {
		return this.firstName;
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
