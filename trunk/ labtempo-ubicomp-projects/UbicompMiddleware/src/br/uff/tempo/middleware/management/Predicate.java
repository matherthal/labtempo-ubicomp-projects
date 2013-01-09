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

//	private long timeout = 0;
	private boolean valid = false;

	/**
	 * Constructor
	 * 
	 * @param ra_op2
	 * @param cv_op2
	 * @param operator
	 * @throws Exception
	 */
	public Predicate(Operand op1, Operator operator, Operand op2, long timeout) throws Exception {
		super("0");
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
		this.setTimeout(timeout);
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
	public Predicate(Operand op, long timeout) throws Exception {
		super();
		this.op1 = op;
		this.setTimeout(timeout);
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

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
		this.key = valid;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return the valueCache
	 */
	// public Object getValueCache() {
	// return value_op1;
	// }

	/**
	 * @param valueCache
	 *            the valueCache to set
	 */
	// public void setValueCache(Object valueCache) {
	// this.value_op1 = valueCache;
	// // if (!test())
	// // timerReset();
	// }

	/**
	 * Update value stored in valueCache, result from context variable query or
	 * notification
	 */
	// public void updateValueCache() {
	// Stub s = new Stub(rai_op1);
	// setValueCache(s.makeCall(cv_op1, params_op1));
	// }

	// /**
	// * @return the value
	// */
	// public Object getConstValue() {
	// return constVal;
	// }
	//
	// /**
	// * @param value
	// * the value to set
	// */
	// public void setConstValue(Object value) {
	// this.constVal = value;
	// }

	// /**
	// * @return the rai
	// */
	// public String getRai() {
	// return rai_op1;
	// }
	//
	// /**
	// * @return the method
	// */
	// public String getCV() {
	// return cv_op1;
	// }
	//
	// /**
	// * @return the value
	// */
	// public void setCV(String cv, Object[] params) {
	// if (op == 1) {
	// this.cv_op1 = cv;
	// }
	//
	// }

	/**
	 * Constructor
	 * 
	 * @param ra
	 * @param cv
	 * @param operator
	 * @param constVal
	 *            It creates a condition
	 * @throws Exception
	 */
	// public ComparisonNode(Operand op, Operator operator, Object value, long
	// timeout) throws Exception {
	// super(0);
	// this.rai_op1 = rai;
	// this.setCV(cv, params);
	//
	// this.rai_op2 = null;
	// this.setCV(null, null);
	// this.op1 = op;
	// this.operator = operator;
	// this.constVal = value;
	// }
}
