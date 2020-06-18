package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;
import java.util.List;
import com.krystianrymonlipinski.exceptions.*;

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

	public List<Node<T, U>> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node<T, U>> nodes) {
		this.nodes = nodes;
	}

	public Node<T, U> getRoot() {
		return root;
	}

	public void setRoot(Node<T, U> root) {
		this.root = root;
	}

	public Node<T, U> getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node<T, U> currentNode) {
		this.currentNode = currentNode;
	}

	public Node<T, U> addNode(Node<T, U> ancestor, U condition) {
		Node<T, U> newNode = new Node<T, U>(ancestor, condition);
		nodes.add(newNode);
		ancestor.addChild(newNode);
		return newNode;
	}

	public void moveDown(U condition) throws NodeWithNoChildrenException {
		for(Node<T, U> node : currentNode.getChildren()) {
			if(node.getCondition() == condition) {
				currentNode = node;
				return;
			}
		}

		throw new NodeWithNoChildrenException("Chosen node has no children to move down");
	}

	public void moveUp() throws NoAncestorForRootNodeException {
		if(currentNode.getLevel() != 0) currentNode = currentNode.getAncestor();
		else throw new NoAncestorForRootNodeException("Chosen node is root and has no ancestor");
	}
	
	public void moveToNode(Node<T, U> node) {
		currentNode = node;
	}

	public void removeNode(Node<T, U> node) {	
		if (node.getChildren().size() > 0) {
			for(Node<T, U> child : node.getChildren()) {
				removeNode(child);
			}
			doRemoval(node);
		}
		else {
			doRemoval(node);
		}
	}

	private void doRemoval(Node<T, U> node) {
		if (node.getLevel() == 0) {
			currentNode = null;
			root = null;
		}
		else {
			currentNode = node.getAncestor();
		}
		nodes.remove(node);
		currentIndex--;
	}
	
	public ArrayList<ArrayList<Node<T, U>>> organizeNodesInBranches() {
		ArrayList<ArrayList<Node<T, U>>> branches = new ArrayList<>();
		ArrayList<Node<T, U>> lowestNodes = new ArrayList<>();
		for(Node<T, U> node : nodes) {
			if(node.getChildren().size() == 0) {
				lowestNodes.add(node);
			}
		}

		for(Node<T, U> node : lowestNodes) {
			branches.add(new ArrayList<Node<T, U>>());
			while(node.getAncestor() != null) {
				branches.get(branches.size()-1).add(node);
				node = node.getAncestor();
			}
		}

		return branches;	
	}
	
	public void setNewNodeAsRoot(Node<T, U> node) {
		if (node.getAncestor() != null) {
			for (Node<T, U> child : node.getAncestor().getChildren()) {
				if (child != node) removeNode(child);
			}
			nodes.remove(node.getAncestor());
			root = node;
			currentNode = node;


		}
	}
}
