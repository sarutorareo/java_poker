package com.pecapoker.texasholdem;

public class AiFolderHoldemPlayer extends AiHoldemPlayer {
	public AiFolderHoldemPlayer(int id, String name) {
		super(id, name);
	}

	@Override
	protected int _getActionNo(RoundActionRule rar)
	{
    	return AC_FOLD;
	}
}
