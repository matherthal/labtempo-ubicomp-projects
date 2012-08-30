package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import android.widget.ToggleButton;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.CVType;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.ContextVariable;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.IOnOff;

public class OnOff extends ResourceAgent implements IOnOff{

	private boolean statusOn;
	private ToggleButton onOff;

	public OnOff(String name, ToggleButton onOff) {
		super(name, "br.uff.tempo.middleware.resources.OnOff", 0);
		statusOn = false;
		this.onOff = onOff;		
	}
	
	public OnOff(String name, Position position, ToggleButton onOff) {
		super(name, "br.uff.tempo.middleware.resources.OnOff", 0, position);
		statusOn = false;
		this.onOff = onOff;		
	}

	@Override
	public void notificationHandler(String change) {
		statusOn = (Boolean) JSONHelper.getChange("value", change);		
	}
	
	@ContextVariable(name = "ligaDesliga", description = "", type = CVType.On)
	public void setStatus(boolean isOn) {
		statusOn = isOn;
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "ligaDesliga", isOn));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isOn() {
		return statusOn;
	}

	
}
