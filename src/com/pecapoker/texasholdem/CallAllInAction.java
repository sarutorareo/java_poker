package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class CallAllInAction extends StepAction {
	public CallAllInAction(int chip) {
		super();
		addChip(chip);
		this.roundStatus = RoundStatus.ALLINED;
	}

	@Override
	public boolean isAllIn()
	{
		return true;
	}
}
