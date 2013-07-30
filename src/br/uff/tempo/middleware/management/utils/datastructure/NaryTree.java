package br.uff.tempo.middleware.management.utils.datastructure;

//
//   This file contains the Java code from Program 9.19 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm09_19.txt
//
public class NaryTree extends AbstractTree {
	protected Object key;
	protected int degree;
	protected NaryTree[] subtree;

	public NaryTree(int degree) {
		key = null;
		this.degree = degree;
		subtree = null;
	}

	public NaryTree(int degree, Object key) {
		this.key = key;
		this.degree = degree;
		subtree = new NaryTree[degree];
		for (int i = 0; i < degree; ++i)
			subtree[i] = new NaryTree(degree);
	}

	public Tree getSubtree(int i) throws InvalidOperationException {
		if (isEmpty())
			throw new InvalidOperationException();
		return subtree[i];
	}

	public boolean isEmpty() {
		return key == null;
	}

	public Object getKey() throws InvalidOperationException {
		if (isEmpty())
			throw new InvalidOperationException();
		return key;
	}

	public void attachKey(Object object) throws InvalidOperationException {
		if (!isEmpty())
			throw new InvalidOperationException();
		key = object;
		subtree = new NaryTree[degree];
		for (int i = 0; i < degree; ++i)
			subtree[i] = new NaryTree(degree);
	}

	public void attachSubtree(int i, NaryTree t) throws InvalidOperationException {
		if (isEmpty() || !subtree[i].isEmpty())
			throw new InvalidOperationException();
		subtree[i] = t;
	}

	public Object detachKey() throws InvalidOperationException {
		if (!isLeaf())
			throw new InvalidOperationException();
		Object result = key;
		key = null;
		subtree = null;
		return result;
	}

	NaryTree detachSubtree(int i) throws InvalidOperationException {
		if (isEmpty())
			throw new InvalidOperationException();
		NaryTree result = subtree[i];
		subtree[i] = new NaryTree(degree);
		return result;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDegree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void purge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int compareTo(Comparable arg) {
		// TODO Auto-generated method stub
		return 0;
	}

}
