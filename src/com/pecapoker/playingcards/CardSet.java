package com.pecapoker.playingcards;

import java.util.ArrayList;
import java.util.List;

import com.pecapoker.texasholdem.FiveCard;

public class CardSet {
	protected List<Card> cardList = new ArrayList<Card>();

	public CardSet() {
	}

	public CardSet(String str) {
		assert str.length() == 7*2;
		this.add(new Card(str.substring(0, 2)));
		this.add(new Card(str.substring(2, 4)));
		this.add(new Card(str.substring(4, 6)));
		this.add(new Card(str.substring(6, 8)));
		this.add(new Card(str.substring(8, 10)));
		this.add(new Card(str.substring(10, 12)));
		this.add(new Card(str.substring(12, 14)));
	}

	public int size(){
		return cardList.size();
	}

	public List<Card> getCardList(){
		return cardList;
	}

	public Card pop() {
		if (cardList.size() > 0)
		{
			Card c = cardList.get(0);
			cardList.remove(0);
			return c;
		}
		else {
			return null;
		}
	}

	public void push(Card c) {
		cardList.add(0, c);
	}
	public void add(Card c) {
		cardList.add(cardList.size(), c);
	}

	/**
	 * 自分自身に指定されたCardSetを追加する
	 * @param cs
	 */
	public void mergeCardSet(CardSet cs) {
		for(Card c : cs.getCardList()) {
			this.cardList.add(c);
		}
	}

	/**
	 * 自分自身と、指定されたCardSetを合わせたCardSetを返す
	 * @param hand
	 * @return
	 */
	public CardSet addCardSet(CardSet cs) {
		CardSet allCs = new CardSet();
		for(Card c : this.getCardList()) {
			allCs.push(c);
		}
		for(Card c : cs.getCardList()) {
			allCs.push(c);
		}
		return allCs;
	}

	/**
	 * 指定された数字のカードを除いたCardSetを返す
	 * @param rank
	 * @return
	 */
	public CardSet diffCardSetExceptRank(int rank) {
		return diffCardSetExceptRank2(rank, -1);
	}

	/**
	 * 指定された2つの数字のカードを除いたCardSetを返す
	 * @param rank
	 * @return
	 */
	public CardSet diffCardSetExceptRank2(int rank1, int rank2) {
		CardSet cs = new CardSet();
		for (Card c : this.getCardList()) {
			if ((c.getRank() == rank1) || (c.getRank() == rank2)) {
				continue;
			}
			cs.push(c);
		}
		return cs;
	}


	public void shuffle() {
		cardList.sort(new CardComparator_shuffle());
	}

	@Override
	public String toString()
	{
		String str = "";
		for(Card c : this.getCardList()) {
			str += c;
		}
		return str;
	}

	/**
	 * 指定された二枚を除いたCardSetを返す
	 * @param except1
	 * @param except2
	 * @return
	 */
	public FiveCard getFiveCardExcept(int except1, int except2) {
		assert this.size() == 7;
		CardSet cs = new CardSet();
		for(int i = 0; i < this.size(); i++) {
			if (i != except1 && i != except2) {
				cs.push(this.getCardList().get(i));
			}
		}
		return new FiveCard(cs.pop(), cs.pop(), cs.pop(), cs.pop(), cs.pop());
	}

	/**
	 * 一番強いカードを返す
	 * @return
	 */
	public Card getHighestCard() {
		Card maxCard = null;
		for(Card c : this.cardList) {
			if (maxCard == null) {
				maxCard = c;
			}
			else if (c.getValue() > maxCard.getValue()){
				maxCard = c;
			}
		}
		return maxCard;
	}

	public int compareTo(CardSet other) {
		// 強い順にソート
		this.getCardList().sort(new CardComparator_value_desc());
		other.getCardList().sort(new CardComparator_value_desc());

		int maxSize = Math.max(this.size(), other.size());
		for(int i = 0; i < maxSize; i++) {
			if (i > this.size()) {
				return -1;
			}
			if (i > other.size()) {
				return +1;
			}
			if (this.getCardList().get(i).getValue()
					== other.getCardList().get(i).getValue()) {
				continue;
			}
			return this.getCardList().get(i).getValue() - other.getCardList().get(i).getValue();
		}
		return 0;
	}

	public Card get(int i) {
		return this.getCardList().get(i);
	}

}
