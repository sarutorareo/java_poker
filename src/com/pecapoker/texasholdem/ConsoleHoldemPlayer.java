package com.pecapoker.texasholdem;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleHoldemPlayer extends HoldemPlayer {

	public ConsoleHoldemPlayer(int id, String name) {
		super(id, name);
	}

	@Override
	protected int _getRaiseAmount(RoundActionRule rar)
	{
		System.out.println(this + " Raise amount ? (min " +  rar.getMinRaiseAmount() + ")");
		int amount = 0;

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try {
			String buf = br.readLine();
			amount = Integer.parseInt(buf);
		}
		catch (Exception ex)
		{
			;
		}
		if (amount < rar.getMinRaiseAmount())
		{
			amount = rar.getMinRaiseAmount();
		}
		return amount;
	}

	@Override
	protected int _getActionNo(RoundActionRule rar)
	{
		printPocket();
		if (_isAbleToRaise(rar.getMinRaiseAmount()))
		{
			System.out.println(this + " Select action 0:FOLD 1:CALL 2:RAISE 9:ALLIN" );
		}
		else if (_isAbleToCall(rar.getCallAmount())){
			System.out.println(this + " Select action 0:FOLD 1:CALL 9:ALLIN" );
		}
		else if (_isAbleToAllIn())
		{
			System.out.println(this + " Select action 0:FOLD 9:ALLIN" );
		}
		else {
			System.out.println(this + " Select action 0:FOLD" );
		}

		int actionNo = AC_FOLD;

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try {
			String buf = br.readLine();
			int an = Integer.parseInt(buf);
			if (an == AC_RAISE && !_isAbleToRaise(rar.getMinRaiseAmount()))
			{
				an = AC_FOLD;
			}
			else if (an == AC_CALL && !_isAbleToCall(rar.getCallAmount()))
			{
				an = AC_FOLD;
			}
			actionNo = an;
		} catch (Exception ex)
		{
			;
		}
		return actionNo;
	}

	private boolean _isAbleToRaise(int minimumRaiseAmount) {
		return (this.chip + this.lastStepAction.getChip()) >= minimumRaiseAmount;
	}
	private boolean _isAbleToCall(int callAmount) {
		return (this.chip + this.lastStepAction.getChip()) >= callAmount;
	}
	private boolean _isAbleToAllIn() {
		return this.chip > 0;
	}
}
