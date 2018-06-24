package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.CardSet;

public class YkThreeOfAKind extends Yaku {
	int trioRank;

	public int getTrioRank()
	{
		return trioRank;
	}

	@Override
	protected int getYakuValue()
	{
		return 3;
	}
	public YkThreeOfAKind(FiveCard fv, int rank) {
		super(fv);
		trioRank = rank;
	}

	@Override
	public int compareTo(Yaku other) {
		// 役で差がつく
		if (this.getYakuValue() - other.getYakuValue() != 0) {
			return this.getYakuValue() - other.getYakuValue();
		}
		// トリオの数字で差がつく
		if (Card.rankToValue(this.getTrioRank()) - Card.rankToValue(((YkThreeOfAKind)other).getTrioRank()) != 0) {
			return Card.rankToValue(this.getTrioRank()) - Card.rankToValue(((YkThreeOfAKind)other).getTrioRank());
		}
		// キッカーで差がつく
		CardSet kicker1 = this.getFiveCard().diffCardSetExceptRank(this.getTrioRank());
		CardSet kicker2 = other.getFiveCard().diffCardSetExceptRank(((YkThreeOfAKind)other).getTrioRank());
		return kicker1.compareTo(kicker2);
	}
}
