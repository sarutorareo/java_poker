package com.pecapoker.texasholdem;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.pecapoker.playingcards.Card;
import com.pecapoker.playingcards.CardSet;
import com.pecapoker.playingcards.PcConst.Suits;
import com.pecapoker.playingcards.Player;
import com.pecapoker.playingcards.Pot;
import com.pecapoker.texasholdem.Game.Step;
import com.pecapoker.texasholdem.HdConst.RoundStatus;

import junit.framework.TestCase;

public class HoldemDealerTest extends TestCase {
	HoldemDealer d;
	TestHoldemPlayer p1;
	TestHoldemPlayer p2;
	TestHoldemPlayer p3;

	public void _initPersons( )
	{
		this.d = new HoldemDealer();
		this.p1 = new TestHoldemPlayer(1, "jirou");
		this.p2 = new TestHoldemPlayer(2, "saburou");
		this.p3 = new TestHoldemPlayer(3, "shirou");
		d.addPlayer(this.p1);
		d.addPlayer(this.p2);
		d.addPlayer(this.p3);
	}
	public CardSet _initBoard()
	{
		CardSet board = new CardSet();
		board.push(new Card(Suits.SPADE, 2));
		board.push(new Card(Suits.SPADE, 3));
		board.push(new Card(Suits.SPADE, 4));
		board.push(new Card(Suits.SPADE, 5));
		board.push(new Card(Suits.HEART, 13));
		return board;
	}

	@Override
	protected void setUp()
	{
		_initPersons();
	}
	protected void tearDown()
	{
		;
	}

	@Test
	public void testConstructor() {
		HoldemDealer d = new HoldemDealer();
		assertEquals(0, d.getId());
		assertEquals("Johnny", d.getName());
	}

	@Test
	/**
	 * （仮実装）
	 * 勝敗判定　カードの値が大きい方が勝ち
	 */
	public void testDecideWinner() {
		// TODO 勝敗判定は仮実装
		// 値の大きいカードを持っているほうが勝ち
		HoldemDealer d = new HoldemDealer();
		HoldemPlayer p1 = new HoldemPlayer(1, "jirou");
		HoldemPlayer p2 = new HoldemPlayer(2, "saburou");
		d.addPlayer(p1);
		d.addPlayer(p2);
		CardSet board = _initBoard();

		p2.receivePocket(new Card(Suits.CRAB, 8));
		p2.receivePocket(new Card(Suits.CRAB, 7));

		p1.receivePocket(new Card(Suits.DIA, 9));
		p1.receivePocket(new Card(Suits.DIA, 8));
		assertEquals(p1, d.decideWinner2players(p1, p2, board));

		//Aが一番強い
		p2.getPocket().pop();
		p2.receivePocket(new Card(Suits.CRAB, 1));
		assertEquals(p2, d.decideWinner2players(p1, p2, board));

		// 値の優劣があっても、foldしていたら負け
		p2.doFold();
		assertEquals(p1, d.decideWinner2players(p1, p2, board));

		// 値の優劣があっても、二人ともfoldしていたら引き分け
		p1.doFold();
		assertEquals(null, d.decideWinner2players(p1, p2, board));
	}

	@Test
	/**
	 * 全プレイヤーにアクションを促す
	 * アクションしたプレイヤーは、RoundStatusが何かしら変わっている
	 */
	public void testRound() throws RoundRulesException{
		assertEquals(3, d.getPlayers().size());
		//
		// Setup
		//
		d.initRoundStatusAfterRaise(null);
		for (Player p : d.getPlayers()) {
			assertEquals(RoundStatus.NONE, ((HoldemPlayer)p).getRoundStatus());
		}

		//
		// Execute
		//
		d.round();

		//
		// Verify
		//
		for (Player p : d.getPlayers()) {
			assertEquals(true, RoundStatus.NONE != ((HoldemPlayer)p).getRoundStatus());
		}
	}

	/**
	 * Raise, Call をinitRoundする
	 * Raiser, Foledした人、AllInした人は残る、それ以外はNoneになる
	 * @throws RoundRulesException
	 */
	@Test
	public void testinitRoundStatusAfterRaise() throws RoundRulesException
	{
		//
		// Setup
		//
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		for(int i = 0; i < d.getPlayers().size(); i++)
		{
			HoldemPlayer p = (HoldemPlayer)d.getPlayers().get(i);
			if (p == p3) {
				p.doRaise(rar, 200);
			}
			else {
				p.doCall(rar);
			}
		}

		//
		// Execute
		//
		d.initRoundStatusAfterRaise(p3);

		//
		// Verify
		//
		assertEquals(RoundStatus.NONE, p1.getRoundStatus());
		assertEquals(100, p1.getLastStepAction().getChip());
		assertEquals(RoundStatus.NONE, p2.getRoundStatus());
		assertEquals(100, p2.getLastStepAction().getChip());
		assertEquals(RoundStatus.RAISED, p3.getRoundStatus());
		assertEquals(200, p3.getLastStepAction().getChip());
	}

	/**
	 * Raise, Call,Fold をinitRoundする
	 * @throws RoundRulesException
	 */
	@Test
	public void testinitRoundStatusAfterRaise_keepFold() throws RoundRulesException
	{
		//
		// Setup
		//
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(10);
		for(int i = 0; i < d.getPlayers().size(); i++)
		{
			HoldemPlayer p = (HoldemPlayer)d.getPlayers().get(i);
			if (p == p3) {
				p.doRaise(rar, 200);
			}
			else if (p == p1) {
				p.doFold();
			}
			else {
				p.doCall(rar);
			}
		}

		//
		// Execute
		//
		d.initRoundStatusAfterRaise(p3);

		//
		// Verify
		//
		assertEquals(RoundStatus.FOLDED, p1.getRoundStatus());
		assertEquals(0, p1.getLastStepAction().getChip());
		assertEquals(RoundStatus.NONE, p2.getRoundStatus());
		assertEquals(10, p2.getLastStepAction().getChip());
		assertEquals(RoundStatus.RAISED, p3.getRoundStatus());
		assertEquals(200, p3.getLastStepAction().getChip());
	}

	/**
	 * Raise, Call,Fold をinitRoundする
	 * @throws RoundRulesException
	 */
	@Test
	public void testinitRoundStatusAfterRaise_keepAllIn() throws RoundRulesException
	{
		//
		// Setup
		//
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(10);
		for(int i = 0; i < d.getPlayers().size(); i++)
		{
			HoldemPlayer p = (HoldemPlayer)d.getPlayers().get(i);
			if (p == p3) {
				p.doRaise(rar, 200);
			}
			else if (p == p1) {
				p.doAllIn(rar);
			}
			else {
				p.doCall(rar);
			}
		}

		//
		// Execute
		//
		d.initRoundStatusAfterRaise(p3);

		//
		// Verify
		//
		assertEquals(RoundStatus.ALLINED, p1.getRoundStatus());
		assertEquals(1000, p1.getLastStepAction().getChip());
		assertEquals(RoundStatus.NONE, p2.getRoundStatus());
		assertEquals(10, p2.getLastStepAction().getChip());
		assertEquals(RoundStatus.RAISED, p3.getRoundStatus());
		assertEquals(200, p3.getLastStepAction().getChip());
	}

	/**
	 * １ハンドの勝敗をテスト
	 * @throws RoundRulesException
	 */
	public void testConclueHand() throws RoundRulesException
	{
		//
		// Setup
		//
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		p1.receivePocket(new Card(Suits.CRAB, 7));
		p1.receivePocket(new Card(Suits.CRAB, 8));
		p2.receivePocket(new Card(Suits.CRAB, 9));
		p2.receivePocket(new Card(Suits.CRAB, 10));
		p3.receivePocket(new Card(Suits.CRAB, 11));
		p3.receivePocket(new Card(Suits.CRAB, 12));

		for(int i = 0; i < d.getPlayers().size(); i++)
		{
			HoldemPlayer p = (HoldemPlayer)d.getPlayers().get(i);
			p.doCall(rar);
		}

		List<Pot> pots = d.collectChipToPot();
		CardSet board = _initBoard();
		//
		// Execute
		//
		d.concludeHand(pots.get(0), board);

		//
		// Verify
		//
		assertEquals(900, p1.getChip());
		assertEquals(900, p2.getChip());
		assertEquals(1200, p3.getChip());
	}

	/**
	 * １ハンドの勝敗をテスト
	 * @throws RoundRulesException
	 */
	public void testConclueHand_draw2player() throws RoundRulesException
	{
		//
		// Setup
		//
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		p1.receivePocket(new Card(Suits.CRAB, 10));
		p1.receivePocket(new Card(Suits.CRAB, 7));
		p2.receivePocket(new Card(Suits.DIA, 7));
		p2.receivePocket(new Card(Suits.CRAB, 8));
		p3.receivePocket(new Card(Suits.HEART, 7));
		p3.receivePocket(new Card(Suits.DIA, 10));

		for(int i = 0; i < d.getPlayers().size(); i++)
		{
			HoldemPlayer p = (HoldemPlayer)d.getPlayers().get(i);
			p.doCall(rar);
		}
		List<Pot> pots = d.collectChipToPot();
		CardSet board = _initBoard();

		//
		// Execute
		//
		d.concludeHand(pots.get(0), board);

		//
		// Verify
		// p1とp3が引き分けて、150ずつ貰う
		//
		assertEquals(1050, p1.getChip());
		assertEquals(900, p2.getChip());
		assertEquals(1050, p3.getChip());
	}

	public void testConcludeHand_sidePot() throws RoundRulesException
	{
		//
		// Setup
		//
		p1.receivePocket(new Card(Suits.CRAB, 13));
		p1.receivePocket(new Card(Suits.CRAB, 6));
		p2.receivePocket(new Card(Suits.CRAB, 7));
		p2.receivePocket(new Card(Suits.CRAB, 8));
		p3.receivePocket(new Card(Suits.CRAB, 9));
		p3.receivePocket(new Card(Suits.CRAB, 10));

		p1.SetChip(100);
		p2.SetChip(1000);
		p3.SetChip(1000);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doAllIn(rar);
		p2.doRaise(rar, 200);
		rar.setCallAmount(200);
		p3.doCall(rar);

		List<Pot> pots = d.collectChipToPot();
		CardSet board = _initBoard();

		assertEquals(2, pots.size());
		// 1ポット目
		Pot pt = pots.get(0);
		assertEquals(300, pt.getChip());
		assertEquals(3, pt.getPlayers().size());
		assertEquals(p1, pt.getPlayers().get(0));
		assertEquals(p2, pt.getPlayers().get(1));
		assertEquals(p3, pt.getPlayers().get(2));

		// Execute
		d.concludeHand(pots.get(0), board);
		assertEquals(300, p1.getChip());
		assertEquals(800, p2.getChip());
		assertEquals(800, p3.getChip());

		// 2ポット目
		pt = pots.get(1);
		assertEquals(200, pt.getChip());
		assertEquals(2, pt.getPlayers().size());
		assertEquals(p2, pt.getPlayers().get(0));
		assertEquals(p3, pt.getPlayers().get(1));
		// Execute
		d.concludeHand(pots.get(1), board);
		assertEquals(300, p1.getChip());
		assertEquals(800, p2.getChip());
		assertEquals(1000, p3.getChip());
	}
	/**
	 * Foldしていないうち、最小のチップ額を求める
	 * @throws RoundRulesException
	 */
	public void testGetMinimumChipFromAction() throws RoundRulesException
	{
		//
		// Setup
		//
		p1.SetChip(100);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doCall(rar);
		p2.doRaise(rar, 200);
		rar.setCallAmount(200);
		p3.doCall(rar);
		p1.doFold();

		assertEquals(200, d.getMinimumChipFromHandTotalChip(0));
	}

	/**
	 * p1が100でCall, p2が200にRaise, p3が200をcall, p1がfold
	 * →500のpotが一つ作られる
	 * 	２人が参加する500のPot
	 * @throws RoundRulesException
	 */
	public void testCollectChipToPot() throws RoundRulesException
	{
		//
		// Setup
		//
		p1.SetChip(100);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doCall(rar);
		p2.doRaise(rar, 200);
		rar.setCallAmount(200);
		p3.doCall(rar);
		p1.doFold();

		List<Pot> pots = new ArrayList<Pot>();

		//
		// Execute
		//
		pots = d.collectChipToPot();

		//
		// Verify
		//
		assertEquals(1, pots.size());
		assertEquals(500, pots.get(0).getChip());
		assertEquals(2, pots.get(0).getPlayers().size());
		assertEquals(p2, pots.get(0).getPlayers().get(0));
		assertEquals(p3, pots.get(0).getPlayers().get(1));
	}

	/**
	 * p1が100でAllIn, p2が200にRaise, p3が200をcall
	 * →サイドポットが作られる
	 * 	３人が参加する300のPot
	 * 	２人が参加する200のPot
	 * @throws RoundRulesException
	 */
	public void testCollectChipToPot_sidePot() throws RoundRulesException
	{
		p1.SetChip(100);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doAllIn(rar);
		p2.doRaise(rar, 200);
		rar.setCallAmount(200);
		p3.doCall(rar);

		List<Pot> pots = new ArrayList<Pot>();

		//
		// Execute
		//
		pots = d.collectChipToPot();

		//
		// Verify
		//
		assertEquals(2, pots.size());
		assertEquals(300, pots.get(0).getChip());
		assertEquals(3, pots.get(0).getPlayers().size());
		assertEquals(p1, pots.get(0).getPlayers().get(0));
		assertEquals(p2, pots.get(0).getPlayers().get(1));
		assertEquals(p3, pots.get(0).getPlayers().get(2));

		assertEquals(200, pots.get(1).getChip());
		assertEquals(2, pots.get(1).getPlayers().size());
		assertEquals(p2, pots.get(1).getPlayers().get(0));
		assertEquals(p3, pots.get(1).getPlayers().get(1));
	}

	/**
	 * p1が100でAllIn, p2が200にRaise, p3が200をcall
	 * →サイドポットが作られる
	 * 	３人が参加する300のPot
	 * 	２人が参加する200のPot
	 * @throws RoundRulesException
	 */
	public void testCollectChipToPot_sidePot2() throws RoundRulesException
	{
		HoldemPlayer p4 = new HoldemPlayer(4, "gorou");
		HoldemPlayer p5 = new HoldemPlayer(5, "rokurou");
		d.addPlayer(p4);
		d.addPlayer(p5);

		p1.SetChip(100);
		p3.SetChip(200);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doAllIn(rar); // call_allin
		p2.doCall(rar);
		p3.doRaise(rar, 200); // raise_allin
		rar.setCallAmount(200);
		p4.doRaise(rar, 300);
		rar.setCallAmount(300);
		p5.doCall(rar);
		p2.doFold();

		List<Pot> pots = new ArrayList<Pot>();

		//
		// Execute
		//
		pots = d.collectChipToPot();

		//
		// Verify
		//
		assertEquals(3, pots.size());
		assertEquals(500, pots.get(0).getChip());
		assertEquals(4, pots.get(0).getPlayers().size());
		assertEquals(p1, pots.get(0).getPlayers().get(0));
		assertEquals(p3, pots.get(0).getPlayers().get(1));
		assertEquals(p4, pots.get(0).getPlayers().get(2));
		assertEquals(p5, pots.get(0).getPlayers().get(3));

		assertEquals(300, pots.get(1).getChip());
		assertEquals(3, pots.get(1).getPlayers().size());
		assertEquals(p3, pots.get(1).getPlayers().get(0));
		assertEquals(p4, pots.get(1).getPlayers().get(1));
		assertEquals(p5, pots.get(1).getPlayers().get(2));

		assertEquals(200, pots.get(2).getChip());
		assertEquals(2, pots.get(2).getPlayers().size());
		assertEquals(p4, pots.get(2).getPlayers().get(0));
		assertEquals(p5, pots.get(2).getPlayers().get(1));
	}

	/**
	 * p1が100でCall, p2が200にRaise, p3が200をcall, p1がfold
	 * →500のpotが一つ作られる
	 * 	２人が参加する500のPot
	 * →Flopで全員0bet
	 * →500のpotが一つ作られる
	 * @throws RoundRulesException
	 */
	public void testCollectChipToPot_flop() throws RoundRulesException
	{
		//
		// Setup
		//
		p1.SetChip(100);
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		p1.doCall(rar);
		p2.doRaise(rar, 200);
		rar.setCallAmount(200);
		p3.doCall(rar);
		p1.doFold();

		List<Pot> pots = new ArrayList<Pot>();

		//
		// Execute
		//
		pots = d.collectChipToPot();

		//
		// Verify
		//
		assertEquals(1, pots.size());
		assertEquals(500, pots.get(0).getChip());
		assertEquals(2, pots.get(0).getPlayers().size());
		assertEquals(p2, pots.get(0).getPlayers().get(0));
		assertEquals(p3, pots.get(0).getPlayers().get(1));

		//
		// Flop
		//
		d.initStep();
		rar.setCallAmount(0);
		p2.doCall(rar);
		p3.doCall(rar);

		//
		// Execute
		//
		pots = d.collectChipToPot();

		//
		// Verify
		//
		assertEquals(1, pots.size());
		assertEquals(500, pots.get(0).getChip());
		assertEquals(2, pots.get(0).getPlayers().size());
		assertEquals(p2, pots.get(0).getPlayers().get(0));
		assertEquals(p3, pots.get(0).getPlayers().get(1));

	}

	@Test
	public void testActionToRoundActionRule()
	{
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);

		StepAction ac = new RaiseAction(200);
		d.actionToRoundActionRule(rar, ac);

		assertEquals(200, rar.getCallAmount());
		assertEquals(300, rar.getMinRaiseAmount());

		rar.setCallAmount(100);
		ac = new RaiseAllInAction(400);
		d.actionToRoundActionRule(rar, ac);
		assertEquals(400, rar.getCallAmount());
		assertEquals(700, rar.getMinRaiseAmount());
	}

	@Test
	public void testIsAllFolded()
	{
		assertEquals(false, d.isAllFolded());

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		p1.doAllIn(rar);
		p2.doFold();
		p3.doFold();

		assertEquals(true, d.isAllFolded());
	}

	@Test
	public void testDealBoard() {
		CardSet board = new CardSet();
		Step s = Step.PREFLOP;
		board.mergeCardSet(d.dealBoard(s));

		assertEquals(0, board.size());

		s = Step.FLOP;
		board.mergeCardSet(d.dealBoard(s));
		assertEquals(3, board.size());

		s = Step.TURN;
		board.mergeCardSet(d.dealBoard(s));
		assertEquals(4, board.size());

		s = Step.RIVER;
		board.mergeCardSet(d.dealBoard(s));
		assertEquals(5, board.size());
	}

	@Test
	public void testInitStep() throws RoundRulesException
	{
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		p1.doFold();
		p2.doCall(rar);
		p3.doAllIn(rar);
		d.initStep();

		assertEquals(RoundStatus.FOLDED, p1.getLastStepAction().getRoundStatus());
		assertEquals(RoundStatus.NONE, p2.getLastStepAction().getRoundStatus());
		assertEquals(RoundStatus.ALLINED, p3.getLastStepAction().getRoundStatus());
	}

	@Test
	public void testResetHand() throws RoundRulesException
	{
		d.dealAllPlayers();
		d.dealAllPlayers();

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		p1.doFold();
		p2.doCall(rar);
		p3.doAllIn(rar);

		d.resetHand();
		for(Player p : d.getPlayers()) {
			HoldemPlayer hp = (HoldemPlayer)p;
			assertEquals(RoundStatus.NONE, hp.getLastStepAction().getRoundStatus());
			assertEquals(0, hp.getLastStepAction().getChip());
			assertEquals(0, hp.getHandTotalChip());
			assertEquals(0, hp.getPocketSize());
		}
	}

	@Test
	public void testGetMaxFiveCard()
	{
		CardSet cs = new CardSet("STH4S5S6HTH8S4");
		FiveCard fv = d.getMaxFiveCard(cs);
		assertEquals(true, fv.getYaku() instanceof YkTwoPair);

		cs = new CardSet("♠4♠J♥A♠8♦Q♥5♠A");
		fv = d.getMaxFiveCard(cs);
		assertEquals(true, fv.getYaku() instanceof YkPair);
		assertEquals(1, ((YkPair)fv.getYaku()).getPairRank());

		cs = new CardSet("♥4♠J♥A♠8♠Q♠T♥T");
		fv = d.getMaxFiveCard(cs);
		assertEquals(true, fv.getYaku() instanceof YkPair);
		assertEquals(10, ((YkPair)fv.getYaku()).getPairRank());
	}
}
