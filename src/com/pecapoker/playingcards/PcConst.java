package com.pecapoker.playingcards;

public class PcConst {
	public enum Suits {SPADE, HEART, DIA, CRAB};

	public static Suits CharToSuits(char c) {
		switch(c) {
		case 'S':
		case '♠':
			return Suits.SPADE;
		case 'H':
		case '♥':
			return Suits.HEART;
		case 'D':
		case '◆':
			return Suits.DIA;
		case 'C':
		case '♣':
			return Suits.CRAB;
		default:
			assert false;
			return Suits.SPADE;
		}
	}
	public static final int NO_KING = 13;
	public static final Suits[] ALL_SUITS = {Suits.SPADE, Suits.HEART, Suits.DIA, Suits.CRAB};
}
