package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.CardSet;

public class Yaku {
	protected FiveCard fiveCard;
	protected int getYakuValue() {
		return 0;
	}

	public FiveCard getFiveCard()
	{
		return fiveCard;
	}

	public Yaku(FiveCard fv) {
		fiveCard = fv;
	}

	public int compareTo(Yaku other) {
		if (this.getYakuValue() - other.getYakuValue() != 0) {
			return this.getYakuValue() - other.getYakuValue();
		}
		// 役が同じ（ブタ同士）ならキッカー
		CardSet kicker1 = this.getFiveCard();
		CardSet kicker2 = other.getFiveCard();
		return kicker1.compareTo(kicker2);
	}

}
