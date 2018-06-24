package com.pecapoker.texasholdem;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.PcConst.Suits;

public class YakuTest {
	@Test
	public void testCompareTo() {
		FiveCard fv1 = new FiveCard(new Card(Suits.CRAB, 6),
				new Card(Suits.CRAB, 2),
				new Card(Suits.CRAB, 4),
				new Card(Suits.CRAB, 5),
				new Card(Suits.DIA, 7)
				);
		FiveCard fv2 = new FiveCard(new Card(Suits.CRAB, 6),
				new Card(Suits.CRAB, 2),
				new Card(Suits.CRAB, 4),
				new Card(Suits.CRAB, 5),
				new Card(Suits.DIA, 8)
				);
		Yaku buta1 = new Yaku(fv1);
		Yaku buta2 = new Yaku(fv2);
		assertEquals(true, buta1.compareTo(buta2) < 0);
	}
}
