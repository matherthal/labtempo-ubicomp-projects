package br.uff.tempo.middleware.management.utils.datastructure;

//
//   This file contains the Java code from Program 4.13 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm04_13.txt
//
public class LinkedList {
	protected Element head;
	protected Element tail;

	public LinkedList() {
	}

	public Element getHead() {
		return head;
	}

	public Element getTail() {
		return tail;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public Object getFirst() {
		if (head == null)
			throw new ContainerEmptyException();
		return head.datum;
	}

	public Object getLast() {
		if (tail == null)
			throw new ContainerEmptyException();
		return tail.datum;
	}

	public void prepend(Object item) {
		Element tmp = new Element(item, head);
		if (head == null)
			tail = tmp;
		head = tmp;
	}

	public void append(Object item) {
		Element tmp = new Element(item, null);
		if (head == null)
			head = tmp;
		else
			tail.next = tmp;
		tail = tmp;
	}

	public void assign(LinkedList list) {
		if (list != this) {
			purge();
			for (Element ptr = list.head; ptr != null; ptr = ptr.next) {
				append(ptr.datum);
			}
		}
	}

	public void extract(Object item) {
		Element ptr = head;
		Element prevPtr = null;
		while (ptr != null && ptr.datum != item) {
			prevPtr = ptr;
			ptr = ptr.next;
		}
		if (ptr == null)
			throw new IllegalArgumentException("item not found");
		if (ptr == head)
			head = ptr.next;
		else
			prevPtr.next = ptr.next;
		if (ptr == tail)
			tail = prevPtr;
	}

	public void purge() {
		head = null;
		tail = null;
	}

	public final class Element {
		Object datum;
		Element next;

		Element(Object datum, Element next) {
			this.datum = datum;
			this.next = next;
		}

		public Object getDatum() {
			return datum;
		}

		public Element getNext() {
			return next;
		}

		public void insertAfter(Object item) {
			next = new Element(item, next);
			if (tail == this)
				tail = next;
		}

		public void insertBefore(Object item) {
			Element tmp = new Element(item, this);
			if (this == head)
				head = tmp;
			else {
				Element prevPtr = head;
				while (prevPtr != null && prevPtr.next != this)
					prevPtr = prevPtr.next;
				prevPtr.next = tmp;
			}
		}
	}
}