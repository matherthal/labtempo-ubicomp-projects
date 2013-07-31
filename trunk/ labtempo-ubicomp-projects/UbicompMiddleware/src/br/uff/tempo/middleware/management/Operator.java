package br.uff.tempo.middleware.management;

public enum Operator {
	Equal("="), Different("!="), GreaterThan(">"), LessThan("<"), GreaterThanOrEqual(">="), LessThanOrEqual("<=");
	
	private String symbol;
	
	Operator(String symbol) {
		this.symbol = symbol;
	}
	
	String getSymbol() {
		return this.symbol;
	}
};