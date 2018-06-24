package com.pecapoker.playingcards;

import java.util.Comparator;

public class CardComparator_value implements Comparator<Card>{
	//カードの強さを比較する(弱い順)
    public int compare(Card c1, Card c2) {
    	assert (c1 != null && c2 != null);
    	return c1.compareTo(c2);
    }

}
