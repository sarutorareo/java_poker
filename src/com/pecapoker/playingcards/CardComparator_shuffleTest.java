package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import java.security.SecureRandom;

import org.junit.Test;

public class CardComparator_shuffleTest {

	@Test
	public void testRandom() {
		SecureRandom rnd = new SecureRandom();
		int num = rnd.nextInt(100);
		System.out.println("num = " + num);
		assertEquals(true, num >= 0);
		assertEquals(true, num < 100);
	}

}
