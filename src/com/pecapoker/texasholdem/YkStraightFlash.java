package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.CardSet;

public class YkStraightFlash extends Yaku {
	@Override
	protected int getYakuValue()
	{
		return 8;
	}
	public YkStraightFlash(FiveCard fv) {
		super(fv);
	}

	@Override
	public int compareTo(Yaku other) {
		// 役で差がつく
		if (this.getYakuValue() - other.getYakuValue() != 0) {
			return this.getYakuValue() - other.getYakuValue();
		}
		// キッカーで差がつく
		CardSet kicker1 = this.getFiveCard();
		CardSet kicker2 = other.getFiveCard();
		return kicker1.compareTo(kicker2);
	}
}
