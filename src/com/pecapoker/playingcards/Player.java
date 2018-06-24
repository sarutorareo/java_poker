package com.pecapoker.playingcards;

abstract public class Player extends Person {
	protected Pocket pocket;
	protected int chip = 1000;

	public Player(int id, String name)
	{
		super(id, name);
		// Playerのidは1以上　（0はディーラー)
		assert id > 0;
		pocket = new Pocket();
	}

	/**
	 * 手札の枚数を返す
	 */
	public int getPocketSize() {
		return pocket.size();
	}

	/**
	 * 手札を返す
	 */
	public Pocket getPocket() {
		return pocket;
	}

	/**
	 * カードを一枚受け取り、手札に加える
	 * @param c
	 */
	public void receivePocket(Card c)
	{
		assert c != null;
		pocket.push(c);
	}

	/**
	 * チップを受け取る
	 * @param チップ
	 */
	public void receiveChip(int c)
	{
		this.chip += c;
	}

	public int getChip()
	{
		return chip;
	}

	public String getPocketStr()
	{
		String ret = "";
		for(Card c : this.pocket.getCardList())
		{
			ret += c.toString() + " ";
		}
		return ret;
	}
	public void printPocket()
	{
		System.out.println(getPocketStr());;
	}

	/**
	 * 手札の中で一番大きいカードを返す スーツは問わない
	 * @return 一番大きいカード　一枚もなければnull
	 */
	public Card getHighestCard() {
		return this.pocket.getHighestCard();
	}

	/**
	 * 現在の手札を返却してリセット
	 * @return 現在の手札
	 */
	public Pocket resetPocket() {
		Pocket ret = this.pocket;
		this.pocket = new Pocket();
		return ret;
	}

}
