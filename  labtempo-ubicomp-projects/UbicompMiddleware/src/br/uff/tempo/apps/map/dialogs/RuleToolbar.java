package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.view.View;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.RuleComposer;

public class RuleToolbar extends MapDialog {

	private IDialogFinishHandler handler;
	private RuleComposer composer = new RuleComposer();

	public RuleToolbar(final Activity act, final IDialogFinishHandler handler) {

		super(act, R.layout.rule_toolbar, R.string.title_config_res);
		this.handler = handler;
	}

	public RuleToolbar(final Activity act) {
		this(act, (IDialogFinishHandler) act);
	}

	@Override
	public void onClick(View view) {
	}

	// ===========================================================
	// UI Events
	// ===========================================================

	public void onAndClick(View v) {

	}

	public void onOrClick(View v) {

	}

	public void onNotClick(View v) {

	}

	public void onOpenBracketClick(View v) {

	}

	public void onCloseBracketClick(View v) {

	}

	public void onEqualClick(View v) {

	}

	public void onNotEqualClick(View v) {

	}

	public void onLessThanClick(View v) {

	}

	public void onGreaterThanClick(View v) {

	}

	public void onTimerClick(View v) {

	}
	
	public void onSetConstantClick(View v) {

	}
	
	public void onChooseCVClick(View v) {

	}
	
	public void setContextVar(String rans, String name) {
		// TODO Auto-generated method stub
	}

}
