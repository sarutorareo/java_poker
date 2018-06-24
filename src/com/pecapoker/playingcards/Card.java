package com.pecapoker.playingcards;

import com.pecapoker.playingcards.PcConst.Suits;

/**
 * カードクラス
 * @author jirou
 *
 */
public class Card {
	private int rank;
	private Suits suits;

	public Card(Suits s, int rank)
	{
		this.setSuits(s);
		this.setRank(rank);
	}

	public Card(String str) {
		this.setSuits(PcConst.CharToSuits(str.charAt(0)));
		this.setRank(_charToRank(str.charAt(1)));
	}

	public Suits getSuits() {
		return suits;
	}

	public void setSuits(Suits suits) {
		this.suits = suits;
	}

	public int getRank() {
		return rank;
	}

	/**
	 * カードの強さを返す
	 * No=1の場合は14を返す。それ以外はそのままNoを返す
	 * No=1 を1と評価したい場合や、スーツに強さを持たせる場合はオーバーライドするクラスを作ること
	 * @return
	 */
	public int getValue() {
		return rankToValue(this.rank);
	}

	/**
	 * カードの強さを返す
	 * No=1の場合は14を返す。それ以外はそのままNoを返す
	 * No=1 を1と評価したい場合や、スーツに強さを持たせる場合はオーバーライドするクラスを作ること
	 * @return
	 */
	static public int rankToValue(int r) {
		if (r == 1) {
			return 14;
		}
		else {
			return r;
		}
	}
	public void setRank(int no) {
		this.rank = no;
	}

	/**
	 * 等価判定
	 * @param 比較相手
	 * @return スーツ、数字とも同じならtrue
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o instanceof Card) {
			Card c = (Card)o;
			if (c.getSuits() == this.suits && c.getRank() == this.rank) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 相手のカードよりも自分のほうが強いか否か
	 * @param 比較相手
	 * @return 同じなら0, 自分のほうが強ければ 正の値, 自分のほうが弱ければ負の値
	 */
	public int compareTo(Card c) {
		if (c == null) {
			return +1;
		}
		return this.getValue() - c.getValue();
	}

	@Override
	public String toString()
	{
		String ret = "";
		switch(this.suits) {
		case CRAB:
			ret += "♣";
			break;
		case DIA:
			ret += "◆";
			break;
		case HEART:
			ret += "♥";
			break;
		case SPADE:
			ret += "♠";
			break;
		}
		ret += _rankToStr();
		return ret;
	}

	private String _rankToStr() {
		switch(this.rank) {
		case 1:
			return "A";
		case 10:
			return"T";
		case 11:
			return"J";
		case 12:
			return"Q";
		case 13:
			return"K";
		default:
			return "" + this.rank;
		}
	}
	private int _charToRank(char c) {
		switch(c) {
		case 'A':
			return 1;
		case 'T':
			return 10;
		case 'J':
			return 11;
		case 'Q':
			return 12;
		case 'K':
			return 13;
		default:
			return Integer.parseInt(String.valueOf(c));
		}
	}
}

