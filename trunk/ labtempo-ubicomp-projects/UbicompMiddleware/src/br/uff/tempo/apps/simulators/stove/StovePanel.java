package br.uff.tempo.apps.simulators.stove;

import android.content.Context;
import android.util.AttributeSet;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public abstract class StovePanel extends AbstractPanel {

	protected static IStove agent;
	
	public StovePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setAgent(IResourceAgent agent) {
		this.agent = (IStove) agent;
	}
	
	@Override
	public IResourceAgent getAgent() {
		return this.agent;
	}
}
