package com.krystianrymonlipinski.tree.model;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeTestWithPrimitives extends TreeTest<Integer, Character> {

	public Character createFirstCondition() {
		return 'a';
	}

	public Character createSecondCondition() {
		return 'f';
	}

	public Character createThirdCondition() {
		return 'q';
	}

}
