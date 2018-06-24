package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class CallAction extends StepAction {
	public CallAction(int chip) {
		super();
		addChip(chip);
		this.roundStatus = RoundStatus.CALLED;
	}

	@Override
	public boolean isCall()
	{
		return true;
	}
}
