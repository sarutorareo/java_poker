package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.CardSet;

public class YkTwoPair extends Yaku {
	int pairRank1;
	int pairRank2;

	public int getPairRank1()
	{
		return pairRank1;
	}
	public int getPairRank2()
	{
		return pairRank2;
	}

	@Override
	protected int getYakuValue()
	{
		return 2;
	}

	public YkTwoPair(FiveCard fv, int pn1, int pn2) {
		super(fv);
		pairRank1 = Math.max(pn1,  pn2);
		pairRank2 = Math.min(pn1,  pn2);
	}

	@Override
	public int compareTo(Yaku other) {
		// 役で差がつく
		if (this.getYakuValue() - other.getYakuValue() != 0) {
			return this.getYakuValue() - other.getYakuValue();
		}
		// 大きいほうのペアの数字で差がつく
		if (Card.rankToValue(this.getPairRank1()) - Card.rankToValue(((YkTwoPair)other).getPairRank1()) != 0){
			return Card.rankToValue(this.getPairRank1()) - Card.rankToValue(((YkTwoPair)other).getPairRank1());
		}
		// 小さいほうのペアの数字で差がつく
		if (Card.rankToValue(this.getPairRank2()) - Card.rankToValue(((YkTwoPair)other).getPairRank2()) != 0){
			return Card.rankToValue(this.getPairRank2()) - Card.rankToValue(((YkTwoPair)other).getPairRank2());
		}
		// キッカーで差がつく
		CardSet kicker1 = this.getFiveCard().diffCardSetExceptRank2(this.getPairRank1(), this.getPairRank2());
		CardSet kicker2 = other.getFiveCard().diffCardSetExceptRank2(((YkTwoPair)other).getPairRank1(), ((YkTwoPair)other).getPairRank2());
		return kicker1.compareTo(kicker2);
	}

}
