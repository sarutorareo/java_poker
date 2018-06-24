package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import org.junit.Test;

public class DealerTest {

	@Test
	public void testConstructor() {
		TestImpDealer d = new TestImpDealer();
		assertEquals(0, d.getId());
		assertEquals("Johnny", d.getName());
	}
	@Test
	public void testAddPlayer() {
		//
		// Setup
		//
		TestImpDealer d = new TestImpDealer();
		Player p = new TestImpPlayer(2, "testPlayer");
		//
		// Execute
		//
		d.addPlayer(p);

		//
		// Verify
		//
		assertEquals(1, d.getPlayers().size());
		assertEquals(2,d.getPlayers().get(d.getPlayers().size() -1).getId());
	}

	@Test
	public void testDeal()
	{
		//
		// Setup
		//
		TestImpDealer d = new TestImpDealer();
		Player p1 = new TestImpPlayer(1, "jirou");
		d.addPlayer(p1);

		assertEquals(52, d.getDeckSize());
		assertEquals(0, p1.getPocketSize());

		//
		// Execute
		//
		boolean ret = d.dealPlayer(p1);

		//
		// Verify
		//
		assertEquals(true, ret);
		assertEquals(51, d.getDeckSize());
		assertEquals(1, p1.getPocketSize());
	}

	@Test
	public void testDeal_fail()
	{
		//
		// Setup
		//
		TestImpDealer d = new TestImpDealer();
		Player p1 = new TestImpPlayer(1, "jirou");
		d.addPlayer(p1);

		assertEquals(52, d.getDeckSize());
		assertEquals(0, p1.getPocketSize());

		//
		// 52回までは正常に配れる
		//
		for (int i = 0; i < 52; i++) {
			boolean ret = d.dealPlayer(p1);
			assertEquals(true, ret);
		}
		assertEquals(0, d.getDeckSize());
		assertEquals(52, p1.getPocketSize());

		//
		// Execute
		// 53回目は失敗
		//
		boolean ret = d.dealPlayer(p1);
		assertEquals(false, ret);
		assertEquals(0, d.getDeckSize());
		assertEquals(52, p1.getPocketSize());

	}


	@Test
	/**
	 * 二人に２枚ずつ配るテスト
	 */
	public void testDealAllPlayers()
	{
		//
		// Setup
		//
		TestImpDealer d = new TestImpDealer();
		Player p1 = new TestImpPlayer(1, "jirou");
		Player p2 = new TestImpPlayer(2, "saburou");
		d.addPlayer(p1);
		d.addPlayer(p2);

		assertEquals(52, d.getDeckSize());

		//
		// Execute
		//
		boolean ret = d.dealAllPlayers();
		assertEquals(true, ret);
		ret = d.dealAllPlayers();
		assertEquals(true, ret);


		//
		// Verify
		//
		assertEquals(48, d.getDeckSize());
		assertEquals(2, p1.getPocketSize());
		assertEquals(2, p2.getPocketSize());

	}

	@Test
	/**
	 * 全員に配りきれないとき、半端な人数には配らない
	 */
	public void testDealAllPlayers_fail()
	{
		//
		// Setup
		//
		TestImpDealer d = new TestImpDealer();
		Player p1 = new TestImpPlayer(1, "jirou");
		Player p2 = new TestImpPlayer(2, "saburou");
		d.addPlayer(p1);
		d.addPlayer(p2);

		assertEquals(52, d.getDeckSize());

		//
		// 25枚ずつ配る
		//
		for(int i = 0; i < 25; i++) {
			boolean ret = d.dealAllPlayers();
			assertEquals(true, ret);
		}
		assertEquals(2, d.getDeckSize());
		assertEquals(25, p1.getPocketSize());
		assertEquals(25, p2.getPocketSize());

		//
		// Execute
		//
		d.dealPlayer(p2);
		assertEquals(1, d.getDeckSize());
		boolean ret = d.dealAllPlayers();

		//
		// Verify
		//
		assertEquals(false, ret);
		assertEquals(1, d.getDeckSize());
		assertEquals(25, p1.getPocketSize());
		assertEquals(26, p2.getPocketSize());
		//
	}

}
