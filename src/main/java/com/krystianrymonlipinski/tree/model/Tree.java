package com.krystianrymonlipinski.tree.model;

import java.util.*;

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

	public void removeNodeAndItsChildren(Node<T, U> node) {
		removeNodeChildren(node);
		removeNodeFromItsParentChildren(node);
	}

	public void removeNodeChildren(Node<T, U> node) {
		ListIterator<Node<T, U>> nodesIterator = node.getChildren().listIterator();
		while (nodesIterator.hasNext()) {
			removeNodeChildren(nodesIterator.next());
			nodesIterator.remove();
		}
		doRemoval(node);
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
		System.out.println("Do removal of node: " + node);
		if (node.equals(root)) {
			root = null;
			currentNode = null;
		}
		else if (node.equals(currentNode)) {
			currentNode = node.getAncestor();
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
	
	public void moveDownAndSetChildAsNewRoot(U condition) {
		try {
			moveDown(condition);
			for (Node<T, U> child : currentNode.getAncestor().getChildren()) {
				if (!child.getCondition().equals(condition)) removeNodeAndItsChildren(child);
			}
			nodes.remove(currentNode.getAncestor());
			currentNode.setAncestor(null);
			currentNode.setCondition(null);
			root = currentNode;

			int newRootCurrentLevel = currentNode.level;
			for (Node<T, U> everyNode : nodes) {
				everyNode.level -= newRootCurrentLevel;
			}
		} catch (NodeWithNoChildrenException | NodeConditionNotFoundException e) {
			e.printStackTrace();
		}
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
