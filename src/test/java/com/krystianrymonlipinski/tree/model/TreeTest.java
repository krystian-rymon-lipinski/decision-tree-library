package com.krystianrymonlipinski.tree.model;

import com.krystianrymonlipinski.exceptions.NodeConditionNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.krystianrymonlipinski.exceptions.NoAncestorForRootNodeException;
import com.krystianrymonlipinski.exceptions.NodeWithNoChildrenException;

import static org.junit.Assert.*;

public abstract class TreeTest<T, U> {

	Node<T, U> root;
	Tree<T, U> testObj;
	
	@Before
	public void setUp() {
		Tree.currentIndex = 0;
		root = new Node<>();
		Tree<T, U> tree = new Tree<>(root);
		testObj = Mockito.spy(tree);
	}
	
	public abstract U createFirstCondition();
	public abstract U createSecondCondition();
	public abstract U createThirdCondition();

	@Test
	public void createTree() {
		assertEquals(root, testObj.getRoot());
		assertEquals(root, testObj.getCurrentNode());
		assertEquals(1, Tree.currentIndex);
	}

	@Test
	public void addNode() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
		assertEquals(1, testObj.getRoot().getChildren().size());
		assertEquals(newNode, testObj.getRoot().getChildren().get(0));
		assertEquals(2, Tree.currentIndex);
	}

	@Test
	public void moveDown_fromNodeWithChildren() throws Exception {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.moveDown(createFirstCondition());
		assertEquals(newNode, testObj.getCurrentNode());
		assertEquals(1, testObj.getCurrentNode().getLevel());
	}

	@Test(expected = NodeWithNoChildrenException.class)
	public void moveDown_fromNodeWithNoChildren() throws NodeWithNoChildrenException, NodeConditionNotFoundException {
		testObj.moveDown(createFirstCondition());
	}

	@Test(expected = NodeConditionNotFoundException.class)
	public void moveDown_wrongConditionProvided() throws NodeWithNoChildrenException, NodeConditionNotFoundException {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.moveDown(createSecondCondition());
	}

	@Test(expected = NoAncestorForRootNodeException.class)
	public void moveUp_fromRoot() throws NoAncestorForRootNodeException {
		testObj.moveUp();
	}

	@Test
	public void moveUp_fromNonRoot() throws Exception {
		Node<T, U> child = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.moveDown(createFirstCondition());
		Node<T, U> previousNode = testObj.moveUp();

		assertEquals(child.getIndex(), previousNode.getIndex());
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}

	@Test
	public void removeNodeFromItsParentChildren() {
		testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.addNode(testObj.getRoot(), createSecondCondition());
		Node<T, U> nodeToRemove = testObj.addNode(testObj.getRoot(), createThirdCondition());

		assertEquals(3, testObj.getRoot().getChildren().size());

		testObj.removeNodeFromItsParentChildren(nodeToRemove);

		assertEquals(2, testObj.getRoot().getChildren().size());
	}

	@Test 
	public void removeNodeWithItsChildren_nonRoot_childrenNonExisting() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.removeNodeAndItsChildren(newNode, false);
		assertEquals(0, testObj.getCurrentNode().getLevel());
		assertTrue(testObj.getCurrentNode().getChildren().isEmpty());
	}

	@Test
	public void removeNodeWithItsChildren_nonRoot_childrenExisting() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.addNode(newNode, createSecondCondition());
		testObj.addNode(newNode, createThirdCondition());
		testObj.removeNodeAndItsChildren(newNode, false);

		assertEquals(0, testObj.getCurrentNode().getLevel());
		assertTrue(testObj.getCurrentNode().getChildren().isEmpty());
	}
	
	@Test
	public void removeNodeWithItsChildren_root_childrenNonExisting() {
		testObj.removeNodeAndItsChildren(testObj.getRoot(), true);
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
	}

	@Test
	public void removeNodeWithItsChildren_root_childrenExisting() {
		testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		testObj.removeNodeAndItsChildren(testObj.getRoot(), true);
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
	}

	@Test
	public void setCurrentNodeAsRoot() throws NodeWithNoChildrenException, NodeConditionNotFoundException {
		Node<T, U> node1A = testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		Node<T, U> node1B = testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		Node<T, U> node2A = testObj.addNode(node1A, createFirstCondition());
		Node<T, U> node2B = testObj.addNode(node1A, createSecondCondition());
		Node<T, U> node2C = testObj.addNode(node1B, createFirstCondition());
		Node<T, U> node3A = testObj.addNode(node2A, createFirstCondition());
		Node<T, U> node3B = testObj.addNode(node2B, createFirstCondition());
		Node<T, U> node3C = testObj.addNode(node2B, createSecondCondition());
		Node<T, U> node3D = testObj.addNode(node2C, createFirstCondition());
		Node<T, U> node3E = testObj.addNode(node2C, createSecondCondition());
		Node<T, U> node3F = testObj.addNode(node2C, createThirdCondition());

		testObj.moveDown(node1A.getCondition());
		testObj.moveDown(node2B.getCondition());

		assertEquals(node2B, testObj.getCurrentNode());

		testObj.setCurrentNodeAsRoot();

		assertEquals(node2B, testObj.getRoot());
		assertEquals(node2B, testObj.getCurrentNode());
		assertEquals(2, node2B.getLevel());
		assertEquals(2, testObj.getRoot().getChildren().size());

		assertNull(node2A);

	}
/*
	@Test
	public void organizeNodesInBranches() {
		Node<T, U> node1A = testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		Node<T, U> node1B = testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		Node<T, U> node1C = testObj.addNode(testObj.getCurrentNode(), createThirdCondition());
		Node<T, U> node2A = testObj.addNode(node1A, createFirstCondition());
		Node<T, U> node2B = testObj.addNode(node1A, createSecondCondition());
		Node<T, U> node2C = testObj.addNode(node1B, createFirstCondition());
		Node<T, U> node2D = testObj.addNode(node1C, createFirstCondition());
		Node<T, U> node2E = testObj.addNode(node1C, createSecondCondition());
		Node<T, U> node2F = testObj.addNode(node1C, createThirdCondition());
		Node<T, U> node3A = testObj.addNode(node2B, createFirstCondition());

		ArrayList<ArrayList<Node<T, U>>> branches = testObj.organizeNodesInBranches();
		assertEquals(6, branches.size());

	}
*/
	@Test
	public void moveToNode() {
		Node<T, U> node1A = testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		Node<T, U> node2A = testObj.addNode(node1A, createFirstCondition());
		Node<T, U> node3A = testObj.addNode(node2A, createFirstCondition());

		testObj.moveToNode(node3A);
		assertEquals(node3A, testObj.getCurrentNode());
	}

	@Test
	public void returnToRoot() throws Exception {
		Node<T, U> node1A = testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		Node<T, U> node2A = testObj.addNode(node1A, createFirstCondition());
		Node<T, U> node3A = testObj.addNode(node2A, createFirstCondition());

		testObj.moveDown(createFirstCondition());
		testObj.moveDown(createFirstCondition());
		testObj.moveDown(createFirstCondition());

		assertEquals(node3A, testObj.getCurrentNode());

		testObj.returnToRoot();

		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
	}
	
	
	
}
