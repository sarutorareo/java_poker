package com.pecapoker.texasholdem;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class HoldemPlayerTest {

	@Test
	public void testConstructor() {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1, p.getId());
		assertEquals("hiyoten", p.getName());
	}

	/**
	 * コールするとチップが減る、状態がCALLEDになる
	 */
	@Test
	public void testCall() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		StepAction ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(100, ac.getChip());

		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(900, p.getChip());
	}

	/**
	 * コールした後、他プレイヤーがレイズしてそれをさらにコール
	 * すると差分のチップが減る、状態がCALLEDになる
	 */
	@Test
	public void testCallAfterRaise() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		// まずコール
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		StepAction ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(100, ac.getChip());

		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(900, p.getChip());
		// 他の人が300にRaiseしたとして・・・
		rar.setCallAmount(300);

		// 追加で200分をコール
		ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(300, ac.getChip());

		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(700, p.getChip());
	}

    @Rule
    public ExpectedException exception = ExpectedException.none();

	/**
	 * Raiseするとチップが減る、状態がRAISEDになる
	 */
	@Test
	public void testRaise() throws RoundRulesException {
		//
		// 普通にRaise
		//
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		//200にレイズ
		StepAction ac = p.doRaise(rar, 200);

		assertEquals(true, ac instanceof RaiseAction);
		assertEquals(200, ac.getChip());

		assertEquals(RoundStatus.RAISED, p.getRoundStatus());
		assertEquals(800, p.getChip());

		//
		// 所持金額以上をかけたらException
		//
        exception.expect(RoundRulesException.class);
        exception.expectMessage("this.chip < diffAmount");
        ac = p.doRaise(rar, 1100);
	}

	/**
	 * コールした後、他プレイヤーがレイズしてそれをさらにレイズ
	 * すると差分のチップが減る、状態がRAISEDになる
	 */
	@Test
	public void testRaiseAfterRaise() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		// まずコール
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		StepAction ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(100, ac.getChip());

		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(900, p.getChip());
		// 他の人が300にRaiseしたとして・・・
		rar.setCallAmount(300);

		// 追加で600にレイズ
		ac = p.doRaise(rar, 600);

		assertEquals(true, ac instanceof RaiseAction);
		assertEquals(600, ac.getChip());

		assertEquals(RoundStatus.RAISED, p.getRoundStatus());
		assertEquals(400, p.getChip());
	}

	/**
	 * フォールドするとチップは減らない、状態がFOLDEDになる
	 */
	@Test
	public void testFold() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		StepAction ac = p.doFold();

		assertEquals(true, ac instanceof FoldAction);
		assertEquals(0, ac.getChip());

		assertEquals(RoundStatus.FOLDED, p.getRoundStatus());
		assertEquals(1000, p.getChip());
	}

	/**
	 * コールした後、他プレイヤーがレイズして,Fold
	 * すると差分のチップは減らないが、
	 * LastActionではコールしたぶんのChipを覚えておく
	 * 状態がFOLDEDになる
	 */
	@Test
	public void testFoldAfterRaise() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		// まずコール
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		StepAction ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(100, ac.getChip());

		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(900, p.getChip());
		// 他の人が300にRaiseしたとして・・・
		rar.setCallAmount(300);

		// FOLD
		ac = p.doFold();

		assertEquals(true, ac instanceof FoldAction);
		assertEquals(100, ac.getChip());

		assertEquals(RoundStatus.FOLDED, p.getRoundStatus());
		assertEquals(900, p.getChip());
	}

	/**
	 * 現在のコール額よりもチップが多い状態でオールインすると
	 * 状態がRAISE_ALLINED
	 * チップは0になる
	 */
	@Test
	public void testRaiseAllIn() {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		StepAction ac = p.doAllIn(rar);

		assertEquals(true, ac instanceof RaiseAllInAction);
		assertEquals(1000, ac.getChip());

		assertEquals(RoundStatus.ALLINED, p.getRoundStatus());
		assertEquals(0, p.getChip());
	}

	/**
	 * 現在のコール額よりもチップが同じか少ない状態でオールインすると
	 * 状態がCALL_ALLINED
	 * チップは0になる
	 */
	@Test
	public void testCallAllIn() {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(1000);
		StepAction ac = p.doAllIn(rar);

		assertEquals(true, ac instanceof CallAllInAction);
		assertEquals(1000, ac.getChip());

		assertEquals(RoundStatus.ALLINED, p.getRoundStatus());
		assertEquals(0, p.getChip());
	}
	/**
	 * コールした後、他プレイヤーがレイズして,AllIn
	 * すると差分のチップは減らないが、
	 * LastActionではコールしたぶんのChipを覚えておく
	 * 状態がALLINEDになる
	@Test
	public void testFoldAfterRaiseAllIn() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		assertEquals(RoundStatus.NONE, p.getRoundStatus());

		// まずコール
		RoundActionRule rar = new RoundActionRule();
		rar.setCallAmount(100);
		Action ac = p.doCall(rar);

		assertEquals(true, ac instanceof CallAction);
		assertEquals(100, ac.getChip());
		assertEquals(RoundStatus.CALLED, p.getRoundStatus());
		assertEquals(900, p.getChip());

		// 他の人が300にRaiseしたとして・・・
		rar.setCallAmount(300);

		// ALLIN
		ac = p.doRaiseAllIn();

		assertEquals(true, ac instanceof RaiseAllInAction);
		assertEquals(1000, ac.getChip());

		assertEquals(RoundStatus.RAISE_ALLINED, p.getRoundStatus());
		assertEquals(0, p.getChip());
	}
		 */
	@Test
	public void testIsRaised() {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		RoundActionRule rar = new RoundActionRule();

		rar.setCallAmount(100);
		p.doAllIn(rar);

		assertEquals(true, p.isRaised());

		rar.setCallAmount(1000);
		p.doAllIn(rar);

		assertEquals(false, p.isRaised());
	}

	/**
	 * Raiseを選び、手動で全額をベットした場合は、raiseではなくRaiseAllIn扱い
	 * @throws RoundRulesException
	 */
	@Test
	public void testManualRaiseAllIn() throws RoundRulesException {
		HoldemPlayer p = new HoldemPlayer(1, "hiyoten");
		assertEquals(1000, p.getChip());
		RoundActionRule rar = new RoundActionRule();

		rar.setCallAmount(100);
		StepAction ac = p.doRaise(rar, 1000);

		assertEquals(true, p.isRaised());
		assertEquals(true, ac instanceof RaiseAllInAction);
		assertEquals(1000, ac.getChip());

		assertEquals(RoundStatus.ALLINED, p.getRoundStatus());
		assertEquals(0, p.getChip());
	}
	
}
