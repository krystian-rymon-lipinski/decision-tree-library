package com.krystianrymonlipinski.tree.model;

import java.util.*;

import com.krystianrymonlipinski.exceptions.*;

public class Tree<T, U> {

	protected static int currentIndex = 0;
	protected Node<T, U> root;
	protected Node<T, U> currentNode;
	
	public Tree(Node<T, U> root) {
		this.root = root;
		this.currentNode = root;	
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
		Node<T, U> newNode = new Node<>(ancestor, condition);
		ancestor.addChild(newNode);
		return newNode;
	}

	public void moveDown(U condition) throws NodeWithNoChildrenException, NodeConditionNotFoundException {
		if (currentNode.getChildren() == null || currentNode.getChildren().isEmpty()) {
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

	public void removeNodeAndItsChildren(Node<T, U> node, boolean evenIfRoot) {
		removeNodeChildren(node, evenIfRoot);
		removeNodeFromItsParentChildren(node);
	}

	public void removeNodeChildren(Node<T, U> node, boolean evenIfRoot) {
		if (node.getChildren() != null) {
			ListIterator<Node<T, U>> nodesIterator = node.getChildren().listIterator();
			while (nodesIterator.hasNext()) {
				removeNodeChildren(nodesIterator.next(), evenIfRoot);
				nodesIterator.remove();
			}
			if (!node.equals(root) || evenIfRoot) doRemoval(node);
		}
	}

	public void removeNodeFromItsParentChildren(Node<T, U> node) {
		if (node.getAncestor() != null) {
			ListIterator<Node<T, U>> nodesIterator = node.getAncestor().getChildren().listIterator();
			while (nodesIterator.hasNext()) {
				if (nodesIterator.next().equals(node)) {
					nodesIterator.remove();
					break;
				}
			}
		}
	}

	private void doRemoval(Node<T, U> node) {
		if (node.equals(root)) {
			root = null;
		}
		if (node.equals(currentNode)) {
			currentNode = null;
		}
		node.setCondition(null);
		node.setState(null);
	}
/*
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
*/
	public void setCurrentNodeAsRoot() {
		Node<T, U> startingPoint = currentNode;

		while (!currentNode.equals(root)) {
			Node<T, U> childToSpare = null;
			try {
				childToSpare = moveUp();
			} catch (NoAncestorForRootNodeException ex) {
				ex.printStackTrace();
			}

			ArrayList<Node<T,U>> childrenToRemove = new ArrayList<>();
			for (Node<T, U> child : currentNode.getChildren()) {
				if (!child.equals(childToSpare)) childrenToRemove.add(child);
			}
			for (Node<T, U> child : childrenToRemove) {
				removeNodeAndItsChildren(child, false);
			}
		}

		while (!currentNode.equals(startingPoint)) {
			try {
				moveDown(currentNode.getChildren().get(0).getCondition());
				doRemoval(currentNode.getAncestor());
				currentNode.setAncestor(null);
			} catch (NodeWithNoChildrenException | NodeConditionNotFoundException e) {
				e.printStackTrace();
			}
		}
		root = currentNode;
	}

	public void returnToRoot() {
		while (!currentNode.equals(root)) {
			try {
				moveUp();
			} catch (NoAncestorForRootNodeException ex) {
				ex.printStackTrace();
			}
		}
	}
}
