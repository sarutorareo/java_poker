package com.pecapoker.texasholdem;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.CardSet;

/**
 * ペアを表すクラス
 * @author jirou
 *
 */
public class YkPair extends Yaku {
	int pairRank;

	public int getPairRank()
	{
		return pairRank;
	}

	@Override
	protected int getYakuValue()
	{
		return 1;
	}
	public YkPair(FiveCard fv, int pn) {
		super(fv);
		pairRank = pn;
	}

	@Override
	public int compareTo(Yaku other) {
		// 役で差がつく
		if (this.getYakuValue() - other.getYakuValue() != 0) {
			return this.getYakuValue() - other.getYakuValue();
		}
		// ペアの数字で差がつく
		if (this.getPairRank() - ((YkPair)other).getPairRank() != 0){
			return Card.rankToValue(this.getPairRank()) - Card.rankToValue(((YkPair)other).getPairRank());
		}
		// キッカーで差がつく
		CardSet kicker1 = this.getFiveCard().diffCardSetExceptRank(this.getPairRank());
		CardSet kicker2 = other.getFiveCard().diffCardSetExceptRank(((YkPair)other).getPairRank());
		return kicker1.compareTo(kicker2);
	}

}
