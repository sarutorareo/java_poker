package com.pecapoker.texasholdem;

import com.pecapoker.texasholdem.HdConst.RoundStatus;

class HoldemPlayer extends com.pecapoker.playingcards.Player {
	protected final int AC_FOLD = 0;
	protected final int AC_CALL = 1;
	protected final int AC_RAISE = 2;
	protected final int AC_ALLIN = 9;

	protected StepAction lastStepAction;
	private int handTotalChip = 0;

	public HoldemPlayer(int id, String name)
	{
		super(id, name);
		initStepAction();
	}
	public RoundStatus getRoundStatus() {
		return lastStepAction.getRoundStatus();
	}
	public int getHandTotalChip() {
		return handTotalChip;
	}

	public void initStepAction() {
		this.lastStepAction = new NoneAction(0);
	}
	public void resetStepActionChipOnly() {
		this.lastStepAction.removeChip(this.lastStepAction.getChip());
	}
	public void resetStepActionStatusOnly()
	{
		this.lastStepAction = new NoneAction(lastStepAction.getChip());
	}

	/**
	 * アクションを選択する
	 * @return 選択したアクション
	 */
	public StepAction getRoundAction(RoundActionRule rar) throws RoundRulesException
	{
		int actionNo = _getActionNo(rar);
		if (actionNo == AC_CALL)
		{
			return doCall(rar);
		}
		else if (actionNo == AC_RAISE)
		{
			int amount = _getRaiseAmount(rar);
			return doRaise(rar, amount);
		}
		else if (actionNo == AC_ALLIN)
		{
			return doAllIn(rar);
		}
		else {
			return doFold();
		}
	}

	protected int _getActionNo(RoundActionRule rar)
	{
		return AC_FOLD;
	}
	protected int _getRaiseAmount(RoundActionRule rar)
	{
		return 100;
	}

	public StepAction doCall(RoundActionRule rar) throws RoundRulesException {

		int diffAmount = rar.getCallAmount() - this.lastStepAction.getChip();
		assert diffAmount >= 0;
		if (this.chip < diffAmount) {
			throw new RoundRulesException(" this.chip < diffAmount (" + this.getChip() + " < " + diffAmount + ")");
		}
		this.chip -= diffAmount;
		this.handTotalChip += diffAmount;
		if (this.chip == 0) {
			this.lastStepAction = new CallAllInAction(this.lastStepAction.getChip() + diffAmount);
			System.out.println(this + " callAllIn ");
		}
		else {
			this.lastStepAction =  new CallAction(this.lastStepAction.getChip() + diffAmount);
			System.out.println(this + " call");
		}
		return this.lastStepAction;
	}

	public StepAction doRaise(RoundActionRule rar, int amount) throws RoundRulesException {

		int diffAmount = amount - this.lastStepAction.getChip();
		if (this.chip < diffAmount) {
			throw new RoundRulesException(" this.chip < diffAmount (" + this.getChip() + " < " + diffAmount + ")");
		}
		this.chip -= diffAmount;
		this.handTotalChip += diffAmount;
		if (this.chip == 0) {
			this.lastStepAction = new RaiseAllInAction(this.lastStepAction.getChip() + diffAmount);
			System.out.println(this + " raiseAllIn make " + lastStepAction.getChip());
		}
		else {
			this.lastStepAction = new RaiseAction(this.lastStepAction.getChip() + diffAmount);
			System.out.println(this + " raise make " + lastStepAction.getChip());
		}

		return this.lastStepAction;
	}

	public FoldAction doFold()
	{
		this.lastStepAction = new FoldAction(this.lastStepAction.getChip());

		System.out.println(this + " fold");
		return (FoldAction)this.lastStepAction;
	}

	public StepAction doAllIn(RoundActionRule rar)
	{
		if (rar.getCallAmount() < this.lastStepAction.getChip() + this.getChip())
		{
			this.lastStepAction = new RaiseAllInAction(this.lastStepAction.getChip() + this.getChip());
			System.out.println(this + " raise_allin");
		}
		else {
			this.lastStepAction = new CallAllInAction(this.lastStepAction.getChip() + this.getChip());
			System.out.println(this + " call_allin");
		}
		this.handTotalChip += this.chip;
		this.chip = 0;

		return this.lastStepAction;
	}

	public StepAction getLastStepAction() {
		return this.lastStepAction;
	}

	public boolean isRaised()
	{
		return lastStepAction.isRaise();
	}

	public boolean isFolded()
	{
		return lastStepAction.isFold();
	}

	public boolean isCalled()
	{
		return lastStepAction.isCall();
	}
	public boolean isAllIned() {
		return lastStepAction.isAllIn();
	}
	public void lastStepActionChipToHandTotal() {
		this.handTotalChip += this.lastStepAction.getChip();
	}
	public void resetHandTotalChip() {
		this.handTotalChip = 0;
	}

}
