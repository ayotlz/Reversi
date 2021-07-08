package io.deeplay.reversi;

import io.deeplay.reversi.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;

import java.io.IOException;

public class Selfplay {
    public static void main(String[] args) throws ReversiException, IOException {
        Bot[] bots = new Bot[2];
        bots[0] = new Bot();
        bots[1] = new Bot();
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);
        System.out.println(board.toString());

        Color color = Color.BLACK;
        while (!handler.isGameEnd(board)) {
            for (Bot bot : bots) {
                if (handler.makeStep(board, color, bot.getAnswer())) {
                    color = color.reverseColor();
                }

                System.out.println(board.toString());
            }
        }
    }
}
