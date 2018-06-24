package com.pecapoker.texasholdem;

import java.util.ArrayList;
import java.util.List;

import com.pecapoker.playingcards.CardSet;
import com.pecapoker.playingcards.Player;
import com.pecapoker.playingcards.Pot;
import com.pecapoker.texasholdem.Game.Step;
import com.pecapoker.texasholdem.HdConst.RoundStatus;

class HoldemDealer extends com.pecapoker.playingcards.Dealer {
	public HoldemDealer()
	{
		super();
	}
	private final int NO_MIN_CHIP = 99999999;

	@Override
	protected Player decideWinner2players(Player p1, Player p2, CardSet board) {
		if ((((HoldemPlayer)p1).getRoundStatus() == RoundStatus.FOLDED)
			&& (((HoldemPlayer)p1).getRoundStatus() == RoundStatus.FOLDED) )
		{
			return null;
		}
		if (((HoldemPlayer)p1).getRoundStatus() == RoundStatus.FOLDED)
		{
			return p2;
		}
		if (((HoldemPlayer)p2).getRoundStatus() == RoundStatus.FOLDED)
		{
			return p1;
		}

		// 役の勝負
		assert board.size() == 5;
		FiveCard p1FiveCard = getMaxFiveCard(board.addCardSet(p1.getPocket()));
		FiveCard p2FiveCard = getMaxFiveCard(board.addCardSet(p2.getPocket()));

		if (p1FiveCard.getYaku().compareTo(p2FiveCard.getYaku()) == 0) {
			return null;
		}
		if (p1FiveCard.getYaku().compareTo(p2FiveCard.getYaku()) > 0) {
			return p1;
		}

		return p2;
	}

	public FiveCard getMaxFiveCard(CardSet allCards) {
		assert allCards.size() == 7;

		FiveCard maxFiveCard = null;
		// 7枚のカードのうち、除く２枚を決めて、５枚分の強さを測定、最大となる５枚を返す
		for(int i = 0; i < allCards.size() - 1; i++) {
			for(int j = i + 1; j < allCards.size(); j++) {
				FiveCard currentFiveCard = allCards.getFiveCardExcept(i, j);
				if ((maxFiveCard == null) || (currentFiveCard.getYaku().compareTo(maxFiveCard.getYaku()) > 0)) {
					maxFiveCard = currentFiveCard;
				}
			}
		}
		return maxFiveCard;
	}

	public void printAllPlayerHands() {
		for(Player p : players) {
			System.out.print(p + " is ");
			p.printPocket();
		}
	}

	private List<HoldemPlayer> _selectWinners(List<Player> activePlayers, CardSet board)
	{
		List<HoldemPlayer> winners = new ArrayList<HoldemPlayer>();
		for (Player pl : activePlayers) {
			HoldemPlayer p = (HoldemPlayer)pl;
			if (p.isFolded()) {
				continue;
			}
			if (winners.size() == 0) {
				winners.add(p);
			}
			else {
				HoldemPlayer winner = winners.get(0);
				winner = (HoldemPlayer)decideWinner2players(winner, p, board);
				if (winner == null) {
					winners.add(p);
				}
				else if (winner == p) {
					winners.clear();
					winners.add(p);
				}
				else {
					// 何もしない
					;
				}
			}
		}
		return winners;
	}

	// TODO 7.ストレート、フラッシュ他の役
	// TODO 7.5 画面

	// TODO 8.button
	// TODO 9.ブラインド
	// TODO 10.Structure
	// TODO 11.トーナメント
	public List<HoldemPlayer> concludeHand(Pot pot, CardSet board)
	{
		printAllPlayerHands();

		List<HoldemPlayer> winners = new ArrayList<HoldemPlayer>();
		winners = _selectWinners(pot.getPlayers(), board);
		assert winners.size() > 0;
		int dividedChip = pot.getChip() / winners.size();
		for (Player p : winners) {
			p.receiveChip(dividedChip);
		}
		return winners;
	}

	public void initRoundStatusAfterRaise(Player raiser)
	{
		for(Player p : players)
		{
			HoldemPlayer hp = (HoldemPlayer)p;
			if (p == raiser)
			{
				continue;
			}
			if (hp.isFolded())
			{
				continue;
			}
			if (hp.isAllIned())
			{
				continue;
			}
			hp.resetStepActionStatusOnly();
		}
	}
	/**
	 * 全プレイヤーにアクションさせる
	 */
	public void round() throws RoundRulesException {
		/**
		 * 全Playerを１周するまでのルール
		 */
		RoundActionRule rar = new RoundActionRule();
		Player raiser = null;
		rar.setCallAmount(0);
		do
		{
			initRoundStatusAfterRaise(raiser);
			raiser = _scanPlayers(raiser, rar);
		} while (raiser != null);
	}

	/**
	 * 新たなレイズが入るか、全員回るまで回す
	 * @param raiser　このプレイヤーの次の人から回る
	 * @return　レイズが入ったらそのプレイヤー
	 * @throws RoundRulesException
	 */
	private Player _scanPlayers(Player raiser, RoundActionRule rar) throws RoundRulesException
	{
		int actionedNum = 0;
		int iRaiser = _getPlayerIndex(raiser);
		if (iRaiser != -1) {
			actionedNum++;
		}
		int iPlayer = this.players.getNextIndex(iRaiser);
		assert iPlayer >= 0;

		while(actionedNum < this.players.size())
		{
			HoldemPlayer p = (HoldemPlayer)this.players.get(iPlayer);
			if (p.getRoundStatus() == RoundStatus.FOLDED) {
				iPlayer = this.players.getNextIndex(iPlayer);
				actionedNum++;
				continue;
			}
			p.getRoundAction(rar);
			actionToRoundActionRule(rar, p.getLastStepAction());
			if (p.isRaised()) {
				return p;
			}
			iPlayer = this.players.getNextIndex(iPlayer);
			actionedNum++;
		}
		return null;
	}

	/**
	 * アクションの結果をルール（最低ベット額）に反映
	 * @param rar
	 * @param p
	 */
	public void actionToRoundActionRule(RoundActionRule rar, StepAction ac) {
		if (ac.isRaise()) {
			rar.setLastRaiseDiffAmount(ac.getChip() - rar.getCallAmount());
			rar.setCallAmount(ac.getChip());
		}
	}

	private int _getPlayerIndex(Player raiser) {
		int iPlayer = -1;
		if (raiser != null) {
			iPlayer = this.players.indexOf(raiser);
			assert iPlayer >= 0;
		}
		return iPlayer;
	}

	public void resetHand() {
		for(Player p : players) {
			HoldemPlayer hp = (HoldemPlayer)p;
			hp.resetPocket();
			hp.initStepAction();
			hp.resetHandTotalChip();
		}
	}

	public void initStep() {
		for(Player p : players) {
			HoldemPlayer hp = (HoldemPlayer)p;
			if (hp.isFolded() || hp.isAllIned()) {
				hp.resetStepActionChipOnly();
				continue;
			}
			hp.initStepAction();
		}
	}

	public List<Pot> collectChipToPot() {
		List<Pot> pots = new ArrayList<Pot>();
		_makePots(pots, 0);
		return pots;
	}

	/**
	 * 再帰的にサイドポットを作成する
	 * @param pots
	 * @param beforeMinChip
	 */
	private void _makePots(List<Pot> pots, int beforeMinChip) {
		// 一番安いFold以外のActionを探す
		int minChip = getMinimumChipFromHandTotalChip(beforeMinChip);
		if (minChip == NO_MIN_CHIP) {
			return;
		}

		Pot currentPot = new Pot();

		// プレイヤーのhandTotalChipからchipを集める
		for(Player p : this.players) {
			HoldemPlayer hp = (HoldemPlayer)p;
			assert minChip > beforeMinChip;
			if ((hp.getHandTotalChip() >= minChip) && !hp.isFolded())
			{
				currentPot.addPlayer(hp);
			}
			if (hp.getHandTotalChip() > beforeMinChip) {
				currentPot.addChip(Math.min(hp.getHandTotalChip(), minChip) - beforeMinChip);
			}
		}
		pots.add(currentPot);

		_makePots(pots, minChip);
	}

	/**
	 * 前回作成したポットのminChipよりも大きい額の中で、最小の額を探す
	 * @param beforeMinChip
	 * @return
	 */
	public int getMinimumChipFromHandTotalChip(int beforeMinChip) {
		int minChip = NO_MIN_CHIP;
		for (Player p : this.players) {
			HoldemPlayer hp = (HoldemPlayer)p;
			if (hp.isFolded()) {
				continue;
			}
			if ((hp.getHandTotalChip() > beforeMinChip)
				&& (hp.getHandTotalChip() < minChip)
					) {
				minChip = hp.getHandTotalChip();
			}
		}
		return minChip;
	}

	public ArrayList<Player> getPots() {
		return null;
	}

	public boolean isAllFolded() {
		int foldedNum = 0;
		for(Player p : this.players) {
			HoldemPlayer hp = (HoldemPlayer)p;
			if (hp.isFolded()) {
				foldedNum++;
			}
		}
		return foldedNum >= this.players.size()-1;
	}

	public CardSet dealBoard(Step s) {
		CardSet cs = new CardSet();
		switch(s) {
		case PREFLOP:
			break;
		case FLOP:
			cs.push(this.getCardFromDeck());
			cs.push(this.getCardFromDeck());
			cs.push(this.getCardFromDeck());
			break;
		case TURN:
			cs.push(this.getCardFromDeck());
			break;
		case RIVER:
			cs.push(this.getCardFromDeck());
			break;
		}
		return cs;
	}


}
