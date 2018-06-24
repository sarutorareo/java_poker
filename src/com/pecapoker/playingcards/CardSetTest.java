package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pecapoker.playingcards.PcConst.Suits;

public class CardSetTest {

	@Test
	public void testConstructor() {
		CardSet cs = new CardSet();
		assertEquals(0, cs.size());

		// Cardがなければnullを返す
		Card c = cs.pop();
		assertEquals(null, c);
	}

	@Test
	public void testPushPop() {
		CardSet cs = new CardSet();
		assertEquals(0, cs.size());

		Card c = new Card(Suits.CRAB, 1);

		cs.push(c);
		assertEquals(1, cs.size());

		Card c2 = cs.pop();
		assertEquals(c.getRank(), c2.getRank());
		assertEquals(c.getSuits(), c2.getSuits());
	}

	@Test
	public void testGetCardSetExcept() {
		CardSet cs = new CardSet();
		Card c = new Card(Suits.CRAB, 1);
		cs.push(c);
		c = new Card(Suits.CRAB, 2);
		cs.push(c);
		c = new Card(Suits.CRAB, 3);
		cs.push(c);
		c = new Card(Suits.CRAB, 4);
		cs.push(c);
		c = new Card(Suits.CRAB, 5);
		cs.push(c);
		c = new Card(Suits.CRAB, 6);
		cs.push(c);
		c = new Card(Suits.CRAB, 7);
		cs.push(c);

		// cs = 7654321の7枚が作られている
		// 先頭から２枚　７、６を抜く
		CardSet cs5 = cs.getFiveCardExcept(0, 1);
		assertEquals(5, cs5.size());
		assertEquals(Suits.CRAB, cs5.getCardList().get(0).getSuits());
		assertEquals(1, cs5.getCardList().get(0).getRank());
		assertEquals(2, cs5.getCardList().get(1).getRank());
		assertEquals(3, cs5.getCardList().get(2).getRank());
		assertEquals(4, cs5.getCardList().get(3).getRank());
		assertEquals(5, cs5.getCardList().get(4).getRank());

		// 末尾から２枚　１、２を抜く
		cs5 = cs.getFiveCardExcept(5, 6);
		assertEquals(5, cs5.size());
		assertEquals(Suits.CRAB, cs5.getCardList().get(0).getSuits());
		assertEquals(3, cs5.getCardList().get(0).getRank());
		assertEquals(4, cs5.getCardList().get(1).getRank());
		assertEquals(5, cs5.getCardList().get(2).getRank());
		assertEquals(6, cs5.getCardList().get(3).getRank());
		assertEquals(7, cs5.getCardList().get(4).getRank());
	}

	@Test
	public void testDiffCardSetExceptRank() {
		CardSet cs = new CardSet();
		Card c = new Card(Suits.CRAB, 1);
		cs.push(c);
		c = new Card(Suits.CRAB, 2);
		cs.push(c);
		c = new Card(Suits.CRAB, 3);
		cs.push(c);
		c = new Card(Suits.CRAB, 4);
		cs.push(c);
		c = new Card(Suits.CRAB, 5);
		cs.push(c);
		c = new Card(Suits.CRAB, 6);
		cs.push(c);
		c = new Card(Suits.DIA, 6);
		cs.push(c);

		// cs = 7654321の7枚が作られている
		// 3のカードを抜く
		CardSet csDiff = cs.diffCardSetExceptRank(3);
		assertEquals(6, csDiff.size());
		assertEquals(Suits.CRAB, csDiff.getCardList().get(0).getSuits());
		assertEquals(1, csDiff.getCardList().get(0).getRank());
		assertEquals(2, csDiff.getCardList().get(1).getRank());
		assertEquals(4, csDiff.getCardList().get(2).getRank());
		assertEquals(5, csDiff.getCardList().get(3).getRank());
		assertEquals(6, csDiff.getCardList().get(4).getRank());
		assertEquals(6, csDiff.getCardList().get(5).getRank());

		// 6の２枚を抜く
		csDiff = cs.diffCardSetExceptRank(6);
		assertEquals(5, csDiff.size());
		assertEquals(Suits.CRAB, csDiff.getCardList().get(0).getSuits());
		assertEquals(1, csDiff.getCardList().get(0).getRank());
		assertEquals(2, csDiff.getCardList().get(1).getRank());
		assertEquals(3, csDiff.getCardList().get(2).getRank());
		assertEquals(4, csDiff.getCardList().get(3).getRank());
		assertEquals(5, csDiff.getCardList().get(4).getRank());
	}

	@Test
	public void testCompareTo() {
		CardSet cs1 = new CardSet();
		Card c = new Card(Suits.CRAB, 1);
		cs1.push(c);
		c = new Card(Suits.CRAB, 2);
		cs1.push(c);
		c = new Card(Suits.CRAB, 3);
		cs1.push(c);

		CardSet cs2 = new CardSet();
		c = new Card(Suits.DIA, 1);
		cs2.push(c);
		c = new Card(Suits.DIA, 2);
		cs2.push(c);
		c = new Card(Suits.DIA, 3);
		cs2.push(c);

		assertEquals(0, cs1.compareTo(cs2));
		assertEquals(0, cs2.compareTo(cs1));

		// ２番目で差がつく
		cs2 = new CardSet();
		c = new Card(Suits.DIA, 2);
		cs2.push(c);
		c = new Card(Suits.DIA, 1);
		cs2.push(c);
		c = new Card(Suits.DIA, 4);
		cs2.push(c);

		assertEquals(true, cs1.compareTo(cs2) < 0);
		assertEquals(true, cs2.compareTo(cs1) > 0);

		// 1番目で差がつく
		cs2 = new CardSet();
		c = new Card(Suits.DIA, 5);
		cs2.push(c);
		c = new Card(Suits.DIA, 2);
		cs2.push(c);
		c = new Card(Suits.DIA, 4);
		cs2.push(c);

		assertEquals(true, cs1.compareTo(cs2) > 0);
		assertEquals(true, cs2.compareTo(cs1) < 0);
	}
	@Test
	public void testConstructor_string()
	{
		CardSet cs = new CardSet("STH4S5S6HTH8S4");
		assertEquals(7, cs.size());
		assertEquals(Suits.SPADE, cs.get(0).getSuits());
		assertEquals(10, cs.get(0).getRank());
		assertEquals(Suits.SPADE, cs.get(6).getSuits());
		assertEquals(4, cs.get(6).getRank());
	}
}
