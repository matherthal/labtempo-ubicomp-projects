package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.ICellPhone;

public class CellPhone extends ResourceAgent implements ICellPhone {

	private String number;
	private boolean isSmsComming;
	private boolean isCallComming;
		
	public CellPhone(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.Lamp", rans);
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isSmsComming() {
		return isSmsComming;
	}

	public void setSmsComming(boolean isSmsComming) {
		this.isSmsComming = isSmsComming;
	}

	public boolean isCallComming() {
		return isCallComming;
	}

	public void setCallComming(boolean isCallComming) {
		this.isCallComming = isCallComming;
	}

	

}
