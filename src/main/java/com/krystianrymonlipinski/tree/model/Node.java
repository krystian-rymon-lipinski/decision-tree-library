package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;
import java.util.List;

public class Node<T, U> {

	private int index;
	private T state;
	private U condition;
	private Node<T, U> ancestor;
	private List<Node<T, U>> children = new ArrayList<>();
	private int level;

	public Node(Node<T, U> ancestor, U condition) {
		this.index = Tree.currentIndex++;
		this.ancestor = ancestor;
		this.condition = condition;	

		if (ancestor == null) this.level = 0;
		else 				  this.level = ancestor.level+1 ;
	}

	public Node(Node.Type type) {
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
		this.children.add(child);
	}
	
	enum Type {
		ROOT_NODE;
	}
}
