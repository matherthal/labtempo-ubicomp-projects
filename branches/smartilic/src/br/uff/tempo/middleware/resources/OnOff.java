package br.uff.tempo.middleware.resources;

import android.widget.ToggleButton;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.IOnOff;

public class OnOff extends ResourceAgent implements IOnOff{
	
	private static final long serialVersionUID = 1L;

	private boolean statusOn;
	private ToggleButton onOff;

	public OnOff(String name, String rans, ToggleButton onOff) {
		super(name, "br.uff.tempo.middleware.resources.OnOff", rans);
		statusOn = false;
		this.onOff = onOff;		
	}
	
	public OnOff(String name, String rans, Position position, ToggleButton onOff) {
		super(name, "br.uff.tempo.middleware.resources.OnOff", rans, position);
		statusOn = false;
		this.onOff = onOff;		
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		statusOn = (Boolean) value;		
	}
	
	@Service(name = "LigaDesliga", description = "", type = "TurnOnOff")
	public void setStatus(boolean isOn) {
		statusOn = isOn;
		
		notifyStakeholders("ligaDesliga", isOn);
	}

	@Override
	public boolean isOn() {
		return statusOn;
	}

	
}
