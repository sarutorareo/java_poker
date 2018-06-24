package com.pecapoker.playingcards;

import java.util.Comparator;

public class CardComparator_rank implements Comparator<Card>{
	//カードのランクを比較する(値の小さい順)
    public int compare(Card c1, Card c2) {
    	assert (c1 != null && c2 != null);
    	return c1.getRank() - c2.getRank();
    }
}
