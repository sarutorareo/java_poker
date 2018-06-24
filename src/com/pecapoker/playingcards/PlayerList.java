package com.pecapoker.playingcards;

import java.util.ArrayList;

public class PlayerList extends ArrayList<Player> {
	public int getNextIndex(int current)
	{
		if (this.size() == 0) {
			return -1;
		}
		if (current < this.size()-1) {
			return current + 1;
		}
		else {
			return 0;
		}
	}

	public void printAllPlayerChips() {
		for(Player p : this)
		{
			if (p != this.get(0)) {
				System.out.print(":");
			}
			System.out.print(p + "=" + p.getChip());
		}
		System.out.println();
	}
}
