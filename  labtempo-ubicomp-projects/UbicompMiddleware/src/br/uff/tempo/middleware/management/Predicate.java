package br.uff.tempo.middleware.management;

public class Predicate extends Formula {
	// First operand information
	private Operand op1 = null;

	// Second operand information
	private Operand op2 = null;

	// Operator
	private Operator operator = Operator.Equal;

	// constVal is the value to compare the the change of the context variable
	// The constant value can be the first or second operand
	// private Object constVal;

	protected boolean valid = false;

	/**
	 * Constructor
	 * 
	 * @param ra_op2
	 * @param cv_op2
	 * @param operator
	 * @throws Exception
	 */
	public Predicate(Operand op1, Operator operator, Operand op2, long timeout, RuleInterpreter stakeholder) throws Exception {
		super("0");
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
		this.setTimeout(timeout);
		this.setTimerStakeholder(stakeholder);
		count = 1;
		evaluate();
	}

	/**
	 * Constructor
	 * 
	 * @param ra_op2
	 * @param cv_op2
	 * @param operator
	 * @throws Exception
	 */
	public Predicate(Operand op, long timeout, RuleInterpreter stakeholder) throws Exception {
		super();
		this.op1 = op;
		this.setTimeout(timeout);
		this.setTimerStakeholder(stakeholder);
		count = 1;
		evaluate();
	}

	public boolean evaluate() {
		Object v1 = this.op1.getVal();
		// If there are only one value, then it must be a boolean one
		if (this.op2 == null) {
			this.valid = (Boolean) v1;
		} else {
			Object v2 = this.op2.getVal();

			// Parse value to attribute's type. Ex., if it is boolean, so we parse
			// the value to boolean before compare
			if (operator.equals(Operator.Equal)) // Operator ==
				this.valid = v1.equals(v2);
			else if (operator.equals(Operator.Different)) // Operator !=
				this.valid = !v1.equals(v2);
			else {
				double d_v2 = Double.parseDouble(v2.toString());
				double d_v1 = Double.parseDouble(v1.toString());
				if (operator.equals(Operator.GreaterThan))
					this.valid = d_v1 > d_v2;
				else if (operator.equals(Operator.GreaterThanOrEqual))
					this.valid = d_v1 >= d_v2;
				else if (operator.equals(Operator.LessThan))
					this.valid = d_v1 < d_v2;
				else if (operator.equals(Operator.LessThanOrEqual))
					this.valid = d_v1 <= d_v2;
				else
					this.valid = false;
			}
		}
		// Make the node's key be the value of valid, 
		// thus the visiting is able to easily get the node's value
		this.key = valid ? "1" : "0";
		this.eval = valid;
		return isValid();
	}

	@Override
	public String toString() {
		return valid ? "1" : "0";
	}

	/**
	 * @return the op1
	 */
	public Operand getOp1() {
		return op1;
	}

	/**
	 * @param op1
	 *            the op1 to set
	 */
	public void setOp1(Operand op1) {
		this.op1 = op1;
	}

	/**
	 * @return the op2
	 */
	public Operand getOp2() {
		return op2;
	}

	/**
	 * @param op2
	 *            the op2 to set
	 */
	public void setOp2(Operand op2) {
		this.op2 = op2;
	}
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
		this.key = valid ? "1" : "0";
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	@Override
	public void timerExpired(boolean b) {
		this.timerexp = b;
		this.valid = b;
	}
	
	public String asText() {
		String pred = "";
		pred = this.op1.isConstant() ? this.op1.getValue().toString() : this.op1.getRai() + ": " + this.op1.getCv();
		pred += "\n" + this.operator.name() + "\n";
		pred += this.op2.isConstant() ? this.op2.getValue().toString() : this.op2.getRai() + ": " + this.op2.getCv();
		
		return pred;
	}
}
