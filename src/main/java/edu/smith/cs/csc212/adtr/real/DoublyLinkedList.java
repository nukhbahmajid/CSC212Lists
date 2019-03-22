package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.TODOErr;





public class DoublyLinkedList<T> extends ListADT<T> {
	private Node<T> start;
	private Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		Node<T> originalStart = this.start;
		this.start = this.start.after;
		return originalStart.value;
		
 	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		if (this.size() == 1) {
			Node<T> originalEnd = this.start;
			this.end = this.start = null;
			return originalEnd.value;
		} else {
			T removedValue = this.end.value;
			this.end = this.end.before;
			this.end.after = null;
			return removedValue;
		}
		
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		checkInclusiveIndex(index);
		int at = 0;
		Node<T> pointer = this.start; 
		Node<T> prevNode = null;
		Node<T> targetNode = null;
		Node<T> nextNode = null;
		
		if (index < 0 || index > this.size() -1) {
			throw new BadIndexError(index);
		}
		
		if (index == 0 || this.size() == 1) {
			return removeFront();

		} 
		
		if(index == this.size() - 1) {
			return removeBack();
		} 
		for (Node<T> current = this.start; current != null; current = current.after) {
			if (at == index - 1) {
				prevNode = pointer;
			}
			
			if (at == index) {
				targetNode = pointer;
			}
			
			if (at == index + 1) {
				nextNode = pointer;
			}
			
			pointer = pointer.after;
			at++;
		}
		
		prevNode.after = nextNode;
		nextNode.before = prevNode;
		return targetNode.value;
	}

	@Override
	public void addFront(T item) {
		Node<T> newStart = new Node<T> (item);
		
		if (this.size() == 0) {
			this.start = newStart;
			this.end = newStart;
			return;
		}
		
		this.start.before = newStart;
		newStart.after = this.start;
		this.start = newStart;
	
	}

	@Override
	public void addBack(T item) {
		if (end == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondLast = end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		checkNotEmpty();
		
		if (index > this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		
		int counter = 0;
		if (index == 0) {
			addFront(item);
			return;
		}
		
		if (index == this.size()) {
			addBack(item);
			return;
		}
		// the node before the index desired
		Node<T> prevNode = this.start; 
		// the node after the index desired
		Node<T> nextNode = this.start;
		// the current node
		Node<T> pointer = this.start;
		
		for(Node<T> node = this.start; node != null; node = node.after) {
			if(counter == index - 1) {
				prevNode = pointer;
			}
			
			if(counter == index) {
				nextNode = pointer;
			}
			
			pointer = pointer.after;
			counter++;
		}
		Node<T> desiredNode = new Node<T>(item);
		prevNode.after = desiredNode;
		nextNode.before = desiredNode;
		desiredNode.before = prevNode;
		desiredNode.after = nextNode;
		
		
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty(); 
		return this.getIndex(this.size() - 1); 
	}
	
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> current = this.start; current != null; current = current.after) {
			if (at++ == index) {
				return current.value;
			}
		}
		throw new BadIndexError(index);
		
	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		if (index >= this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		
		if(index == 0) {
			this.start.value = value;
		}
		
		if(index == this.size() -1) {
			this.end.value = value;
		}
		int counter = 0;
		Node<T> pointer = this.start;
		Node<T> targetNode = this.start;
		
		for (Node<T> current = this.start; current != null; current = current.after) {
			if(counter == index) {
				targetNode = pointer;
			}
			pointer = pointer.after;
			counter++;
		}
		targetNode.value = value;
	}

	@Override
	public int size() {
		int at = 0;
		
		for (Node<T> current = this.start; current != null; current = current.after) {
			at++;
		}
		return at;
	}

	@Override
	public boolean isEmpty() {
		if (this.start == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
