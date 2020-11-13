package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node<T, U> {

	protected int index;
	protected T state;
	protected U condition;
	protected Node<T, U> ancestor;
	protected List<Node<T, U>> children;
	protected int level;

	public Node(Node<T, U> ancestor, U condition) {
		this.index = Tree.currentIndex++;
		this.ancestor = ancestor;
		this.condition = condition;	

		if (ancestor == null) this.level = 0;
		else 				  this.level = ancestor.level+1 ;
	}

	public Node() {
		this(null, null);
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public T getState() {
		return state;
	}
	public void setState(T state) {
		this.state = state;
	}
	public U getCondition() {
		return condition;
	}
	public void setCondition(U condition) {
		this.condition = condition;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Node<T, U> getAncestor() {
		return ancestor;
	}
	public void setAncestor(Node<T, U> ancestor) {
		this.ancestor = ancestor;
	}
	public List<Node<T, U>> getChildren() {
		return children;
	}

	public void addChild(Node<T, U> child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		this.children.add(child);
	}

	public String toString() {
		return String.valueOf(index);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node<?, ?> node = (Node<?, ?>) o;
		return index == node.index;
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}
}
