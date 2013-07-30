package br.uff.tempo.middleware.management.utils.datastructure;

//
//   This file contains the Java code from Program 9.1 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm09_01.txt
//
public interface Tree extends Container {
	Object getKey() throws InvalidOperationException;

	Tree getSubtree(int i) throws InvalidOperationException;

	boolean isEmpty();

	boolean isLeaf();

	int getDegree();

	int getHeight();

	void depthFirstTraversal(PrePostVisitor visitor);

	void depthFirstTraversal_NodeVisiting(PrePostVisitor visitor);

	void breadthFirstTraversal(Visitor visitor);

	void breadthFirstTraversal_NodeVisiting(Visitor visitor);
}
