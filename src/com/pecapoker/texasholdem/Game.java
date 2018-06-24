package com.pecapoker.texasholdem;

import java.util.ArrayList;
import java.util.List;

import com.pecapoker.playingcards.CardSet;
import com.pecapoker.playingcards.Player;
import com.pecapoker.playingcards.Pot;

public class Game {
	public enum Step {PREFLOP, FLOP, TURN, RIVER};

	public static void main(String[] args) {
		System.out.println("hello texasholdem");

		try {
			HoldemDealer d = new HoldemDealer();
			HoldemPlayer cpu1 = new AiHoldemPlayer(1, "Hiyoten");
			HoldemPlayer cpu2 = new AiHoldemPlayer(2, "saburou");
			ConsoleHoldemPlayer human = new ConsoleHoldemPlayer(3, "Jirou");
			ConsoleHoldemPlayer human2 = new ConsoleHoldemPlayer(4, "Shirou");

			d.addPlayer(cpu1);
//			d.addPlayer(cpu2);
			d.addPlayer(human);
//			d.addPlayer(human2);

			// シャッフル
			d.shuffle();

			// どちらかのchipが0になるまで、または10ハンド
			for (int i = 0; i < 3; i++) {
				//
				// 1ハンド
				//
				{
					System.out.println("##### HAND " + (i+1) + "#####");
					List<Pot> pots = new ArrayList<Pot>();
					d.resetHand();

					// ２枚配る
					d.dealAllPlayers();
					d.dealAllPlayers();

					//
					// 1ラウンド
					//
					Step[] steps = {Step.PREFLOP, Step.FLOP, Step.TURN, Step.RIVER};
					CardSet board = new CardSet();
					for(Step s : steps)
					{
						d.initStep();
						// カードを出す
						board.mergeCardSet(d.dealBoard(s));
						System.out.println("Board : " + board );

						// 全員アクションさせる
						d.round();
						// 出されたチップからポットを作る
						pots = d.collectChipToPot();
						_printAllPots(pots);

						if (d.isAllFolded()) {
							break;
						}
					}

					// 勝敗を判定して、チップを分配する
					int index = 0;
					for (Pot pt : pots)
					{
						System.out.println("### pot" + index++ + " " + pt.getChip());
						List<HoldemPlayer> winners = d.concludeHand(pt, board);
						if (winners.size() == 0) {
							System.out.println("   draw");
						}
						else {
							System.out.print("   winner is ...");
							for(Player p : winners) {
								System.out.print(p + ", ");
							}
							System.out.println("");
						}
					}
				}
				d.getPlayers().printAllPlayerChips();
			}
			System.out.print("*** ");
			d.getPlayers().printAllPlayerChips();
		}
		catch( Exception ex) {
			System.out.println("!!!Exception occured" + ex.getMessage());
			System.out.println("!!! in " + ex.getStackTrace());
		}
	}

	private static void _printAllPots(List<Pot> pots) {
		int index = 0;
		for (Pot pt : pots)
		{
			System.out.println("### pot" + index++ + " " + pt.getChip());
		}
	}
}
