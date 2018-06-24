package com.pecapoker.texasholdem;

public class AiCallerHoldemPlayer extends AiHoldemPlayer {
	public AiCallerHoldemPlayer(int id, String name) {
		super(id, name);
	}

	@Override
	protected int _getActionNo(RoundActionRule rar)
	{
    	return AC_CALL;
	}
}
