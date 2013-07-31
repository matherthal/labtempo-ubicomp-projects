package br.uff.tempo.middleware.management;

public interface IRuleComposeListener {

	void onRuleCompositionChanged(String op);
	void onRuleCompositionFinished(Formula formula);
}
