package br.uff.tempo.middleware.management.utils.datastructure;

import java.util.Enumeration;
import java.util.NoSuchElementException;

//
//   This file contains the Java code from Program 9.12 of
//   "Data Structures and Algorithms
//    with Object-Oriented Design Patterns in Java"
//   by Bruno R. Preiss.
//
//   Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
//
//   http://www.pads.uwaterloo.ca/Bruno.Preiss/books/opus5/programs/pgm09_12.txt
//
public abstract class AbstractTree extends AbstractContainer implements Tree {

	public void breadthFirstTraversal(Visitor visitor) {
		bft(visitor, true);
	}

	public void breadthFirstTraversal_NodeVisiting(Visitor visitor) {
		bft(visitor, false);
	}

	private void bft(Visitor visitor, boolean visitKey) {
		Queue queue = new QueueAsLinkedList();
		if (!isEmpty())
			queue.enqueue(this);
		while (!queue.isEmpty() && !visitor.isDone()) {
			Tree head = (Tree) queue.dequeue();
			try {
				if (visitKey)
					visitor.visit(head.getKey());
				else
					visitor.visit(head);
				for (int i = 0; i < head.getDegree(); ++i) {
					Tree child = head.getSubtree(i);
					if (!child.isEmpty())
						queue.enqueue(child);
				}
			} catch (InvalidOperationException e) {
				e.printStackTrace();
			}
		}
	}

	public void depthFirstTraversal(PrePostVisitor visitor) {
		dft(visitor, true);
	}

	public void depthFirstTraversal_NodeVisiting(PrePostVisitor visitor) {
		dft(visitor, false);
	}

	public void dft(PrePostVisitor visitor, boolean visitKey) {
		if (visitor.isDone())
			return;
		if (!isEmpty()) {
			try {
				if (visitKey)
					visitor.preVisit(getKey());
				else
					visitor.preVisit(this);
				for (int i = 0; i < getDegree(); ++i)
					if (visitKey)
						getSubtree(i).depthFirstTraversal(visitor);
					else
						getSubtree(i).depthFirstTraversal_NodeVisiting(visitor);
				if (visitKey)
					visitor.postVisit(getKey());
				else
					visitor.postVisit(this);
			} catch (InvalidOperationException e) {
				e.printStackTrace();
			}
		}
	}

	public void accept(Visitor visitor) {
		depthFirstTraversal(new PreOrder(visitor));
	}

	public Enumeration getEnumeration() {
		return new TreeEnumeration();
	}

	protected class TreeEnumeration implements Enumeration {
		protected Stack stack;

		public TreeEnumeration() {
			stack = new StackAsLinkedList();
			if (!isEmpty())
				stack.push(AbstractTree.this);
		}

		public boolean hasMoreElements() {
			return !stack.isEmpty();
		}

		public Object nextElement() {
			if (stack.isEmpty())
				throw new NoSuchElementException();

			Tree top = (Tree) stack.pop();
			try {
				for (int i = top.getDegree() - 1; i >= 0; --i) {
					Tree subtree = (Tree) top.getSubtree(i);
					if (!subtree.isEmpty())
						stack.push(subtree);
				}
				return top.getKey();
			} catch (InvalidOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
}
