package br.uff.tempo.testrpcserver;

public class SimpleCalculatorImpl implements Calculator{

	public double add(double x, double y) {
		return x + y;
	}

	public double multiply(double x, double y) {
		return x*y;
	}

}
