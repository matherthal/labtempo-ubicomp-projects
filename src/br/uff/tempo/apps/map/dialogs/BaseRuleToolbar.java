package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import br.uff.tempo.R;

public abstract class BaseRuleToolbar extends MapDialog {
	
	public BaseRuleToolbar(final Activity act) {
		super(act, R.layout.rule_toolbar, R.string.title_rule_composer);
		registerToUiEvents();
	}
	
	private final void registerToUiEvents() {
		// Register to UI events
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Button and = (Button) dialog.findViewById(R.id.rule_btn_and);
				Button or = (Button) dialog.findViewById(R.id.rule_btn_or);
				Button not = (Button) dialog.findViewById(R.id.rule_btn_not);
				Button openBracket = (Button) dialog.findViewById(R.id.rule_btn_open_bracket);
				Button closeBracket = (Button) dialog.findViewById(R.id.rule_btn_close_bracket);
				Button equal = (Button) dialog.findViewById(R.id.rule_btn_equal);
				Button notEqual = (Button) dialog.findViewById(R.id.rule_btn_not_equal);
				Button lessThan = (Button) dialog.findViewById(R.id.rule_btn_less_than);
				Button greaterThan = (Button) dialog.findViewById(R.id.rule_btn_greater_than);
				Button lessThanOrEqual = (Button) dialog.findViewById(R.id.rule_btn_less_than_or_equal);
				Button greaterThanOrEqual = (Button) dialog.findViewById(R.id.rule_btn_greater_than_or_equal);
				Button timer = (Button) dialog.findViewById(R.id.rule_btn_timer);
				Button constant = (Button) dialog.findViewById(R.id.rule_btn_constant);
				Button anotherCV = (Button) dialog.findViewById(R.id.rule_btn_context_variable);
				Button finish = (Button) dialog.findViewById(R.id.rule_btn_finish);

				and.setOnClickListener(BaseRuleToolbar.this);
				or.setOnClickListener(BaseRuleToolbar.this);
				not.setOnClickListener(BaseRuleToolbar.this);
				openBracket.setOnClickListener(BaseRuleToolbar.this);
				closeBracket.setOnClickListener(BaseRuleToolbar.this);
				equal.setOnClickListener(BaseRuleToolbar.this);
				notEqual.setOnClickListener(BaseRuleToolbar.this);
				lessThan.setOnClickListener(BaseRuleToolbar.this);
				greaterThan.setOnClickListener(BaseRuleToolbar.this);
				lessThanOrEqual.setOnClickListener(BaseRuleToolbar.this);
				greaterThanOrEqual.setOnClickListener(BaseRuleToolbar.this);
				timer.setOnClickListener(BaseRuleToolbar.this);
				constant.setOnClickListener(BaseRuleToolbar.this);
				anotherCV.setOnClickListener(BaseRuleToolbar.this);
				finish.setOnClickListener(BaseRuleToolbar.this);
			}
		});
	}
	
	//Select the appropriated callback method
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.rule_btn_and:
			
			onAndClick(v);
			
			break;
		case R.id.rule_btn_or:
			
			onOrClick(v);
			
			break;
		case R.id.rule_btn_not:

			onNotClick(v);
						
			break;
		case R.id.rule_btn_open_bracket:
			
			onOpenBracketClick(v);
			
			break;
		case R.id.rule_btn_close_bracket:

			onCloseBracketClick(v);
			
			break;
		case R.id.rule_btn_equal:

			onEqualClick(v);
			
			break;
		case R.id.rule_btn_not_equal:

			onNotEqualClick(v);
			
			break;
		case R.id.rule_btn_less_than:

			onLessThanClick(v);
			
			break;
		case R.id.rule_btn_greater_than:

			onGreaterThanClick(v);
			
			break;

		case R.id.rule_btn_less_than_or_equal:
			
			onLessThanOrEqualClick(v);
			
			break;
		case R.id.rule_btn_greater_than_or_equal:
			
			onGreaterThanOrEqualClick(v);
			
			break;
		case R.id.rule_btn_timer:

			onTimerClick(v);
			
			break;
		case R.id.rule_btn_constant:

			onInsertConstantClick(v);
			
			break;
		case R.id.rule_btn_context_variable:

			onChooseContextVariableClick(v);
			
			break;
			
		case R.id.rule_btn_finish:
			
			onFinishContextVariableClick(v);
			
			break;

		default:
			break;
		}
	}
	
	abstract void onAndClick(View v);
	abstract void onOrClick(View v);
	abstract void onNotClick(View v);
	abstract void onOpenBracketClick(View v);
	abstract void onCloseBracketClick(View v);
	abstract void onEqualClick(View v);
	abstract void onNotEqualClick(View v);
	abstract void onLessThanClick(View v);
	abstract void onGreaterThanClick(View v);
	abstract void onLessThanOrEqualClick(View v);
	abstract void onGreaterThanOrEqualClick(View v);
	abstract void onTimerClick(View v);
	abstract void onInsertConstantClick(View v);
	abstract void onChooseContextVariableClick(View v);
	abstract void onFinishContextVariableClick(View v);
	
}
