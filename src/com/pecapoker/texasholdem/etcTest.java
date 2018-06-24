package com.pecapoker.texasholdem;

import org.junit.Test;

import com.pecapoker.playingcards.PcConst.Suits;

import junit.framework.TestCase;

public class etcTest extends TestCase {

	@Test
	public void testSuitsToInt() {
		assertEquals(0, Suits.SPADE.ordinal());
		assertEquals(1, Suits.HEART.ordinal());
		assertEquals(2, Suits.DIA.ordinal());
		assertEquals(3, Suits.CRAB.ordinal());
	}

}
