package com.krystianrymonlipinski.tree.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.krystianrymonlipinski.exceptions.NoAncestorForRootNodeException;
import com.krystianrymonlipinski.exceptions.NodeWithNoChildrenException;

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
	public void moveDown() throws Exception {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.moveDown(createFirstCondition());
		assertEquals(newNode, testObj.getCurrentNode());
		assertEquals(1, testObj.getCurrentNode().getLevel());
	}

	@Test(expected = NodeWithNoChildrenException.class)
	public void moveDown_fromRootWithNoChildren() throws NodeWithNoChildrenException {
		testObj.moveDown(createFirstCondition());
	}

	@Test(expected = NoAncestorForRootNodeException.class)
	public void moveUp_fromRoot() throws NoAncestorForRootNodeException {
		testObj.moveUp();
	}

	@Test
	public void moveUp_fromNonRoot() throws Exception {
		testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.moveDown(createFirstCondition());
		testObj.moveUp();
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}

	@Test 
	public void removeNode_nonRoot_withoutChildren() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.removeNode(newNode);
		assertEquals(1, testObj.getNodes().size());
		assertEquals(1, Tree.currentIndex);
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}

	@Test
	public void removeNode_nonRoot_withChilden() {
		Node<T, U> newNode = testObj.addNode(testObj.getRoot(), createFirstCondition());
		testObj.addNode(newNode, createSecondCondition());
		testObj.addNode(newNode, createThirdCondition());
		testObj.removeNode(newNode);
		
		assertEquals(1, testObj.getNodes().size());
		assertEquals(1, Tree.currentIndex);
		assertEquals(0, testObj.getCurrentNode().getLevel());
	}
	
	@Test
	public void removeNode_root_withoutChildren() {
		testObj.removeNode(testObj.getRoot());
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
		assertEquals(0, Tree.currentIndex);
	}

	@Test
	public void removeNode_root_withChildren() {
		testObj.addNode(testObj.getCurrentNode(), createFirstCondition());
		testObj.addNode(testObj.getCurrentNode(), createSecondCondition());
		testObj.removeNode(testObj.getRoot());
		assertNull(testObj.getRoot());
		assertNull(testObj.getCurrentNode());
		assertEquals(0, Tree.currentIndex);
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

	@Test
	public void setNewNodeAsRoot() {

	}
	
	
	
}
