package com.pecapoker.texasholdem;

import java.util.ArrayList;
import java.util.List;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.CardComparator_rank;
import com.pecapoker.playingcards.CardComparator_value;
import com.pecapoker.playingcards.CardSet;
import com.pecapoker.playingcards.RankCount;

public class FiveCard extends CardSet {

	private Yaku yaku;
	public FiveCard(Card c1, Card c2, Card c3, Card c4, Card c5)
	{
		super();
		this.push(c5);
		this.push(c4);
		this.push(c3);
		this.push(c2);
		this.push(c1);
		yaku = _calcYaku();
	}
	public FiveCard(CardSet cs) {
		this(cs.get(0),cs.get(1),cs.get(2),cs.get(3),cs.get(4));
	}
	private Yaku _calcYaku() {
		List<RankCount> rankCountList = _getRankCountList();
		int maxSuitsCount = _getMaxSuitsCount();
		int contNum = _getContCount();
		int contNum_Ais1 = _getContCount_Ais1();

		// ストレートフラッシュ
		if (maxSuitsCount >= 5) {
			// （AをKの次として扱う）ストレート
			if (contNum >= 5) {
				return new YkStraightFlash(this);
			}
			// （Aを1として扱う）ストレート
			if (contNum_Ais1 >= 5) {
				_changeAtoSmallA();
				return new YkStraightFlash(this);
			}
		}

		// 4カード
		if (rankCountList.size() >= 1 && rankCountList.get(0).count >= 4) {
			return new YkFourOfAKind(this, rankCountList.get(0).rank);
		}
		// フルハウス
		if (rankCountList.size() >= 2 &&
				(rankCountList.get(0).count >= 3 || rankCountList.get(1).count >= 3)) {
			int trioRank, pairRank;
			if (rankCountList.get(0).count >= 3) {
				trioRank = rankCountList.get(0).rank;
				pairRank = rankCountList.get(1).rank;
			}
			else {
				trioRank = rankCountList.get(1).rank;
				pairRank = rankCountList.get(0).rank;
			}
			return new YkFullHouse(this, trioRank, pairRank);
		}
		// フラッシュ
		if (maxSuitsCount >= 5) {
			return new YkFlash(this);
		}
		// （AをKの次として扱う）ストレート
		if (contNum >= 5) {
			return new YkStraight(this);
		}
		// （Aを1として扱う）ストレート
		if (contNum_Ais1 >= 5) {
			_changeAtoSmallA();
			return new YkStraight(this);
		}
		// 3カード
		if (rankCountList.size() >= 1 && rankCountList.get(0).count >= 3) {
			return new YkThreeOfAKind(this, rankCountList.get(0).rank);
		}
		// 2ペア
		if (rankCountList.size() >= 2) {
			return new YkTwoPair(this, rankCountList.get(0).rank, rankCountList.get(1).rank);
		}
		// 1ペア
		if (rankCountList.size() >= 1) {
			return new YkPair(this, rankCountList.get(0).rank);
		}
		return new Yaku(this);
	}
	private void _changeAtoSmallA() {
		assert this.get(0).getRank() == 1;
		Card smallA = new SmallACard(this.get(0));
		this.cardList.remove(0);
		this.push(smallA);
	}

	/**
	 * 連続するランクの数を数える
	 * @param contNum
	 * @return
	 */
	private int _getContCount() {
		int contNum = 0;
		this.getCardList().sort(new CardComparator_value());
		Card beforeCard = null;
		for (Card c : this.getCardList()) {
			if (beforeCard != null){
				if (c.getValue() != beforeCard.getValue() + 1) {
					break;
				}
			}
			beforeCard = c;
			contNum++;
		}
		return contNum;
	}

	/**
	 * 連続するランクの数を数える
	 * @param contNum
	 * @return
	 */
	private int _getContCount_Ais1() {
		int contNum = 0;
		this.getCardList().sort(new CardComparator_rank());
		Card beforeCard = null;
		for (Card c : this.getCardList()) {
			if (beforeCard != null){
				if (c.getRank() != beforeCard.getRank() + 1) {
					break;
				}
			}
			beforeCard = c;
			contNum++;
		}
		return contNum;
	}

	/**
	 * ランク毎に枚数を数える
	 * @return
	 */
	private List<RankCount> _getRankCountList() {
		List<RankCount> rankCountList = new ArrayList<RankCount>();

		int rankArray[] = new int[14];
		for(Card c : this.getCardList())
		{
			rankArray[c.getRank()]++;
		}
		for(int i = 1 ; i < rankArray.length; i++) {
			if (rankArray[i] >= 2) {
				rankCountList.add(new RankCount(i, rankArray[i]));
			}
		}
		return rankCountList;
	}

	/**
	 * 最大の同スーツの枚数を得る
	 * @return
	 */
	private int _getMaxSuitsCount() {
		int maxNum = -1;
		int suitsArray[] = new int[4];

		for(Card c : this.getCardList())
		{
			suitsArray[c.getSuits().ordinal()]++;
		}
		for (int i = 0; i < suitsArray.length; i++) {
			if (suitsArray[i] > maxNum) {
				maxNum = suitsArray[i];
			}
		}
		return maxNum;
	}

	public Yaku getYaku() {
		return yaku;
	}
}
