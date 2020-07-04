package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;
import java.util.List;
import com.krystianrymonlipinski.exceptions.*;

public class Tree<T, U> {

	protected static int currentIndex = 0;
	protected List<Node<T, U>> nodes;
	protected Node<T, U> root;
	protected Node<T, U> currentNode;
	
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

	public void addNode(Node<T, U> node) {
		nodes.add(node);
	}

	public void moveDown(U condition) throws NodeWithNoChildrenException, NodeConditionNotFoundException {
		if (currentNode.getChildren().size() == 0) {
			throw new NodeWithNoChildrenException("Chosen node has no children to move down");
		}
		else {
			for(Node<T, U> node : currentNode.getChildren()) {
				if(node.getCondition().equals(condition)) {
					currentNode = node;
					return;
				}
			}
		}

		throw new NodeConditionNotFoundException("No node with such condition");
	}

	public Node<T, U> moveUp() throws NoAncestorForRootNodeException {
		Node<T, U> previousNode = currentNode;
		if(currentNode.getLevel() != 0) {
			currentNode = currentNode.getAncestor();
			return previousNode;
		}
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
			if (node.equals(currentNode)) currentNode = node.getAncestor();
		}
		nodes.remove(node);
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
	
	public void setChildAsNewRoot(Node<T, U> node) {
		if (node.getAncestor() != null) {
			for (Node<T, U> child : node.getAncestor().getChildren()) {
				if (!child.equals(node)) removeNode(child);
			}
			nodes.remove(node.getAncestor());
			node.setAncestor(null);
			node.setCondition(null);
			root = node;
			currentNode = node;

			int newRootCurrentLevel = node.level;
			for (Node<T, U> everyNode : nodes) {
				everyNode.level -= newRootCurrentLevel;
			}
		}
	}
}
