package br.uff.tempo.apps.map.dialogs;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.rule.RuleComposeBar;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.RuleComposer;

public class RuleToolbar extends MapDialog {
	private static final String TAG = "RuleToolbar";

	private IDialogFinishHandler handler;
	private RuleComposer composer = new RuleComposer();

	private RuleComposeBar composeBar;
	
	// Context Variable 1
	private String rai1;
	private String cv1;
	private Object[] params1;
	// Comparison operator
	private Operator op;
	// Context Variable 2
	private String rai2;
	private String cv2;
	private Object[] params2;
	// Comparison timer
	private long timeout;
	// Comparison value (either this is used or the context variable 2)
	private Object value;
	
	public RuleToolbar(final Activity act, final IDialogFinishHandler handler) {

		super(act, R.layout.rule_toolbar, R.string.title_rule_composer);
		this.handler = handler;
	}
	
	public RuleToolbar(final Activity act) {
		this(act, (IDialogFinishHandler) act);
		Log.d(TAG, "OK");
	}

	@Override
	public void onClick(View view) {
	}

	// ===========================================================
	// UI Events
	// ===========================================================

	public void onRuleName(View v) {
		String name = "";//TODO: get from editbox
		composer.setRuleName(name);
	}
	
	public void onAndClick(View v) {
		composer.addAndClause();
		composeBar.appendTextLine("AND");
	}

	public void onOrClick(View v) {
		composer.addOrClause();
		composeBar.appendTextLine("OR");
	}

	public void onNotClick(View v) {
		composer.addNotClause();
		composeBar.appendTextLine("NOT");
	}

	public void onOpenBracketClick(View v) {
		composer.addOpenBracket();
		composeBar.appendTextLine("(");
		composeBar.openBracket();
	}

	public void onCloseBracketClick(View v) {
		composer.addCloseBracket();
		composeBar.closeBracket();
		composeBar.appendTextLine(")");		
	}

	public void onEqualClick(View v) {
		this.op = Operator.Equal;
		composeBar.appendTextLine("=");
	}

	public void onNotEqualClick(View v) {
		this.op = Operator.Different;
		composeBar.appendTextLine("!=");
	}

	public void onLessThanClick(View v) {
		this.op = Operator.LessThan;
		composeBar.appendTextLine("<");
	}

	public void onGreaterThanClick(View v) {
		this.op = Operator.GreaterThan;
		composeBar.appendTextLine(">");
	}

	public void onLessThanOrEqualClick(View v) {
		this.op = Operator.LessThanOrEqual;
		composeBar.appendTextLine("<=");
	}

	public void onGreaterThanOrEqualClick(View v) {
		this.op = Operator.GreaterThanOrEqual;
		composeBar.appendTextLine(">=");
	}

	public void onTimerClick(View v) {
		long timeout = 0; //TODO: get from edit box
		composer.addExpressionTimer(timeout);
		composeBar.appendTextLine("FOR " + timeout/60 + " MIN");
	}
	
	public void onSetConstantClick(View v) {
		//this.value;
		composeBar.appendTextLine(this.value.toString());
	}
	
	public void onChooseCVClick(View v) {
		
	}
	
	public void setContextVar(String rans, String name) {
		
	}

	public void btnEndRuleCreation() {
		try {
			composer.finish(this.activity.getBaseContext());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void linkToComposeBar(RuleComposeBar bar) {
		composeBar = bar;
	}
}
