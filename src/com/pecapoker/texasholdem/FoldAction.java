package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class FoldAction extends StepAction {
	public FoldAction(int lastChip)
	{
		super();
		this.addChip(lastChip);
		this.roundStatus = RoundStatus.FOLDED;
	}

	@Override
	public boolean isFold()
	{
		return true;
	}
}
