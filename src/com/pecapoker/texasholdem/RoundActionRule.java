package com.pecapoker.texasholdem;

public class RoundActionRule {
	private int bigBlind = 100;
	private int callAmount = bigBlind;
	private int lastRaiseDiffAmount = bigBlind;

	/**
	 * コールに必要な額
	 * @return
	 */
	public int getCallAmount() {
		return callAmount;
	}
	public void setCallAmount(int callAmount) {
		this.callAmount = callAmount;
	}

	/**
	 * レイズに必要な最少額
	 * @return
	 */
	public int getMinRaiseAmount() {
		return callAmount + lastRaiseDiffAmount;
	}
	public void setLastRaiseDiffAmount(int amount) {
		this.lastRaiseDiffAmount = amount;
	}
}
