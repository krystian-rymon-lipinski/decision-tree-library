package com.krystianrymonlipinski.tree.model;

import java.util.ArrayList;

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
		root = new Node<T, U>(Node.Type.ROOT_NODE);
		Tree<T, U> tree = new Tree<>(root);
		testObj = Mockito.spy(tree);
	}
	
	public abstract U createFirstCondition();
	public abstract U createSecondCondition();
	public abstract U createThirdCondition();

	public void createTree() {
		assertEquals(1, testObj.getNodes().size());
		assertEquals(root, testObj.getRoot());
		assertEquals(root, testObj.getCurrentNode());
		assertEquals(1, Tree.currentIndex);
	}

	@Test
	public void addNode() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
		assertEquals(2, testObj.getNodes().size());
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
	public void removeNode_nonRoot_withoutChildren() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.removeNode(newNode);
		assertEquals(1, testObj.getNodes().size());
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}

	@Test
	public void removeNode_nonRoot_withChildren() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.addNode(newNode, createSecondCondition());
		testObj.addNode(newNode, createThirdCondition());
		testObj.removeNode(newNode);

		assertEquals(1, testObj.getNodes().size());
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}
	
	@Test
	public void removeNode_root_withoutChildren() {
		testObj.removeNode(testObj.getRoot());
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
		assertEquals(0, testObj.getNodes().size());
	}

	@Test
	public void removeNode_root_withChildren() {
		testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		testObj.removeNode(testObj.getRoot());
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
		assertEquals(0, testObj.getNodes().size());
	}

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

	@Test (expected = NoAncestorForRootNodeException.class)
	public void moveDownAndSetNewNodeAsRoot() throws NoAncestorForRootNodeException {
		Node<T, U> node1A = testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		Node<T, U> node1B = testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		Node<T, U> node1C = testObj.addNode(testObj.getCurrentNode(), createThirdCondition());
		Node<T, U> node2A = testObj.addNode(node1A, createFirstCondition());
		Node<T, U> node2B = testObj.addNode(node1A, createSecondCondition());
		Node<T, U> node2C = testObj.addNode(node1B, createFirstCondition());
		Node<T, U> node2D = testObj.addNode(node1B, createSecondCondition());
		Node<T, U> node2E = testObj.addNode(node1C, createFirstCondition());
		Node<T, U> node2F = testObj.addNode(node1C, createSecondCondition());

		testObj.moveDownAndSetChildAsNewRoot(node1B.getCondition());

		assertEquals(3, testObj.getNodes().size());
		assertEquals(node1B, testObj.getRoot());
		assertEquals(node1B, testObj.getCurrentNode());
		assertEquals(0, testObj.getCurrentNode().getLevel());
		assertNull(testObj.getCurrentNode().getAncestor());
		assertNull(testObj.getCurrentNode().getCondition());
		assertEquals(1, node2C.getLevel());
		assertEquals(1, node2D.getLevel());

		assertFalse(testObj.nodes.contains(node1A));
		assertFalse(testObj.nodes.contains(node1C));
		assertFalse(testObj.nodes.contains(node2A));
		assertFalse(testObj.nodes.contains(node2B));
		assertFalse(testObj.nodes.contains(node2E));
		assertFalse(testObj.nodes.contains(node2F));

		testObj.moveUp();
	}
	
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
