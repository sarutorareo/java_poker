package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class RaiseAction extends StepAction {
	public RaiseAction(int chip) {
		super();
		addChip(chip);
		this.roundStatus = RoundStatus.RAISED;
	}

	@Override
	public boolean isRaise()
	{
		return true;
	}
}
