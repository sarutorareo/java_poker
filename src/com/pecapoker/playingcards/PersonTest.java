package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonTest {

	@Test
	public void testConstructor() {
		Person psn = new Person(0, "abc");
		assertEquals(0, psn.getId());
		assertEquals("abc", psn.getName());
	}

}
