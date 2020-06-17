package com.krystianrymonlipinski.tree.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeTest {

	Tree<Integer, String> testObj;
	
	@Before
	public void setUp() {
		Tree<Integer, String> tree = new Tree(new Node<Integer, String>(Node.Type.ROOT_NODE));
		testObj = Mockito.spy(tree);
	}

	@Test
	public void createTree() {
		Node<Integer> root = new Node<Integer, String>(Node.Type.ROOT_NODE);
		Tree<Integer, String> tree = new Tree(root);
		assertEquals(1, tree.getNodes().getSize());
		assertEquals(root, tree.getRoot());
		assertEquals(root, tree.getCurrentNode());
	}

	@Test
	public void addNode_secondRootError() {
		Node<Integer, String> newNode = new Node<Integer, String>(Node.Type.ROOT_NODE);

	}

	@Test
	public void addNode_correctCreation() {
		Node<Integer> newNode = testObj.addNode(testObj.getRoot(), "first");
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
		assertEquals(2, testObj.getNodes().size());
		assertEquals(1, testObj.getRoot().getChildren().size());
		assertEquals(newNode, testObj.getRoot().getChildren().get(0));
	}

	@Test
	public void moveDown() {
		Node<Integer> newNode = testObj.addNode(testObj.getRoot(), "node");
		testObj.moveDown("node");
		assertEquals(newNode, testObj.getCurrentNode());
	}

	@Test
	public void moveUp() {
		Node<Integer> newNode = testObj.addNode(testObj.getRoot(), "node");
		testObj.moveDown("node");
		testObj.moveUp();
		assertEquals(testObj.getRoot(), testObj.getCurrentNode());
	}

	@Test 
	public void removeNode_withoutChildren() {
		Node<Integer> newNode = testObj.addNode(testObj.getRoot(), "node");
		testObj.removeNode(newNode);
		assertEquals(1, testObj.getNodes().size());
	}

	@Test
	public void removeNode_withChilden() {
		Node<Integer> newNode = testObj.addNode(testObj.getRoot(), "first_level");
		testObj.addNode(newNode, "second_level_one");
		testObj.addNode(newNode, "second_level_two");
		testObj.removeNode(newNode);

		assertEquals(1, testObj.getNodes().getSize());
	}
	
}
