package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;
import java.util.List;

public class Tree<T, U> {

	static int currentIndex = 0;
	List<Node<T, U>> nodes;
	Node<T, U> root;
	Node<T, U> currentNode;

	public Tree(Node<T, U> root) {
		this.nodes = new ArrayList<>();
		this.nodes.add(root);
		this.root = root;
		this.currentNode = root;	
	}

	public Node<T, U> addNode(Node<T, U> ancestor, U condition) {
		Node<T, U> newNode = new Node<T, U>(ancestor, condition);
		nodes.add(newNode);
		ancestor.addChild(newNode);
		return newNode;
	}
}
