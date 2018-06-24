package com.pecapoker.playingcards;

import com.pecapoker.playingcards.PcConst.Suits;

public class Deck extends CardSet{
	public Deck(boolean useJoker) {
		_initCards();
		if (useJoker) {
			;
		}
	}

	private void _initCards()
	{
		Suits[] sArray = {Suits.SPADE, Suits.HEART, Suits.DIA, Suits.CRAB};
		for(Suits s : sArray)
		{
			for(int no = 1; no <= PcConst.NO_KING; no++)
			{
				cardList.add(new Card(s, no));
			}
		}
	}

}
