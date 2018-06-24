package com.pecapoker.texasholdem;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.PcConst.Suits;

public class YkPairTest {

	@Test
	public void testCompareTo() {
		// ペアの大きさで比較
		FiveCard fv = new FiveCard(new Card(Suits.CRAB, 6),
				new Card(Suits.CRAB, 2),
				new Card(Suits.CRAB, 4),
				new Card(Suits.CRAB, 5),
				new Card(Suits.DIA, 5)
				);
		YkPair pair1 = new YkPair(fv, 5);
		YkPair pair2 = new YkPair(fv, 6);
		assertEquals(true, pair1.compareTo(pair2) < 0);

		pair2 = new YkPair(fv, 5);

		assertEquals(0, pair1.compareTo(pair2));

		// ペアの大きさで比較
		fv = new FiveCard(new Card(Suits.CRAB, 6),
				new Card(Suits.CRAB, 2),
				new Card(Suits.CRAB, 4),
				new Card(Suits.CRAB, 5),
				new Card(Suits.DIA, 5)
				);
		pair1 = new YkPair(fv, 1);
		pair2 = new YkPair(fv, 6);
		assertEquals(true, pair1.compareTo(pair2) > 0);

		pair1 = new YkPair(fv, 5);
		pair2 = new YkPair(fv, 5);

		assertEquals(0, pair1.compareTo(pair2));

		// キッカーで比較
		FiveCard fv2 = new FiveCard(new Card(Suits.CRAB, 6),
				new Card(Suits.CRAB, 2),
				new Card(Suits.DIA, 3),
				new Card(Suits.CRAB, 5),
				new Card(Suits.DIA, 5)
				);
		pair2 = new YkPair(fv2, 5);
		assertEquals(true, pair1.compareTo(pair2) > 0);

	}

}
