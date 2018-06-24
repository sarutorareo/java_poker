package com.pecapoker.playingcards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pecapoker.playingcards.PcConst.Suits;

public class DeckTest {

	@Test
	public void testConstructor() {
		Deck deck = new Deck(false);
		assertEquals(52, deck.size());

		for (Suits s : PcConst.ALL_SUITS) {
			for (int no = 1; no <= PcConst.NO_KING; no++) {
				Card c = deck.pop();
				assertEquals(s, c.getSuits());
				assertEquals(no, c.getRank());
			}
		}

		// �S�Ĕ������� pop��null��Ԃ�
		Card c = deck.pop();
		assertEquals(null, c);
	}

	@Test
	public void testShuffle() {
		Deck deck = new Deck(false);
		deck.shuffle();

		final int NUM = deck.size();
		Suits beforeSuits = Suits.SPADE;
		int beforeNo = 1;
		int diffSuits = 0;
		int diffNo = 0;

		for (int i = 0; i < NUM; i++)
		{
			Card c = deck.pop();
			if (c.getSuits() != beforeSuits) {
				diffSuits++;
			}
			if (c.getRank() != beforeNo + 1)
			{
				diffNo++;
			}
			beforeSuits = c.getSuits();
			beforeNo = c.getRank();
		}
		// �P�O��ȏ�X�[�c�A�������ω����邱��
		System.out.println("diffSuits=" + diffSuits + ", diffNo=" + diffNo);
		assertEquals(true, diffSuits >= 10);
		assertEquals(true, diffNo >= 10);

	}
}
