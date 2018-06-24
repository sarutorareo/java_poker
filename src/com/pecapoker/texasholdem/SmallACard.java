package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.Card;

public class SmallACard extends Card {
	public SmallACard(Card c) {
		super(c.getSuits(), c.getRank());
		assert c.getRank() == 1: c.getRank() + " is not A";
	}

	@Override
	public int getValue() {
		return this.getRank();
	}
}
