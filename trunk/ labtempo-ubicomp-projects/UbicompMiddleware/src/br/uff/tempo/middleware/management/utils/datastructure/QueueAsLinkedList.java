package br.uff.tempo.middleware.management.utils.datastructure;

import java.util.Enumeration;

//
//   This file contains the Java code from Program 6.19 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm06_19.txt
//
public class QueueAsLinkedList extends AbstractContainer implements Queue {
	protected LinkedList list;

	public QueueAsLinkedList() {
		list = new LinkedList();
	}

	public void purge() {
		list.purge();
		count = 0;
	}

	public Object getHead() {
		if (count == 0)
			throw new ContainerEmptyException();
		return list.getFirst();
	}

	public void enqueue(Object object) {
		list.append(object);
		++count;
	}

	public Object dequeue() {
		if (count == 0)
			throw new ContainerEmptyException();
		Object result = list.getFirst();
		list.extract(result);
		--count;
		return result;
	}

	@Override
	public void accept(Visitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Enumeration getEnumeration() {
		// TODO Auto-generated method stub
		return null;
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