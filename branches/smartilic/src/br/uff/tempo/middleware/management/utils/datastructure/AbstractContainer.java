package br.uff.tempo.middleware.management.utils.datastructure;

//
//   This file contains the Java code from Program 5.9 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm05_09.txt
//
public abstract class AbstractContainer extends AbstractObject implements Container {
	protected int count;

	public int getCount() {
		return count;
	}

	public boolean isEmpty() {
		return getCount() == 0;
	}

	public boolean isFull() {
		return false;
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		Visitor visitor = new AbstractVisitor() {
			private boolean comma;

			public void visit(Object object) {
				if (comma)
					buffer.append(", ");
				buffer.append(object);
				comma = true;
			}
		};
		accept(visitor);
		return getClass().getName() + " {" + buffer + "}";
	}

	public int hashCode() {
		Visitor visitor = new AbstractVisitor() {
			private int value;

			public void visit(Object object) {
				value += object.hashCode();
			}

			public int hashCode() {
				return value;
			}
		};
		accept(visitor);
		return getClass().hashCode() + visitor.hashCode();
	}
}