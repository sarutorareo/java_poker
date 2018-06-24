package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pecapoker.playingcards.PcConst.Suits;

public class CardTest {

	@Test
	public void testConstructor() {
		Card c = new Card(Suits.SPADE, 1);
		assertEquals(Suits.SPADE, c.getSuits());
		assertEquals(1, c.getRank());
	}

	@Test
	/**
	 * 等価判定のテスト
	 */
	public void testEquals() {
		Card c1 = new Card(Suits.SPADE, 1);
		Card c2 = null;

		assertEquals(false, c1.equals(c2));

		c2 = new Card(Suits.SPADE, 1);
		assertEquals(true, c1.equals(c2));

		Card c3 = new Card(Suits.SPADE, 2);
		assertEquals(false, c1.equals(c3));

		Card c4 = new Card(Suits.CRAB, 1);
		assertEquals(false, c1.equals(c4));
	}

	@Test
	public void testToString()
	{
		Card c = new Card(Suits.SPADE, 1);
		assertEquals("♠A", c.toString());
		c = new Card(Suits.DIA, 13);
		assertEquals("◆K", c.toString());
		c = new Card(Suits.DIA, 12);
		assertEquals("◆Q", c.toString());
		c = new Card(Suits.DIA, 11);
		assertEquals("◆J", c.toString());
		c = new Card(Suits.DIA, 10);
		assertEquals("◆T", c.toString());
		c = new Card(Suits.DIA, 9);
		assertEquals("◆9", c.toString());

	}

	@Test
	public void testCompareTo()
	{
		Card c1 = new Card(Suits.DIA, 9);
		Card c2 = new Card(Suits.DIA, 10);
		assertEquals(true, c1.compareTo(c2) < 0);

		c1 = new Card(Suits.DIA, 1);
		c2 = new Card(Suits.DIA, 10);
		assertEquals(true, c1.compareTo(c2) > 0);
	}

	@Test
	public void testConstructor_string()
	{
		Card c = new Card("SA");
		assertEquals(Suits.SPADE, c.getSuits());
		assertEquals(1, c.getRank());
		c = new Card("C2");
		assertEquals(Suits.CRAB, c.getSuits());
		assertEquals(2, c.getRank());
		c = new Card("C1");
		assertEquals(Suits.CRAB, c.getSuits());
		assertEquals(1, c.getRank());
	}
}
