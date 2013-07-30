package br.uff.tempo.middleware.management.utils.datastructure;

//
//   This file contains the Java code from Program 9.15 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm09_15.txt
//
public class GeneralTree extends AbstractTree {
	protected Object key;
	protected int degree;
	protected LinkedList list;
	protected GeneralTree father = null;

	public GeneralTree(Object key) {
		this.key = key;
		degree = 0;
		count = 1;
		list = new LinkedList();
	}

	public void purge() {
		list.purge();
		degree = 0;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Tree getSubtree(int i) {
		if (i < 0 || i >= degree)
			throw new IndexOutOfBoundsException();
		LinkedList.Element ptr = list.getHead();
		for (int j = 0; j < i; ++j)
			ptr = ptr.getNext();
		return (GeneralTree) ptr.getDatum();
	}

	public void attachSubtree(GeneralTree t) {
		t.father = this;
		list.append(t);
		++degree;
		++count;
	}

	public GeneralTree detachSubtree(GeneralTree t) {
		list.extract(t);
		--degree;
		--count;
		return t;
	}

	public boolean isRoot() {
		return father == null;
	}

	@Override
	public boolean isLeaf() {
		return list.isEmpty();
	}

	@Override
	public int getDegree() {
		return degree;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public GeneralTree getFather() {
		return father;
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
	
	@Override
	public String toString() {
		return key.toString();
	}
}
