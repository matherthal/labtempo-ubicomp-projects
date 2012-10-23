package br.uff.tempo.middleware.management.utils.datastructure;

import java.util.Enumeration;
import java.util.NoSuchElementException;

//
//   This file contains the Java code from Program 6.7 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm06_07.txt
//
public class StackAsLinkedList extends AbstractContainer implements Stack {
	protected LinkedList list;

	public StackAsLinkedList() {
		list = new LinkedList();
	}

	public void push(Object object) {
		list.prepend(object);
		++count;
	}

	public Object pop() {
		if (count == 0)
			throw new ContainerEmptyException();
		Object result = list.getFirst();
		list.extract(result);
		--count;
		return result;
	}

	public Object getTop() {
		if (count == 0)
			throw new ContainerEmptyException();
		return list.getFirst();
	}

	public void purge() {
		list.purge();
		count = 0;
	}

	public void accept(Visitor visitor) {
		for (LinkedList.Element ptr = list.getHead(); ptr != null; ptr = ptr.getNext()) {
			visitor.visit(ptr.getDatum());
			if (visitor.isDone())
				return;
		}
	}

	public Enumeration getEnumeration() {
		return new Enumeration() {
			protected LinkedList.Element position = list.getHead();

			public boolean hasMoreElements() {
				return position != null;
			}

			public Object nextElement() {
				if (position == null)
					throw new NoSuchElementException();
				Object result = position.getDatum();
				position = position.getNext();
				return result;
			}
		};
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int compareTo(Comparable arg) {
		// TODO Auto-generated method stub
		return 0;
	}
}