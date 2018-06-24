package com.pecapoker.texasholdem;

import java.security.SecureRandom;

public class AiHoldemPlayer extends HoldemPlayer {
	public AiHoldemPlayer(int id, String name) {
		super(id, name);
	}

	@Override
	protected int _getRaiseAmount(RoundActionRule rar)
	{
		return rar.getMinRaiseAmount();
	}

	@Override
	protected int _getActionNo(RoundActionRule rar)
	{
    	SecureRandom rnd = new SecureRandom();
    	int randVal = rnd.nextInt(300);
    	int cardVal = this.getHighestCard().getValue();

    	int actionVal = randVal + cardVal * 10;

    	if ((actionVal >= 200)
    			&& (this.getChip() + this.lastStepAction.getChip() >= rar.getMinRaiseAmount())
    			) {
    		return AC_RAISE;
    	}
    	if ((actionVal >= 100)
    			&& (this.getChip() + this.lastStepAction.getChip() >= rar.getCallAmount())){
    		return AC_CALL;
    	}

    	return AC_FOLD;
	}

}
