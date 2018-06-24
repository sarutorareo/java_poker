package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

public class StepAction {
	private int chip = 0;
	protected RoundStatus roundStatus = RoundStatus.NONE;

	public RoundStatus getRoundStatus() {
		return roundStatus;
	}
	public int getChip() {
		return chip;
	}

	public void addChip(int chip) {
		this.chip += chip;
	}
	public void removeChip(int chip) {
		this.chip -= chip;
	}
	public boolean isRaise()
	{
		return false;
	}
	public boolean isFold()
	{
		return false;
	}
	public boolean isCall()
	{
		return false;
	}
	public boolean isAllIn()
	{
		return false;
	}
}
