package com.pecapoker.playingcards;

import java.util.ArrayList;

public class Pot {
	private int chip = 0;
	private ArrayList<Player> players;

	public Pot()
	{
		players = new ArrayList<Player>();
	}

	public int getChip() {
		return chip;
	}

	public void addChip(int chip) {
		this.chip += chip;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public void addPlayer(Player p) {
		this.players.add(p);
	}

}
