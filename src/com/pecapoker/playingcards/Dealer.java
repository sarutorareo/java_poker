package com.pecapoker.playingcards;

abstract public class Dealer extends Person {
	/**
	 * カードデッキ
	 */
	private Deck deck;
	/**
	 * 全Player
	 */
	protected PlayerList players;

	public Dealer () {
		super(0, "Johnny");
		deck = new Deck(false);
		players = new PlayerList();
	}
	public void shuffle() {
		deck.shuffle();
	}
	public int getDeckSize()
	{
		return this.deck.size();
	}

	public void addPlayer(Player p) {
		this.getPlayers().add(p);
	}
	public PlayerList getPlayers() {
		return players;
	}

	/**
	 * 一人のPlayerにカードを一枚配る
	 * @throws PlayingCardException カードの枚数が足りない
	 */
	public boolean dealPlayer(Player p)
	{
		Card c = this.getCardFromDeck();
		if (c == null) {
			return false;
		}
		p.receivePocket(c);
		return true;
	}

	/**
	 * デッキからカードを一枚取り出す
	 * @return 取り出したカード
	 * @throws PlayingCardException
	 */
	public Card getCardFromDeck()
	{
		if (this.deck.size() == 0) {
			return null;
		}
		Card c = this.deck.pop();
		return c;
	}

	/**
	 * すべてのPlayerにカードを一枚ずつ配る
	 * @return true : 正常に配った false : カードの枚数が足りない
	 */
	public boolean dealAllPlayers(){
		if (this.players.size() > this.deck.size()) {
			return false;
		}
		for (Player p : this.players)
		{
			dealPlayer(p);
		}
		return true;
	}

	/**
	 * 二人のプレイヤーのうち、どっちが勝ったかを判定する
	 * @param p1
	 * @param p2
	 * @return 勝ったほうのPlayer 引き分けの場合はnull
	 */
	abstract protected Player decideWinner2players(Player p1, Player p2, CardSet board);
}
