package br.uff.tempo.middleware.management;

public interface IRuleComposeListener {

	void onRuleCompositionChanged(String op, Formula formula);
	void onRuleCompositionChanged(Predicate predicate, Formula formula);
	void onRuleCompositionFinished(Formula formula);
}
