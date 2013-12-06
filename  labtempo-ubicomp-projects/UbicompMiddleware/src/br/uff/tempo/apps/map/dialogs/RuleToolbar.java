package br.uff.tempo.apps.map.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;
import br.uff.tempo.apps.map.SmartMapActivity;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.ContextVariableBundle;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.RuleComposer;

public class RuleToolbar extends BaseRuleToolbar implements InputTextGetter {

	public static final int CONTEXT_VARIABLE = 0;
	public static final int CONSTANT_VALUE = CONTEXT_VARIABLE + 1;
	public static final int CONTEXT_VARIABLE_ONLY = CONSTANT_VALUE + 1;
	
	private IDialogFinishHandler handler;
	private RuleComposer ruleComposer;
	private boolean comparisonOperatorChoosed = false;
	
	//Rule condition items
	private ContextVariableBundle contextVariable = null;
	private Operator operator = null;
	private Object cte = null;

	public RuleToolbar(final Activity act, final IDialogFinishHandler handler, RuleComposer ruleComposer) {
		super(act);
		this.handler = handler;
		this.setComposer(ruleComposer);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	}

	public RuleToolbar(final Activity act, RuleComposer ruleComposer) {
		this(act, null, ruleComposer);
	}
	
	public void setComposer(RuleComposer ruleComposer) {
		this.ruleComposer = ruleComposer;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMddHHmmss");
		Date dt = new Date();
		String myDate = sdf.format(dt);
		this.ruleComposer.setRuleName("RULE_" + myDate);
	}

	// ===========================================================
	// UI Events
	// ===========================================================

	@Override
	public void onAndClick(View v) {
		// case CV has been chosen, but and operator and value are not, then write it down
		if (this.contextVariable != null)
			comparableClicked(CONTEXT_VARIABLE_ONLY);
		
		this.ruleComposer.addAndClause();
		comparisonOperatorChoosed = false;
	}

	@Override
	public void onOrClick(View v) {
		// case CV has been chosen, but and operator and value are not, then write it down
		if (this.contextVariable != null)
			comparableClicked(CONTEXT_VARIABLE_ONLY);
		
		this.ruleComposer.addOrClause();
		comparisonOperatorChoosed = false;
	}

	@Override
	public void onNotClick(View v) {
		this.ruleComposer.addNotClause();
		comparisonOperatorChoosed = false;
	}

	@Override
	public void onOpenBracketClick(View v) {
		this.ruleComposer.addOpenBracket();
		comparisonOperatorChoosed = false;
	}

	@Override
	public void onCloseBracketClick(View v) {
		// case CV has been chosen, but and operator and value are not, then write it down
		if (this.contextVariable != null)
			comparableClicked(CONTEXT_VARIABLE_ONLY);
		
		this.ruleComposer.addCloseBracket();
		comparisonOperatorChoosed = false;
	}

	@Override
	public void onEqualClick(View v) {
		
		this.operator = Operator.Equal; 
		comparisonOperatorCliked();
	}

	@Override
	public void onNotEqualClick(View v) {
		
		this.operator = Operator.Different;
		comparisonOperatorCliked();
	}

	@Override
	public void onLessThanClick(View v) {
		
		this.operator = Operator.LessThan;
		comparisonOperatorCliked();
	}

	@Override
	public void onGreaterThanClick(View v) {
		
		this.operator = Operator.GreaterThan;
		comparisonOperatorCliked();
	}

	@Override
	public void onLessThanOrEqualClick(View v) {
		
		this.operator = Operator.LessThanOrEqual;
		comparisonOperatorCliked();
	}

	@Override
	public void onGreaterThanOrEqualClick(View v) {
		
		this.operator = Operator.GreaterThanOrEqual;
		comparisonOperatorCliked();
	}

	@Override
	public void onTimerClick(View v) {
		// case CV has been chosen, but and operator and value are not, then write it down
		if (this.contextVariable != null)
			comparableClicked(CONTEXT_VARIABLE_ONLY);
		
		// TODO Get a timer value from user
		comparisonOperatorChoosed = false;
	}
	
	@Override
	void onInsertConstantClick(View v) {
		
		InputDialog dialog = new InputDialog("Insert constant", "Constant:", activity, this);
	}
	
	@Override
	public void onInputText(String text) {
		// TODO Auto-generated method stub
		
		try {
			cte = Float.parseFloat(text);
		} catch(NumberFormatException e) {
			cte = text;
		}
		
		comparableClicked(CONSTANT_VALUE);
	}

	@Override
	void onChooseContextVariableClick(View v) {

		// Hide the dialog, in order to the user choose another context variable
		dialog.dismiss();
	}
	
	@Override
	void onFinishContextVariableClick(View v) {
		// case CV has been chosen, but and operator and value are not, then write it down
		if (this.contextVariable != null)
			comparableClicked(CONTEXT_VARIABLE_ONLY);
		
//		try {
//			this.ruleComposer.finish(activity);
//		} catch (Exception e) {
//			throw new SmartAndroidRuntimeException("Error by finishing a context rule expression!", e);
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMddHHmmss");
		Date dt = new Date();
		String myDate = sdf.format(dt);
		
		this.ruleComposer.setActuatorName("Act_" + myDate);
		dialog.dismiss();
		((SmartMapActivity)activity).setMode(SmartMapActivity.ACTUATOR_MODE);
	}
	
	public void setContextVariable(String rans, String contextVariable, String cvName) {
		this.contextVariable = new ContextVariableBundle(rans, contextVariable, cvName);
		
		comparableClicked(CONTEXT_VARIABLE);
	}
	
	private void comparableClicked(int type) {
		
		if (comparisonOperatorChoosed) {

			// Finishes this condition
			if (type == CONTEXT_VARIABLE) {
				// TODO something like: this.ruleComposer.addConditionComp(contextVariable, operator, val, timeout)
			} else if (type == CONSTANT_VALUE) {
				try {
					// TODO Get correct value
					this.ruleComposer.addConditionComp(contextVariable, operator, cte, 0);
					contextVariable = null;
					operator = null;
					cte = null;
					comparisonOperatorChoosed = false;
				} catch (Exception e) {
					throw new SmartAndroidRuntimeException("Error by finilizing current condition", e);
				}
			} 
			
			showMessage("You can either choose a logical operator to add another condition or finish this Rule Expression");
			
		} else if (type == CONTEXT_VARIABLE_ONLY) {
			try {
				// TODO Get correct value
				this.ruleComposer.addCondition(contextVariable, 0);
				contextVariable = null;
				operator = null;
				cte = null;
				comparisonOperatorChoosed = false;
			} catch (Exception e) {
				throw new SmartAndroidRuntimeException("Error by finilizing current condition", e);
			}
		} else {
			showMessage("Please, choose an comparison operator to continue...");
		}
	}
	
	private void comparisonOperatorCliked() {
		
		comparisonOperatorChoosed  = true;
		showMessage("Now, choose a constant or another context variable...");
	}
	
	private void showMessage(final String message) {
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			}
		});
	}
}
